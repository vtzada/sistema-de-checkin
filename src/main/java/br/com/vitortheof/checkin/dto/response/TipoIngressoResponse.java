package br.com.vitortheof.checkin.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TipoIngressoResponse(Long id, String nome,
                                   String descricao, List<LoteResponse> lotes) {
}
