package br.com.vitortheof.checkin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @Column(columnDefinition = "TEXT")
    @Size(max = 10000, message = "A descrição não pode exceder 10.000 caracteres")
    private String descricao;

    private boolean ativo;

    @OneToMany(mappedBy = "evento")
    private List<Patrocinador> patrocinadores;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario produtor;

    @CreationTimestamp
    @Column(name = "criado_em")
    private Instant criadoEm;



}
