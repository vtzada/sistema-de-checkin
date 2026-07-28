package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.request.PacoteRequest;
import br.com.vitortheof.checkin.dto.response.PacoteResponse;
import br.com.vitortheof.checkin.exception.ResourceNotFoundException;
import br.com.vitortheof.checkin.mapper.PacoteMapper;
import br.com.vitortheof.checkin.model.Evento;
import br.com.vitortheof.checkin.model.Pacote;
import br.com.vitortheof.checkin.repository.EventoRepository;
import br.com.vitortheof.checkin.repository.PacoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacoteService {

    private final PacoteRepository pacoteRepository;
    private final EventoRepository eventoRepository;

    public PacoteResponse findById(Long id) {
        Pacote pacote = pacoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pacote não encontrado"));
        return PacoteMapper.toPacoteResponse(pacote);
    }

    public List<PacoteResponse> findByEventoId(Long eventoId) {
        List<Pacote> pacotes = pacoteRepository.findByEventoId(eventoId);
        return pacotes.stream().map(PacoteMapper::toPacoteResponse).toList();
    }

    public PacoteResponse createPacote(Long eventoId, PacoteRequest request) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        Pacote pacote = PacoteMapper.toPacote(request);

        pacote.setEvento(evento);

        Pacote savedPacote = pacoteRepository.save(pacote);

        return PacoteMapper.toPacoteResponse(savedPacote);
    }

}
