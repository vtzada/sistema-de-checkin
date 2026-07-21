package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.request.PatrocinadorRequest;
import br.com.vitortheof.checkin.dto.response.PatrocinadorResponse;
import br.com.vitortheof.checkin.mapper.PatrocinadorMapper;
import br.com.vitortheof.checkin.model.Evento;
import br.com.vitortheof.checkin.model.Pacote;
import br.com.vitortheof.checkin.model.Patrocinador;
import br.com.vitortheof.checkin.repository.EventoRepository;
import br.com.vitortheof.checkin.repository.PacoteRepository;
import br.com.vitortheof.checkin.repository.PatrocinadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatrocinadorService {

    private final PatrocinadorRepository patrocinadorRepository;
    private final EventoRepository eventoRepository;
    private final PacoteRepository pacoteRepository;

    public PatrocinadorResponse createPatrocinador(PatrocinadorRequest request) {
        Evento evento = eventoRepository.findById(request.eventoId())
                .orElseThrow(() -> new RuntimeException("Evento não encontrado!"));

        Pacote pacote = pacoteRepository.findById(request.pacoteId())
                .orElseThrow(() -> new RuntimeException("Pacote não encontrado!"));

        Patrocinador patrocinador = Patrocinador.builder()
                .nome(request.nome())
                .ativo(request.ativo())
                .evento(evento)
                .pacote(pacote)
                .build();
        Patrocinador savedPatrocinador = patrocinadorRepository.save(patrocinador);

        return PatrocinadorMapper.toPatrocinadorResponse(savedPatrocinador);
    }

    public PatrocinadorResponse findById(Long id) {
        Patrocinador patrocinador = patrocinadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patrocinador não encontrado;"));

        return PatrocinadorMapper.toPatrocinadorResponse(patrocinador);
    }

    public List<PatrocinadorResponse> findAll() {
        List<Patrocinador> patrocinadores = patrocinadorRepository.findAll();
        return patrocinadores.stream()
                .map(PatrocinadorMapper::toPatrocinadorResponse)
                .toList();
    }
}
