package br.com.vitortheof.checkin.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PacoteRequest(
                            @NotBlank(message = "O nome do pacote é obrigatório.")
                            String nome,
                            @Min(value = 7, message = "O mínimo de jogadores deve ser pelo menos 7.")
                            int limiteJogadores,
                            @Min(value = 0, message = "O limite de vips não pode ser negativo.")
                            int limiteVips,
                            @Min(value = 0, message = "O limite de convidados não pode ser negativo.")
                            int limiteConvidados) {
}
