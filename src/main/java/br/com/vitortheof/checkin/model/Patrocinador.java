package br.com.vitortheof.checkin.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patrocinador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "pacote_id")
    private Pacote pacote;

    @OneToMany(mappedBy = "patrocinador")
    private List<Convidado> convidados;

    private boolean ativo;

    @CreationTimestamp
    private Instant criadoEm;
}
