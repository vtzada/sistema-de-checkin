package br.com.vitortheof.checkin.controller;

import br.com.vitortheof.checkin.dto.request.PedidoRequest;
import br.com.vitortheof.checkin.dto.response.PedidoResponse;
import br.com.vitortheof.checkin.infra.MercadoPagoWebhookValidator;
import br.com.vitortheof.checkin.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final MercadoPagoWebhookValidator webhookValidator;

    @PostMapping("/pedido")
    public ResponseEntity<PedidoResponse> criarPedido(
            @Valid @RequestBody PedidoRequest request
    ) {
        PedidoResponse response = pedidoService.criarPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pedido/{id}")
    @PreAuthorize("hasRole('PRODUTOR')")
    public ResponseEntity<PedidoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.findById(id));
    }

    @PostMapping("/webhook/mercadopago/notificacoes")
    public ResponseEntity<Void> receberNotificacao(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "data.id", required = false) String dataIdQuery,
            @RequestHeader(value = "x-signature", required = false) String xSignature,
            @RequestHeader(value = "x-request-id", required = false) String xRequestId,
            @RequestBody(required = false) Map<String, Object> payload
    ) {
        Timestamp timeAtual = new Timestamp(System.currentTimeMillis());
        log.info("Tempo iniciado: {}", timeAtual);

        String xSignatureTrim = (xSignature == null) ? null : xSignature.trim();
        String xRequestIdTrim = (xRequestId == null) ? null : xRequestId.trim();

        // Normaliza data.id para minúsculas (ex.: ORD... -> ord...), como recomendado para IDs alfanuméricos
        String dataIdNormalized = (dataIdQuery == null) ? null : dataIdQuery.trim().toLowerCase(Locale.ROOT);

        log.info("========================================");
        log.info("WEBHOOK MERCADO PAGO RECEBIDO");
        log.info("type: {}", type);
        log.info("data.id da query: {}", dataIdQuery);
        log.info("data.id normalizado: {}", dataIdNormalized);
        log.info("x-request-id: {}", xRequestIdTrim);
        log.info("x-signature: {}", xSignatureTrim);
        log.info("payload: {}", payload);
        log.info("========================================");

        if (xSignatureTrim == null
                || xRequestIdTrim == null
                || dataIdNormalized == null
                || dataIdNormalized.isBlank()) {
            log.warn("Webhook rejeitado: dados necessários para validação ausentes.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!webhookValidator.isValid(
                dataIdNormalized,
                xRequestIdTrim,
                xSignatureTrim
        )) {
            Timestamp timeFinal = new Timestamp(System.currentTimeMillis());
            log.info("Tempo final: {}", timeFinal);
            log.warn("Webhook rejeitado: assinatura inválida.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Assinatura do webhook validada com sucesso.");

        if (payload == null) {
            log.warn("Webhook recebido sem payload (assinatura OK, mas sem corpo).");
            return ResponseEntity.ok().build();
        }

        Object dataObject = payload.get("data");
        String dataIdBody = null;

        if (dataObject instanceof Map<?, ?> dataMap) {
            Object idObject = dataMap.get("id");
            if (idObject != null) dataIdBody = String.valueOf(idObject);
        }

        log.info("data.id do body: {}", dataIdBody);

        String action = (payload.get("action") != null) ? String.valueOf(payload.get("action")) : null;

        if ("order".equals(type) && "order.processed".equals(action)) {

            String externalReference = null;

            if (dataObject instanceof Map<?, ?> dataMap) {
                Object externalReferenceObject = dataMap.get("external_reference");
                if (externalReferenceObject != null) externalReference = String.valueOf(externalReferenceObject);
            }

            if (externalReference == null || externalReference.isBlank()) {
                log.warn("Order recebida sem external_reference. dataId={}", dataIdBody);
                return ResponseEntity.ok().build();
            }

            try {
                Long pedidoId = Long.valueOf(externalReference);
                log.info("Pagamento confirmado pelo Mercado Pago. pedidoId={}, orderId={}", pedidoId, dataIdBody);
                pedidoService.processarPedidoPago(pedidoId, dataIdBody);
            } catch (NumberFormatException e) {
                log.error("external_reference inválida: {}", externalReference, e);
            }
        }
        return ResponseEntity.ok().build();
    }
}