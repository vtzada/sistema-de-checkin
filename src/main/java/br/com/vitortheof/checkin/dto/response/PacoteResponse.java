package br.com.vitortheof.checkin.dto.response;

import lombok.Builder;

@Builder
public record PacoteResponse(Long id, String nome, int limiteJogadores, int limiteVips, int limiteConvidados) {
}
