package br.com.vitortheof.checkin.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pacote")
public class Pacote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private int limiteJogadores;

    private int limiteVips;

    private int limiteConvidados;

    @OneToMany(mappedBy = "pacote")
    private List<Patrocinador> patrocinadores;

    @CreationTimestamp
    @Column(name = "criado_em")
    private Instant criadoEm;

}
