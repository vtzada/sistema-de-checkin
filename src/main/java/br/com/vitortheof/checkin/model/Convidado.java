package br.com.vitortheof.checkin.model;

import br.com.vitortheof.checkin.model.enums.StatusConfirmacao;
import br.com.vitortheof.checkin.model.enums.TipoConvidado;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("CONVIDADO")
@Table(name = "convidado")
public class Convidado extends OrigemIngresso {

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConvidado tipoConvidado;

    @ManyToOne
    @JoinColumn(name = "patrocinador_id", nullable = false)
    private Patrocinador patrocinador;

    @Enumerated(EnumType.STRING)
    private StatusConfirmacao statusConfirmacao;

    private String tokenConfirmacao;

}
