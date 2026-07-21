package br.com.vitortheof.checkin.dto.request;

import java.time.LocalDateTime;

public record EventoRequest(String nome, LocalDateTime data, String local, boolean ativo) {
}
