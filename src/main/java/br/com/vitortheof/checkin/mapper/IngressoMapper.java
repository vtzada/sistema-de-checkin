package br.com.vitortheof.checkin.mapper;

import br.com.vitortheof.checkin.dto.response.IngressoResponse;
import br.com.vitortheof.checkin.model.Convidado;
import br.com.vitortheof.checkin.model.Ingresso;
import br.com.vitortheof.checkin.model.ItemPedido;
import br.com.vitortheof.checkin.model.enums.TipoConvidado;
import org.springframework.stereotype.Component;

@Component
public class IngressoMapper {

    public static IngressoResponse toIngressoResponse(Ingresso ingresso) {

        String nomeTitular = null;
        TipoConvidado tipoConvidado = null;

        if (ingresso.getOrigem() instanceof Convidado convidado) {
            nomeTitular = convidado.getNomeCompleto();
            tipoConvidado = convidado.getTipoConvidado();
        } else if (ingresso.getOrigem() instanceof ItemPedido itemPedido) {
            nomeTitular = itemPedido.getPedido().getNomeComprador();
        }

        return IngressoResponse.builder()
                .id(ingresso.getId())
                .codigoQR(ingresso.getCodigoQR())
                .status(ingresso.getStatus())
                .tipoIngresso(tipoConvidado)
                .dataHoraEntrada(ingresso.getDataHoraEntrada())
                .nomeConvidado(nomeTitular)
                .build();
    }
}
