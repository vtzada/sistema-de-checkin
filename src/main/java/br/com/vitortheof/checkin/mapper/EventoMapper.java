package br.com.vitortheof.checkin.mapper;

import br.com.vitortheof.checkin.dto.request.EventoRequest;
import br.com.vitortheof.checkin.dto.response.EventoResponse;
import br.com.vitortheof.checkin.model.Evento;
import br.com.vitortheof.checkin.utils.HtmlSanitizer;
import org.springframework.stereotype.Component;

@Component
public class EventoMapper {

    public static EventoResponse toEventoResponse(Evento evento) {
        return EventoResponse.builder()
                .id(evento.getId())
                .nome(evento.getNome())
                .data(evento.getData())
                .local(evento.getLocal())
                .descricao(evento.getDescricao())
                .build();
    }

    public static Evento toEvento(EventoRequest request) {
        return Evento.builder()
                .nome(request.nome())
                .data(request.data())
                .local(request.local())
                .descricao(HtmlSanitizer.sanitizar(request.descricao()))
                .ativo(request.ativo())
                .build();
    }
}
