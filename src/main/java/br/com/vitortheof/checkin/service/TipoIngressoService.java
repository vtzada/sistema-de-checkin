package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.request.TipoIngressoRequest;
import br.com.vitortheof.checkin.dto.response.TipoIngressoResponse;
import br.com.vitortheof.checkin.exception.ResourceNotFoundException;
import br.com.vitortheof.checkin.mapper.TipoIngressoMapper;
import br.com.vitortheof.checkin.model.Evento;
import br.com.vitortheof.checkin.model.Pedido;
import br.com.vitortheof.checkin.model.TipoIngresso;
import br.com.vitortheof.checkin.repository.EventoRepository;
import br.com.vitortheof.checkin.repository.TipoIngressoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoIngressoService {

    private final TipoIngressoRepository tipoIngressoRepository;
    private final PedidoService pedidoService;
    private final EventoRepository eventoRepository;

    public TipoIngressoResponse createTipoIngresso(Long eventoId, TipoIngressoRequest request) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        TipoIngresso tipoIngresso = TipoIngressoMapper.toTipoIngresso(request);
        tipoIngresso.setEvento(evento);

        TipoIngresso salvo = tipoIngressoRepository.save(tipoIngresso);
        return TipoIngressoMapper.toTipoIngressoResponse(salvo);
    }

    public TipoIngressoResponse findById(Long id) {
        TipoIngresso tipoIngresso = buscarOuFalhar(id);
        return TipoIngressoMapper.toTipoIngressoResponse(tipoIngresso);
    }

    public List<TipoIngressoResponse> findByEventoId(Long eventoId) {
        return tipoIngressoRepository.findByEventoId(eventoId).stream()
                .map(TipoIngressoMapper::toTipoIngressoResponse)
                .toList();
    }

    public TipoIngresso buscarOuFalhar(Long id) {
        return tipoIngressoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de ingresso não encontrado"));
    }
}
