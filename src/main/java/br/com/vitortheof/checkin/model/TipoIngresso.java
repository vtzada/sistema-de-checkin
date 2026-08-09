package br.com.vitortheof.checkin.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "tipo_ingresso")
public class TipoIngresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id",  nullable = false)
    private Evento evento;
    private String nome;
    private String descricao;

    @OneToMany(mappedBy = "tipoIngresso")
    private List<Lote> lotes;
}
