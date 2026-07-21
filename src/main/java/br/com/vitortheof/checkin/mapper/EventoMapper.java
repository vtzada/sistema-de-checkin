package br.com.vitortheof.checkin.mapper;

import br.com.vitortheof.checkin.dto.request.EventoRequest;
import br.com.vitortheof.checkin.dto.response.EventoResponse;
import br.com.vitortheof.checkin.model.Evento;
import org.springframework.stereotype.Component;

@Component
public class EventoMapper {

    public static EventoResponse toEventoResponse(Evento evento) {
        return EventoResponse.builder()
                .nome(evento.getNome())
                .data(evento.getData())
                .local(evento.getLocal())
                .build();
    }

    public static Evento toEvento(EventoRequest request) {
        return Evento.builder()
                .nome(request.nome())
                .data(request.data())
                .local(request.local())
                .ativo(request.ativo())
                .build();
    }
}
