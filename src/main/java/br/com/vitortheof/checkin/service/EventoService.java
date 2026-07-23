package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.request.EventoRequest;
import br.com.vitortheof.checkin.dto.response.EventoResponse;
import br.com.vitortheof.checkin.exception.ResourceNotFoundException;
import br.com.vitortheof.checkin.mapper.EventoMapper;
import br.com.vitortheof.checkin.model.Evento;
import br.com.vitortheof.checkin.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoResponse findById(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
        return EventoMapper.toEventoResponse(evento);
    }

    public List<EventoResponse> findAll() {
        List<Evento> eventos = eventoRepository.findAll();
        return eventos.stream()
                .map(EventoMapper::toEventoResponse)
                .toList();
    }

    public EventoResponse createEvento(EventoRequest request) {
        Evento evento = EventoMapper.toEvento(request);
        Evento savedEvent = eventoRepository.save(evento);

        return EventoMapper.toEventoResponse(savedEvent);
    }
}
