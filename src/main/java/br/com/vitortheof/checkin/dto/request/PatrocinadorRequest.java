package br.com.vitortheof.checkin.dto.request;

import br.com.vitortheof.checkin.model.Evento;
import br.com.vitortheof.checkin.model.Pacote;

public record PatrocinadorRequest(String nome, Long eventoId, Long pacoteId, boolean ativo) {
}
