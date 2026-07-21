package br.com.vitortheof.checkin.dto.request;

import br.com.vitortheof.checkin.model.enums.StatusIngresso;
import br.com.vitortheof.checkin.model.enums.TipoConvidado;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record IngressoResponse(Long id, UUID codigoQR,
                               StatusIngresso status,
                               TipoConvidado tipoIngresso,
                               @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                               LocalDateTime dataHoraEntrada,
                               String nomeConvidado) {
}
