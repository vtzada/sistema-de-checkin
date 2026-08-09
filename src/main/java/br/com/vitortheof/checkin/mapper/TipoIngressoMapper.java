package br.com.vitortheof.checkin.mapper;

import br.com.vitortheof.checkin.dto.request.TipoIngressoRequest;
import br.com.vitortheof.checkin.dto.response.LoteResponse;
import br.com.vitortheof.checkin.dto.response.TipoIngressoResponse;
import br.com.vitortheof.checkin.model.TipoIngresso;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TipoIngressoMapper {

    public static TipoIngressoResponse toTipoIngressoResponse(TipoIngresso tipoIngresso) {
        List<LoteResponse> lotes = tipoIngresso.getLotes()
                == null ? Collections.emptyList() : tipoIngresso.getLotes().stream()
                .map(LoteMapper::toLoteResponse)
                .toList();

        return TipoIngressoResponse.builder()
                .id(tipoIngresso.getId())
                .nome(tipoIngresso.getNome())
                .descricao(tipoIngresso.getDescricao())
                .lotes(lotes)
                .build();
    }
    public static TipoIngresso toTipoIngresso(TipoIngressoRequest request) {
        return TipoIngresso.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();
    }
}
