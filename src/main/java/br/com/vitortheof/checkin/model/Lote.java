package br.com.vitortheof.checkin.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "lote")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_ingresso_id",  nullable = false)
    private TipoIngresso tipoIngresso;

    private int numeroOrdem;
    private BigDecimal preco;
    private int qtdTotal;
    private int qtdVendida;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    public boolean isDisponivel() {
        LocalDateTime agora = LocalDateTime.now();

        boolean dentroDoPeriodo = (dataInicio == null || !agora.isBefore(dataInicio))
                && (dataFim == null || !agora.isAfter(dataFim));

        boolean temEstoque = qtdVendida < qtdTotal;

        return dentroDoPeriodo && temEstoque;
    }
}
