package br.com.vitortheof.checkin.dto.request;

import br.com.vitortheof.checkin.model.enums.TipoConvidado;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConvidadoRequest(
        @NotBlank(message = "O nome completo é obrigatório.")
        String nomeCompleto,
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O formato do e-mail informado é inválido.")
        String email,
        @NotNull(message = "O tipo de convidado é obrigatório.")
        TipoConvidado tipoConvidado) {
}
