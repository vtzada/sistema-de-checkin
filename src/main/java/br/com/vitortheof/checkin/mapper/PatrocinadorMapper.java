package br.com.vitortheof.checkin.mapper;

import br.com.vitortheof.checkin.dto.response.PatrocinadorResponse;
import br.com.vitortheof.checkin.model.Patrocinador;
import org.springframework.stereotype.Component;

@Component
public class PatrocinadorMapper {

    public static PatrocinadorResponse toPatrocinadorResponse(Patrocinador patrocinador) {
        return PatrocinadorResponse.builder()
                .nome(patrocinador.getNome())
                .nomeEvento(patrocinador.getEvento().getNome())
                .nomePacote(patrocinador.getPacote().getNome())
                .build();
    }
}
