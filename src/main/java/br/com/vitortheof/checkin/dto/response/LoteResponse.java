package br.com.vitortheof.checkin.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record LoteResponse(Long id, int numeroOrdem,
                           String nomeTipoIngresso,
                           BigDecimal preco, int qtdTotal,
                           int qtdVendida, int qtdDisponivel,
                           LocalDateTime dataInicio, LocalDateTime dataFim) {
}
