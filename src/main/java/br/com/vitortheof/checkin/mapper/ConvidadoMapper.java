package br.com.vitortheof.checkin.mapper;

import br.com.vitortheof.checkin.dto.request.ConvidadoRequest;
import br.com.vitortheof.checkin.dto.response.ConvidadoResponse;
import br.com.vitortheof.checkin.model.Convidado;
import org.springframework.stereotype.Component;

@Component
public class ConvidadoMapper {

    public static ConvidadoResponse toConvidadoResponse(Convidado convidado) {
        return ConvidadoResponse.builder()
                .nomeCompleto(convidado.getNomeCompleto())
                .email(convidado.getEmail())
                .tipoConvidado(convidado.getTipoConvidado())
                .nomePatrocinador(convidado.getPatrocinador().getNome())
                .build();
    }

//    public static Convidado toConvidado(ConvidadoRequest request) {
//        return Convidado.builder()
//                .nomeCompleto(request.nomeCompleto())
//                .email(request.email())
//                .tipoConvidado(request.tipoConvidado())
//                .patrocinador(request.patrocinadorId())
//                .build();
//    }
}
