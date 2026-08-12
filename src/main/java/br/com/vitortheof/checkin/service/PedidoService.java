package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.request.ItemPedidoRequest;
import br.com.vitortheof.checkin.dto.request.PedidoRequest;
import br.com.vitortheof.checkin.dto.response.PedidoResponse;
import br.com.vitortheof.checkin.exception.BusinessException;
import br.com.vitortheof.checkin.exception.ResourceNotFoundException;
import br.com.vitortheof.checkin.infra.EmailDadosIngressoCompra;
import br.com.vitortheof.checkin.infra.EmailService;
import br.com.vitortheof.checkin.infra.MercadoPagoService;
import br.com.vitortheof.checkin.infra.QrCodeService;
import br.com.vitortheof.checkin.mapper.PedidoMapper;
import br.com.vitortheof.checkin.model.*;
import br.com.vitortheof.checkin.model.enums.StatusPedido;
import br.com.vitortheof.checkin.repository.LoteRepository;
import br.com.vitortheof.checkin.repository.PedidoRepository;
import com.mercadopago.resources.order.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private static final long MINUTOS_PARA_EXPIRAR = 15;

    // Status que a API do Mercado Pago retorna para uma Order paga com sucesso.
    private static final String STATUS_ORDER_PROCESSADA = "processed";

    private final PedidoRepository pedidoRepository;
    private final LoteRepository loteRepository;
    private final MercadoPagoService mercadoPagoService;
    private final IngressoService ingressoService;
    private final EmailService emailService;
    private final QrCodeService qrCodeService;

    @Transactional
    public PedidoResponse criarPedido(PedidoRequest request, Usuario usuarioLogado) {
        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        // valida e reserva estoque de cada lote, se qualquer reserva falhar, a excecao estoura e o transactional
        // desfaz as reservas já feitas no msm pedido
        for (ItemPedidoRequest itemRequest : request.itens()) {
            Lote lote = loteRepository.findById(itemRequest.loteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado: id=" + itemRequest.loteId()));

            validarJanelaDeVendas(lote);

            int linhasAfetadas = loteRepository.reservarEstoque(lote.getId(), itemRequest.quantidade());
            if(linhasAfetadas == 0){
                throw new BusinessException(
                        "Estoque insuficiente para o lote \"" + lote.getTipoIngresso().getDescricao() +
                                "\" (lote " + lote.getNumeroOrdem() + ").");
            }
            // cada unidade comprada vira um pedidoItem proprio (1 ingresso = 1 qrcode)
            for (int i = 0; i < itemRequest.quantidade(); i++) {
                ItemPedido item = ItemPedido.builder()
                        .lote(lote)
                        .precoUnitario(lote.getPreco())
                        .build();
                itens.add(item);
                valorTotal = valorTotal.add(lote.getPreco());
            }
        }

        //cria o pedido em si, ja com os itens
        Pedido pedido = Pedido.builder()
                .usuario(usuarioLogado)
                .nomeComprador(request.nomeComprador())
                .emailComprador(request.emailComprador())
                .valorTotal(valorTotal)
                .statusPedido(StatusPedido.PENDENTE)
                .dataCriacao(LocalDateTime.now())
                .dataExpiracao(LocalDateTime.now().plusMinutes(MINUTOS_PARA_EXPIRAR))
                .itens(itens)
                .build();

        itens.forEach(item -> item.setPedido(pedido));

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        MercadoPagoService.PreferenceResult preferencia = mercadoPagoService.criarPreferencia(pedidoSalvo);

        pedidoSalvo.setGatewayReferencia(preferencia.preferenceId());
        pedidoRepository.save(pedidoSalvo);

        return PedidoMapper.toPedidoResponse(pedidoSalvo, preferencia.checkoutUrl());
    }

    // Chamado pelo webhook quando chega uma notificação de order.processed.
    // NÃO confia no status que vem no corpo da notificação — consulta a
    // Order de volta na API do Mercado Pago (buscarOrder) e só aprova o
    // pedido se a PRÓPRIA API confirmar que está "processed". Isso importa
    // porque a assinatura (x-signature) de eventos "order" está com uma
    // inconsistência não resolvida do lado do MP; sem essa segunda checagem,
    // um POST forjado pro nosso endpoint poderia liberar ingressos de graça.
    @Transactional
    public void processarPedidoPago(Long pedidoId, String orderId) {

        Pedido pedido = buscarPedidoOuFalhar(pedidoId);

        // Idempotência: se o Mercado Pago mandar a mesma notificação duas vezes,
        // não devemos gerar dois ingressos.
        if (pedido.getStatusPedido() != StatusPedido.PENDENTE) {
            log.info("Pedido {} já processado. Status atual: {}", pedidoId, pedido.getStatusPedido());
            return;
        }

        Order order = mercadoPagoService.buscarOrder(orderId);

        // Confere que a order realmente pertence a ESSE pedido (não só que
        // existe uma order válida qualquer) — evita que alguém tente usar o
        // orderId de um pagamento real pra aprovar um pedidoId diferente.
        if (order.getExternalReference() == null
                || !Objects.equals(order.getExternalReference(), pedidoId.toString())) {
            log.warn("external_reference da order {} não corresponde ao pedido {}. Recusando.",
                    orderId, pedidoId);
            return;
        }

        if (!STATUS_ORDER_PROCESSADA.equals(order.getStatus())) {
            log.info("Order {} do pedido {} está com status '{}', não '{}'. Não aprovando ainda.",
                    orderId, pedidoId, order.getStatus(), STATUS_ORDER_PROCESSADA);
            return;
        }

        log.info("Pagamento confirmado via API do Mercado Pago. pedido={}, order={}", pedidoId, orderId);
        aprovarPedido(pedido);
    }

    public PedidoResponse findById(Long id) {
        Pedido pedido = buscarPedidoOuFalhar(id);
        return PedidoMapper.toPedidoResponse(pedido, null);
    }

    private void aprovarPedido(Pedido pedido) {
        pedido.setStatusPedido(StatusPedido.PAGO);
        pedidoRepository.save(pedido);

        for (ItemPedido item : pedido.getItens()) {

            Ingresso ingresso = ingressoService.gerarIngresso(item);
            byte[] qrCodeBytes = qrCodeService.gerarQrCodeBytes(ingresso.getCodigoQR().toString());
            var dadosEmail = getEmailDadosIngressoCompra(pedido, item, ingresso);

            try {
                emailService.enviarEmailIngressoCompra(dadosEmail, qrCodeBytes);
            } catch (Exception e) {
                log.error("Falha ao enviar e-mail de ingresso. pedido={}, item={}",
                        pedido.getId(), item.getId(), e);
            }
        }
    }
    private static @NonNull EmailDadosIngressoCompra getEmailDadosIngressoCompra(Pedido pedido, ItemPedido item, Ingresso ingresso) {
        var evento = item.getLote().getTipoIngresso().getEvento();
        return new EmailDadosIngressoCompra(
                pedido.getEmailComprador(),
                pedido.getNomeComprador(),
                evento.getNome(),
                evento.getData(),
                evento.getLocal(),
                item.getLote().getTipoIngresso().getNome(),
                item.getPrecoUnitario(),
                ingresso.getCodigoQR().toString()
        );
    }

    //cancela pedidos e devolve estoque reservado pra esses lotes
    @Transactional
    public void expirarPedidosPendentes() {
        List<Pedido> vencidos = pedidoRepository.findByStatusPedidoAndDataExpiracaoBefore(StatusPedido.PENDENTE, LocalDateTime.now());

        if (vencidos.isEmpty()) {
            return;
        }
        log.info("Expirando {} pedido(s) pendente(s) vencido(s).", vencidos.size());
        for (Pedido pedido : vencidos) {
            cancelarPedidoELiberarEstoque(pedido);
        }
    }
    private void cancelarPedidoELiberarEstoque(Pedido pedido) {
        pedido.setStatusPedido(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);

        for (ItemPedido item : pedido.getItens()) {
            loteRepository.liberarEstoque(item.getLote().getId(), 1);
        }
    }

    private void validarJanelaDeVendas(Lote lote) {
        LocalDateTime agora = LocalDateTime.now();

        if(lote.getDataInicio() != null && agora.isBefore(lote.getDataInicio())) {
            throw new BusinessException("As vendas do lote \"" + lote.getTipoIngresso().getNome() + "\" ainda não começaram.");
        }
        if (lote.getDataFim() != null && agora.isAfter(lote.getDataFim())) {
            throw new BusinessException("As vendas do lote \"" + lote.getTipoIngresso().getNome() + "\" já foram encerradas.");
        }
    }

    private Pedido buscarPedidoOuFalhar(Long id){
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: id=" + id));
    }

}