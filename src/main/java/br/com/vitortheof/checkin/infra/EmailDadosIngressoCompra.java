package br.com.vitortheof.checkin.infra;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EmailDadosIngressoCompra(
        String emailComprador,
        String nomeComprador,
        String nomeEvento,
        LocalDateTime dataEvento,
        String enderecoEvento,
        String nomeTipoIngresso,
        BigDecimal precoPago,
        String codigoQR
) {
}
