package br.com.vitortheof.checkin.dto.response;

import lombok.Builder;

@Builder
public record PacoteResponse(String nome, int limiteJogadores, int limiteVips, int limiteConvidados) {
}
