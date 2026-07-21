package br.com.vitortheof.checkin.dto.request;

import br.com.vitortheof.checkin.model.enums.TipoConvidado;

public record ConvidadoRequest(String nomeCompleto, String email, TipoConvidado tipoConvidado, Long patrocinadorId) {
}
