package br.com.vitortheof.checkin.infra;

import br.com.vitortheof.checkin.exception.BusinessException;
import br.com.vitortheof.checkin.model.ItemPedido;
import br.com.vitortheof.checkin.model.Pedido;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.order.OrderClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.order.Order;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MercadoPagoService {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${mercadopago.frontend.success-url}")
    private String successUrl;

    @Value("${mercadopago.frontend.failure-url}")
    private String failureUrl;

    @Value("${mercadopago.frontend.pending-url}")
    private String pendingUrl;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
        MercadoPagoConfig.setConnectionTimeout(5000);
        MercadoPagoConfig.setSocketTimeout(5000);
    }

    public PreferenceResult criarPreferencia(Pedido pedido) {
        try {
            PreferenceClient client = new PreferenceClient();

            var itens = pedido.getItens()
                    .stream()
                    .map(this::toPreferenceItem)
                    .toList();

            PreferenceRequest request = PreferenceRequest.builder()
                    .items(itens)
                    .payer(PreferencePayerRequest.builder()
                            .name(pedido.getNomeComprador())
                            .email(pedido.getEmailComprador())
                            .build())
                    .externalReference(pedido.getId().toString())
                    .backUrls(PreferenceBackUrlsRequest.builder()
                            .success(successUrl)
                            .failure(failureUrl)
                            .pending(pendingUrl)
                            .build())
                    .build();

            Preference preference = client.create(request);

            return new PreferenceResult(preference.getId(), preference.getInitPoint());

        } catch (MPApiException e) {
            throw new BusinessException(
                    "Erro ao criar pagamento no Mercado Pago: " + e.getApiResponse().getContent());
        } catch (MPException e) {
            throw new BusinessException("Falha ao comunicar com o Mercado Pago: " + e.getMessage());
        }
    }
    public Order buscarOrder(String orderId) {
        try {
            OrderClient client = new OrderClient();
            return client.get(orderId);
        } catch (MPApiException e) {
            throw new BusinessException(
                    "Erro ao consultar order no Mercado Pago: " + e.getApiResponse().getContent());
        } catch (MPException e) {
            throw new BusinessException("Falha ao comunicar com o Mercado Pago: " + e.getMessage());
        }
    }
    private PreferenceItemRequest toPreferenceItem(ItemPedido item) {
        return PreferenceItemRequest.builder()
                .id(item.getId() != null ? item.getId().toString() : null)
                .title(item.getLote().getTipoIngresso().getNome() + " - Lote " + item.getLote().getNumeroOrdem())
                .quantity(1)
                .unitPrice(item.getPrecoUnitario())
                .currencyId("BRL")
                .build();
    }

    public record PreferenceResult(String preferenceId, String checkoutUrl) {
    }
}