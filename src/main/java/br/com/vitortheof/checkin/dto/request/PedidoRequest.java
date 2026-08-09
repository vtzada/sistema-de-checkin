package br.com.vitortheof.checkin.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PedidoRequest(@NotBlank(message = "O nome do comprador é obrigatório")
                            String nomeComprador,
                            @NotBlank(message = "O email do comprador é obrigatório")
                            @Email(message = "E-mail inválido")
                            String emailComprador,
                            @NotEmpty(message = "O pedido precisa conter pelo menos um item")
                            @Valid
                            List<ItemPedidoRequest> itens) {
}
