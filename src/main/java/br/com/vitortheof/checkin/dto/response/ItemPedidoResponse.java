package br.com.vitortheof.checkin.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ItemPedidoResponse(Long id, String nomeTipoIngresso, BigDecimal precoUnitario){}
