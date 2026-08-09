package br.com.vitortheof.checkin.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "item_pedido")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("ITEM_PEDIDO")
@Entity
public class ItemPedido extends OrigemIngresso {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;

    @Column(name = "preco_unitario", nullable = false)
    private BigDecimal precoUnitario;
}