package br.com.vitortheof.checkin.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventoResponse(Long id, String nome, LocalDateTime data, String descricao, String local) {
}
