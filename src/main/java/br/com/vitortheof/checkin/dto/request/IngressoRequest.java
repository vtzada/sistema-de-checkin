package br.com.vitortheof.checkin.dto.request;

import jakarta.validation.constraints.NotNull;

public record IngressoRequest(
        @NotNull(message = "O ID do convidado é obrigatório.")
        Long convidadoId) {
}
