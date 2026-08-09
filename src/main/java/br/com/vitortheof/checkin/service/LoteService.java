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

import java.util.List;

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
        return loteRepository.findByTipoIngressoEventoId(eventoId).stream()
                .map(LoteMapper::toLoteResponse)
                .toList();
    }

    private Lote buscarOuFalhar(Long id){
        return loteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Esse lote não foi encontrado."));
    }
}
