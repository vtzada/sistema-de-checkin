package br.com.vitortheof.checkin.dto.response;

import br.com.vitortheof.checkin.model.enums.TipoConvidado;
import lombok.Builder;

@Builder
public record ConvidadoResponse(String nomeCompleto, String email, TipoConvidado tipoConvidado, String nomePatrocinador) {
}
