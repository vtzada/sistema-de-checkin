package br.com.vitortheof.checkin.model;

import br.com.vitortheof.checkin.model.enums.TipoConvidado;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "convidado")
public class Convidado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;

    private String email;

    private TipoConvidado tipoConvidado;

    @ManyToOne
    @JoinColumn(name = "patrocinador_id")
    private Patrocinador patrocinador;

}
