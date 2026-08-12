package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.response.PedidoResponse;
import br.com.vitortheof.checkin.infra.MercadoPagoWebhookValidator;
import br.com.vitortheof.checkin.infra.security.TokenService;
import br.com.vitortheof.checkin.model.enums.StatusPedido;
import br.com.vitortheof.checkin.repository.UsuarioRepository;
import br.com.vitortheof.checkin.service.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService pedidoService;

    @MockitoBean
    private MercadoPagoWebhookValidator mercadoPagoWebhookValidator;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;


    @Test
    @DisplayName("Deve retornar Status 201 ao criar pedido com sucesso")
    @WithMockUser
    void deveRetornar201AoCriarPedido() throws Exception {
        PedidoResponse response = new PedidoResponse(
                1L,
                "João Silva",
                "joao@email.com",
                new BigDecimal("200.00"),
                StatusPedido.PENDENTE,
                "https://checkout.mercadopago.com/...",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                List.of()
        );

        when(pedidoService.criarPedido(any(), any())).thenReturn(response);

        String jsonBody = """
        {
          "nomeComprador": "João Silva",
          "emailComprador": "joao@email.com",
          "itens": [{"loteId": 1, "quantidade": 2}]
        }
        """;

        mockMvc.perform(post("/pedido")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.checkoutUrl").value("https://checkout.mercadopago.com/..."));
    }
}
