package br.com.vitortheof.checkin.model;

import br.com.vitortheof.checkin.model.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String nomeComprador;

    private String emailComprador;

    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataExpiracao;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<ItemPedido> itens;

    private String gatewayReferencia;


}
