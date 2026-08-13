package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.request.LoteRequest;
import br.com.vitortheof.checkin.dto.response.LoteResponse;
import br.com.vitortheof.checkin.exception.BusinessException;
import br.com.vitortheof.checkin.exception.ResourceNotFoundException;
import br.com.vitortheof.checkin.mapper.LoteMapper;
import br.com.vitortheof.checkin.model.Lote;
import br.com.vitortheof.checkin.model.TipoIngresso;
import br.com.vitortheof.checkin.repository.LoteRepository;
import br.com.vitortheof.checkin.repository.TipoIngressoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
    private final TipoIngressoRepository tipoIngressoRepository;

    public LoteResponse createLote(Long tipoIngressoId, LoteRequest request) {
        TipoIngresso tipoIngresso = tipoIngressoRepository.findById(tipoIngressoId)
                .orElseThrow(() -> new ResourceNotFoundException("Esse tipo de ingresso não existe."));

        if (request.dataInicio() != null && request.dataFim() != null
        && request.dataInicio().isAfter(request.dataFim())) {
            throw new BusinessException("A data de inicio de lote não pode ser depois da data de fim.");
        }
        Lote lote = LoteMapper.toLote(request);
        lote.setTipoIngresso(tipoIngresso);

        Lote salvo = loteRepository.save(lote);
        return LoteMapper.toLoteResponse(salvo);
    }

    public LoteResponse findById(Long loteId) {
        Lote lote = buscarOuFalhar(loteId);
        return LoteMapper.toLoteResponse(lote);
    }

    public List<LoteResponse> findByTipoIngressoId(Long tipoIngressoId) {
        return loteRepository.findByTipoIngressoId(tipoIngressoId).stream()
                .map(LoteMapper::toLoteResponse)
                .toList();
    }

    public List<LoteResponse> findDisponiveisByEventoId(Long eventoId) {
        List<Lote> lotes = loteRepository.findByTipoIngressoEventoId(eventoId);

        return lotes.stream()
                .filter(Lote::isDisponivel)
                .collect(Collectors.groupingBy(
                        lote -> lote.getTipoIngresso().getId(),
                        Collectors.minBy(Comparator.comparingInt(Lote::getNumeroOrdem))
                ))
                .values().stream()
                .flatMap(Optional::stream)
                .map(LoteMapper::toLoteResponse)
                .toList();
    }

    private Lote buscarOuFalhar(Long id){
        return loteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Esse lote não foi encontrado."));
    }
}
