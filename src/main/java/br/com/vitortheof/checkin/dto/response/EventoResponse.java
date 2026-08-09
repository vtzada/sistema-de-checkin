package br.com.vitortheof.checkin.dto.response;

import br.com.vitortheof.checkin.model.enums.CategoriaEvento;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventoResponse(Long id, String nome, LocalDateTime data, String descricao, String bannerUrl, String local,
                             CategoriaEvento categoria) {
}
