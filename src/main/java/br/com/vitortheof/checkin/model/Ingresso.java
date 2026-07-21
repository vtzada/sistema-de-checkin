package br.com.vitortheof.checkin.model;


import br.com.vitortheof.checkin.model.enums.StatusIngresso;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ingressos")
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_qr", unique = true, nullable = false)
    private UUID codigoQR;

    private StatusIngresso status;

    private LocalDateTime dataHoraEntrada;

    @ManyToOne
    @JoinColumn(name = "convidado_id", nullable = false)
    private Convidado convidado;

    @CreationTimestamp
    @Column(name = "criado_em")
    private Instant criadoEm;
}
