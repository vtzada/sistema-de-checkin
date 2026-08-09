package br.com.vitortheof.checkin.mapper;

import br.com.vitortheof.checkin.dto.response.ItemPedidoResponse;
import br.com.vitortheof.checkin.dto.response.PedidoResponse;
import br.com.vitortheof.checkin.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public static PedidoResponse toPedidoResponse(Pedido pedido, String checkoutUrl) {
        return PedidoResponse.builder()
                .id(pedido.getId())
                .nomeComprador(pedido.getNomeComprador())
                .emailComprador(pedido.getEmailComprador())
                .valorTotal(pedido.getValorTotal())
                .status(pedido.getStatusPedido())
                .checkoutUrl(checkoutUrl)
                .dataCriacao(pedido.getDataCriacao())
                .dataExpiracao(pedido.getDataExpiracao())
                .itens(pedido.getItens().stream()
                        .map(item -> ItemPedidoResponse.builder()
                                .id(item.getId())
                                .nomeTipoIngresso(item.getLote().getTipoIngresso().getNome())
                                .precoUnitario(item.getPrecoUnitario())
                                .build())
                        .toList())
                .build();
    }
}
