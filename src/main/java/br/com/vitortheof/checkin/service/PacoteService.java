package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.request.PacoteRequest;
import br.com.vitortheof.checkin.dto.response.PacoteResponse;
import br.com.vitortheof.checkin.mapper.PacoteMapper;
import br.com.vitortheof.checkin.model.Pacote;
import br.com.vitortheof.checkin.repository.PacoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PacoteService {

    private final PacoteRepository pacoteRepository;

    public PacoteResponse findById(Long id) {
        Pacote pacote = pacoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pacote não encontrado"));
        return PacoteMapper.toPacoteResponse(pacote);
    }

    public List<PacoteResponse> findAll() {
        List<Pacote> pacotes = pacoteRepository.findAll();
        return pacotes.stream()
                .map(PacoteMapper::toPacoteResponse)
                .toList();
    }

    public PacoteResponse createPacote(PacoteRequest request) {
        Pacote pacote = PacoteMapper.toPacote(request);
        Pacote savedPacote = pacoteRepository.save(pacote);

        return PacoteMapper.toPacoteResponse(savedPacote);
    }

}
