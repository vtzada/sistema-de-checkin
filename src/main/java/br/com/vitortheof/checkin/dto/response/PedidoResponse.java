package br.com.vitortheof.checkin.dto.response;

import br.com.vitortheof.checkin.model.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PedidoResponse(Long id,
                             String nomeComprador,
                             String emailComprador,
                             BigDecimal valorTotal,
                             StatusPedido status,
                             String checkoutUrl,
                             @JsonFormat(pattern = "dd/MM/yyyy")
                             LocalDateTime dataCriacao,
                             @JsonFormat(pattern = "dd/MM/yyyy")
                             LocalDateTime dataExpiracao,
                             List<ItemPedidoResponse> itens
) {
}

