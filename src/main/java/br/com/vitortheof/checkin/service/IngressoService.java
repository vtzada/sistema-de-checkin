package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.request.IngressoResponse;
import br.com.vitortheof.checkin.dto.response.IngressoRequest;
import br.com.vitortheof.checkin.mapper.IngressoMapper;
import br.com.vitortheof.checkin.model.Convidado;
import br.com.vitortheof.checkin.model.Ingresso;
import br.com.vitortheof.checkin.model.enums.StatusIngresso;
import br.com.vitortheof.checkin.repository.ConvidadoRepository;
import br.com.vitortheof.checkin.repository.IngressoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngressoService {

    private final IngressoRepository ingressoRepository;
    private final ConvidadoRepository convidadoRepository;

    public IngressoResponse createIngresso(IngressoRequest request) {

        Convidado convidado = convidadoRepository.findById(request.convidadoId())
                .orElseThrow(() -> new RuntimeException("Convidado não encontrado."));

        Ingresso ingresso = Ingresso.builder()
                .codigoQR(UUID.randomUUID())
                .status(StatusIngresso.VALIDO)
                .convidado(convidado)
                .build();

        Ingresso savedIngresso = ingressoRepository.save(ingresso);

        return IngressoMapper.toIngressoResponse(savedIngresso);
    }

    public List<IngressoResponse> findAll(){
        return ingressoRepository.findAll()
                .stream()
                .map(IngressoMapper::toIngressoResponse)
                .toList();
    }

}
