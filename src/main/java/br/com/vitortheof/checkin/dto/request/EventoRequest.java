package br.com.vitortheof.checkin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EventoRequest(@NotBlank(message = "O nome do evento é obrigatório")
                            @Size(min = 3, max = 100, message = "O nome do evento deve ter entre 3 e 100 caracteres.")
                            String nome,
                            @NotNull(message = "A data do evento é obrigatória.")
                            LocalDateTime data,
                            @NotBlank(message = "O local do evento é obrigatório.")
                            String local,
                            @Size(max = 10000, message = "A descrição não pode exceder 10.000 caracteres")
                            String descricao,
                            boolean ativo) {
}
