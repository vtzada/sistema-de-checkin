package br.com.vitortheof.checkin.dto.response;

import br.com.vitortheof.checkin.model.enums.TipoConvidado;
import lombok.Builder;

import java.util.List;

@Builder
public record PatrocinadorResponse(Long id, String nome, String nomeEvento, String nomePacote) {

public record ConvidadoResumo(Long id, String nomeCompleto, TipoConvidado tipo) {}
}

