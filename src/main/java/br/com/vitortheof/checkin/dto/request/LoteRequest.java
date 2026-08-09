package br.com.vitortheof.checkin.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoteRequest(
        @Min(value = 1, message = "O número de ordem deve ser pelo menos 1.")
        int numeroOrdem,
        @NotNull(message = "O preço é obrigatório")
        @Min(value = 0, message = "O valor não pode ser negativo")
        BigDecimal preco,
        @Min(value = 1, message = "A quantidade total deve ser pelo menos 1.")
        int qtdTotal,
        LocalDateTime dataInicio,
        LocalDateTime dataFim) {
}
