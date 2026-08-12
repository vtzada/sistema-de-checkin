package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.request.EventoRequest;
import br.com.vitortheof.checkin.dto.response.EventoResponse;
import br.com.vitortheof.checkin.exception.ResourceNotFoundException;
import br.com.vitortheof.checkin.mapper.EventoMapper;
import br.com.vitortheof.checkin.model.enums.CategoriaEvento;
import br.com.vitortheof.checkin.model.Evento;
import br.com.vitortheof.checkin.model.Usuario;
import br.com.vitortheof.checkin.repository.EventoRepository;
import br.com.vitortheof.checkin.repository.LoteRepository;
import br.com.vitortheof.checkin.utils.HtmlSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final LoteRepository loteRepository;

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

    public EventoResponse createEvento(EventoRequest request, Usuario produtor) {
        Evento evento = EventoMapper.toEvento(request);
        evento.setProdutor(produtor);
        evento.setAtivo(true);
        Evento savedEvent = eventoRepository.save(evento);

        return EventoMapper.toEventoResponse(savedEvent);
    }

    public EventoResponse updateEvento(Long id, EventoRequest request) {
        Evento eventoExistente = eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado."));

        eventoExistente.setNome(request.nome());
        eventoExistente.setData(request.data());
        eventoExistente.setLocal(request.local());
        eventoExistente.setDescricao(HtmlSanitizer.sanitizar(request.descricao()));
        eventoExistente.setCategoria(request.categoria());
        eventoExistente.setBannerUrl(request.bannerUrl());
        eventoExistente.setAtivo(request.ativo());

        Evento updatedEvent = eventoRepository.save(eventoExistente);
        return EventoMapper.toEventoResponse(updatedEvent);
    }

    public List<EventoResponse> listarMeusEventos(Long produtorId) {
        List<Evento> eventos = eventoRepository.findByProdutorId(produtorId);
        return eventos.stream()
                .map(EventoMapper::toEventoResponse)
                .toList();
    }

    public List<EventoResponse> buscarEventos(String busca, CategoriaEvento categoria) {
        String buscar = (busca == null || busca.isBlank()) ? "" : busca.trim();
        return eventoRepository.buscarEventos(buscar, categoria).stream()
                .map(EventoMapper::toEventoResponse)
                .toList();
    }

    public List<EventoResponse> listarEventosEmAlta(int limite) {
        Pageable pageable = PageRequest.of(0, limite);

        return loteRepository.findEventosMaisVendidosRaw(pageable).stream()
                .map(linha -> (Evento) linha[0])
                .map(EventoMapper::toEventoResponse)
                .toList();
    }

    public void deleteEvento(Long eventoId) {
        Evento eventoExistente = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
        eventoRepository.delete(eventoExistente);
    }
}
