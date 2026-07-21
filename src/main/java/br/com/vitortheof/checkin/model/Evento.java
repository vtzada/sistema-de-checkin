package br.com.vitortheof.checkin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private LocalDateTime data;

    @Column(nullable = false)
    private String local;

    private boolean ativo;

    @OneToMany(mappedBy = "evento")
    private List<Patrocinador> patrocinadores;

    @CreationTimestamp
    @Column(name = "criado_em")
    private Instant criadoEm;



}
