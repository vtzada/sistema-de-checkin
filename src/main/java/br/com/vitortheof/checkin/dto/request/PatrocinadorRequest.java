package br.com.vitortheof.checkin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatrocinadorRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        @NotNull(message = "Pacote é obrigatório")
        Long pacoteId,
        boolean ativo) {
}
