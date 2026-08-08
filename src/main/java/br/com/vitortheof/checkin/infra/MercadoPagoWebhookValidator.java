package br.com.vitortheof.checkin.infra;

import com.mercadopago.exceptions.MPInvalidWebhookSignatureException;
import com.mercadopago.webhook.WebhookSignatureValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class MercadoPagoWebhookValidator {

    @Value("${mercadopago.webhook-secret:}")
    private String webhookSecret;

    public boolean isValid(String dataId, String xRequestId, String xSignature) {
        String secret = (webhookSecret == null) ? "" : webhookSecret.trim();

        if (secret.isBlank()) {
            log.error("mercadopago.webhook-secret não configurado.");
            return false;
        }

        if (dataId == null || dataId.isBlank()
                || xRequestId == null || xRequestId.isBlank()
                || xSignature == null || xSignature.isBlank()) {
            log.warn("Dados insuficientes para validar assinatura do webhook.");
            return false;
        }

        String normalizedDataId = dataId.toLowerCase(Locale.ROOT);

        try {
            WebhookSignatureValidator.validate(
                    xSignature.trim(),
                    xRequestId.trim(),
                    normalizedDataId,
                    secret
            );
            log.info("Assinatura do webhook validada com sucesso.");
            return true;
        } catch (MPInvalidWebhookSignatureException e) {
            log.warn("Assinatura do webhook do Mercado Pago inválida.");
            return false;
        }
    }
}