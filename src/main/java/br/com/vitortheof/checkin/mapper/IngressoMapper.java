package br.com.vitortheof.checkin.mapper;

import br.com.vitortheof.checkin.dto.response.IngressoResponse;
import br.com.vitortheof.checkin.model.Ingresso;
import org.springframework.stereotype.Component;

@Component
public class IngressoMapper {

    public static IngressoResponse toIngressoResponse(Ingresso ingresso) {
        return IngressoResponse.builder()
                .id(ingresso.getId())
                .codigoQR(ingresso.getCodigoQR())
                .status(ingresso.getStatus())
                .tipoIngresso(ingresso.getConvidado().getTipoConvidado())
                .dataHoraEntrada(ingresso.getDataHoraEntrada())
                .nomeConvidado(ingresso.getConvidado().getNomeCompleto())
                .build();
    }
}
