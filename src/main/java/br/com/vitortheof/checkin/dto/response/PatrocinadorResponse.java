package br.com.vitortheof.checkin.dto.response;

import lombok.Builder;

@Builder
public record PatrocinadorResponse(String nome, String nomeEvento, String nomePacote) {
}
