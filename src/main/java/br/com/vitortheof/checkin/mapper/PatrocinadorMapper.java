package br.com.vitortheof.checkin.mapper;

import br.com.vitortheof.checkin.dto.response.PatrocinadorResponse;
import br.com.vitortheof.checkin.model.Patrocinador;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class PatrocinadorMapper {

    public static PatrocinadorResponse toPatrocinadorResponse(Patrocinador patrocinador) {
        return PatrocinadorResponse.builder()
                .nome(patrocinador.getNome())
                .nomeEvento(patrocinador.getEvento().getNome())
                .nomePacote(patrocinador.getPacote().getNome())
                .convidados(patrocinador.getConvidados() != null ?
                        patrocinador.getConvidados().stream()
                        .map(c -> new PatrocinadorResponse.ConvidadoResumo(c.getId(), c.getNomeCompleto(), c.getTipoConvidado()))
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }
}
