package br.com.vitortheof.checkin.repository;

import br.com.vitortheof.checkin.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByGatewayReferencia(String gateway);

    // usado por um job/scheduler futuro pra expirar pedidos pendentes.
    // q passaram do prazo sem pagamento confirmado.
    List<Pedido> findByStatusPedidoAndDataExpiracaoBefore(
            br.com.vitortheof.checkin.model.enums.StatusPedido status,
            LocalDateTime agora
    );
}
