package br.com.vitortheof.checkin.scheduler;

import br.com.vitortheof.checkin.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoExpirationScheduler {

    private final PedidoService pedidoService;

    @Scheduled(fixedRate = 60_000)
    public void expirarPedidoVencido() {
        try {
            pedidoService.expirarPedidosPendentes();
        } catch (Exception e) {

            log.error("erro ao expirar pedidos pendentes", e);
        }
    }
}
