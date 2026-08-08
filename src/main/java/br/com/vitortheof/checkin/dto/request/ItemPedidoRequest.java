package br.com.vitortheof.checkin.dto.request;

import jakarta.validation.constraints.*;

public record ItemPedidoRequest(
        @NotNull(message = "O lote é obrigatório.")
        Long loteId,
        @Min(value = 1, message = "A quantidade deve ser pelo menos 1.")
        int quantidade
) {
}
