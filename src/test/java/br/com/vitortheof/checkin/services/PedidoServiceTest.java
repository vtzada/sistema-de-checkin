package br.com.vitortheof.checkin.services;

import br.com.vitortheof.checkin.dto.request.ItemPedidoRequest;
import br.com.vitortheof.checkin.dto.request.PedidoRequest;
import br.com.vitortheof.checkin.dto.response.PedidoResponse;
import br.com.vitortheof.checkin.exception.BusinessException;
import br.com.vitortheof.checkin.exception.ResourceNotFoundException;
import br.com.vitortheof.checkin.infra.MercadoPagoService;
import br.com.vitortheof.checkin.model.Lote;
import br.com.vitortheof.checkin.model.Pedido;
import br.com.vitortheof.checkin.model.TipoIngresso;
import br.com.vitortheof.checkin.model.Usuario;
import br.com.vitortheof.checkin.repository.LoteRepository;
import br.com.vitortheof.checkin.repository.PedidoRepository;
import br.com.vitortheof.checkin.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private LoteRepository loteRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private MercadoPagoService mercadoPagoService;

    @InjectMocks
    private PedidoService pedidoService;

    private Lote loteValido;
    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        TipoIngresso tipo = new TipoIngresso();
        tipo.setDescricao("Pista VIP");

        loteValido = new Lote();
        loteValido.setId(1L);
        loteValido.setPreco(new BigDecimal("100.00"));
        loteValido.setTipoIngresso(tipo);

        usuarioValido = new Usuario();
        usuarioValido.setId(10L);
        usuarioValido.setNome("João Silva");
        usuarioValido.setEmail("joao@email.com");
    }

    @Test
    @DisplayName("Deve criar um pedido com sucesso, quando o usuario estiver logado")
    void criarPedidoComUsuarioLogadoComSucesso() {
        ItemPedidoRequest itemReq = new ItemPedidoRequest(1L, 2);
        PedidoRequest request = new PedidoRequest("joao", "joao@email.com", List.of(itemReq));

        when(loteRepository.findById(1L)).thenReturn(Optional.of(loteValido));
        when(loteRepository.reservarEstoque(1L, 2)).thenReturn(1);

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));
        when(mercadoPagoService.criarPreferencia(any()))
                .thenReturn(new MercadoPagoService.PreferenceResult("pref-123", "https://mercadopago.com/checkout"));

        PedidoResponse response = pedidoService.criarPedido(request, usuarioValido);

        assertNotNull(response);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository, times(2)).save(pedidoCaptor.capture());

        Pedido pedidoSalvo = pedidoCaptor.getValue();
        assertEquals(usuarioValido, pedidoSalvo.getUsuario());
        assertEquals(new BigDecimal("200.00"), pedidoSalvo.getValorTotal());
        assertEquals(2, pedidoSalvo.getItens().size());
        assertEquals("pref-123", pedidoSalvo.getGatewayReferencia());
    }

    @Test
    void criarPedidoComoConvidadoComSucesso() {

        ItemPedidoRequest itemReq = new ItemPedidoRequest(1L, 2);
        PedidoRequest request = new PedidoRequest("Convidado", "convidado@email.com", List.of(itemReq));

        when(loteRepository.findById(1L)).thenReturn(Optional.of(loteValido));
        when(loteRepository.reservarEstoque(1L, 2)).thenReturn(1);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));
        when(mercadoPagoService.criarPreferencia(any()))
                .thenReturn(new MercadoPagoService.PreferenceResult("pref-guest", "https://mercadopago.com/checkout"));

        PedidoResponse response = pedidoService.criarPedido(request, null);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository, times(2)).save(pedidoCaptor.capture());

        Pedido pedidoSalvo = pedidoCaptor.getValue();
        assertNull(pedidoSalvo.getUsuario()); // Garante que usuário ficou nulo
        assertEquals("convidado@email.com", pedidoSalvo.getEmailComprador());

    }

    @Test
    @DisplayName("Deve lançar BusinessException quando não houver estoque no lote")
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        // ARRANGE
        ItemPedidoRequest itemReq = new ItemPedidoRequest(1L, 5);
        PedidoRequest request = new PedidoRequest("João", "joao@email.com", List.of(itemReq));

        when(loteRepository.findById(1L)).thenReturn(Optional.of(loteValido));
        when(loteRepository.reservarEstoque(1L, 5)).thenReturn(0); // 0 linhas = estoque acabou

        // ACT & ASSERT
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> pedidoService.criarPedido(request, usuarioValido)
        );

        assertTrue(exception.getMessage().contains("Estoque insuficiente"));

        verify(pedidoRepository, never()).save(any());
        verify(mercadoPagoService, never()).criarPreferencia(any());
    }
    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o lote não existir")
    void deveLancarExcecaoQuandoLoteNaoEncontrado() {

        ItemPedidoRequest itemReq = new ItemPedidoRequest(99L, 1);
        PedidoRequest request = new PedidoRequest("João", "joao@email.com", List.of(itemReq));

        when(loteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> pedidoService.criarPedido(request, usuarioValido)
        );

        verify(loteRepository, never()).reservarEstoque(anyLong(), anyInt());
    }
}

