package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.request.ConvidadoRequest;
import br.com.vitortheof.checkin.dto.response.ConvidadoResponse;
import br.com.vitortheof.checkin.mapper.ConvidadoMapper;
import br.com.vitortheof.checkin.model.Convidado;
import br.com.vitortheof.checkin.model.Patrocinador;
import br.com.vitortheof.checkin.repository.ConvidadoRepository;
import br.com.vitortheof.checkin.repository.PatrocinadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConvidadoService {

    private final ConvidadoRepository convidadoRepository;
    private final PatrocinadorRepository patrocinadorRepository;

    public ConvidadoResponse createConvidado(ConvidadoRequest request) {
        Patrocinador patrocinador = patrocinadorRepository.findById(request.patrocinadorId())
                .orElseThrow(() -> new RuntimeException("Patrocinador não encontrado."));

        Convidado convidado = Convidado.builder()
                .nomeCompleto(request.nomeCompleto())
                .email(request.email())
                .tipoConvidado(request.tipoConvidado())
                .patrocinador(patrocinador)
                .build();

        Convidado savedConvidado = convidadoRepository.save(convidado);
        return ConvidadoMapper.toConvidadoResponse(savedConvidado);
    }

    public ConvidadoResponse findById(Long id) {
        Convidado convidado = convidadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Convidado não encontrado."));
        return ConvidadoMapper.toConvidadoResponse(convidado);
    }

    public List<ConvidadoResponse> findAll() {
        List<Convidado> convidados = convidadoRepository.findAll();
        return convidados.stream()
                .map(ConvidadoMapper::toConvidadoResponse)
                .toList();
    }

}
