package br.com.vitortheof.checkin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TipoIngressoRequest(
        @NotBlank(message = "O nome do tipo de ingresso é obrigatório")
        String nome,
        @Size(max = 2000, message = "A descrição não pode exceder 2000 caracteres.")
        String descricao
) {
}
