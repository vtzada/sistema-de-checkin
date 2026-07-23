package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.request.ConvidadoRequest;
import br.com.vitortheof.checkin.dto.response.ConvidadoResponse;
import br.com.vitortheof.checkin.exception.BusinessException;
import br.com.vitortheof.checkin.exception.ResourceAlreadyExistsException;
import br.com.vitortheof.checkin.exception.ResourceNotFoundException;
import br.com.vitortheof.checkin.mapper.ConvidadoMapper;
import br.com.vitortheof.checkin.model.Convidado;
import br.com.vitortheof.checkin.model.Pacote;
import br.com.vitortheof.checkin.model.Patrocinador;
import br.com.vitortheof.checkin.model.enums.StatusConfirmacao;
import br.com.vitortheof.checkin.model.enums.TipoConvidado;
import br.com.vitortheof.checkin.repository.ConvidadoRepository;
import br.com.vitortheof.checkin.repository.PatrocinadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConvidadoService {

    private final ConvidadoRepository convidadoRepository;
    private final PatrocinadorRepository patrocinadorRepository;
    private final IngressoService ingressoService;


    @Transactional
    public ConvidadoResponse createConvidado(ConvidadoRequest request) {

        if (convidadoRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Este e-mail já está cadastrado.");
        }

        Patrocinador patrocinador = patrocinadorRepository.findById(request.patrocinadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Patrocinador não encontrado."));

        validarLimite(patrocinador, request.tipoConvidado());

        String token = UUID.randomUUID().toString();

        Convidado convidado = Convidado.builder()
                .nomeCompleto(request.nomeCompleto())
                .email(request.email())
                .tipoConvidado(request.tipoConvidado())
                .patrocinador(patrocinador)
                .statusConfirmacao(StatusConfirmacao.PENDENTE)
                .tokenConfirmacao(token)
                .build();

        Convidado savedConvidado = convidadoRepository.save(convidado);

        return ConvidadoMapper.toConvidadoResponse(savedConvidado);
    }

    @Transactional
    public void confirmarPresenca(String token) {
        Convidado convidado = convidadoRepository.findByTokenConfirmacao(token)
                .orElseThrow(() -> new ResourceNotFoundException("Link de confirmação inválido ou expirado."));

        if (convidado.getStatusConfirmacao().equals(StatusConfirmacao.CONFIRMADO)) {
            throw new BusinessException("Esta presença já foi confirmada anteriormente.");
        }

        convidado.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
        convidado.setTokenConfirmacao(null);

        convidadoRepository.save(convidado);

        ingressoService.gerarIngresso(convidado);
    }

    public ConvidadoResponse findById(Long id) {
        Convidado convidado = convidadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convidado não encontrado."));
        return ConvidadoMapper.toConvidadoResponse(convidado);
    }

    public List<ConvidadoResponse> findAll() {
        List<Convidado> convidados = convidadoRepository.findAll();
        return convidados.stream()
                .map(ConvidadoMapper::toConvidadoResponse)
                .toList();
    }

    private void validarLimite(Patrocinador patrocinador, TipoConvidado tipo) {

        long qtdAtual = convidadoRepository.countByPatrocinadorIdAndTipoConvidado(patrocinador.getId(), tipo);

        int limitePermitido = obterLimitePorTipo(patrocinador.getPacote(), tipo);

        if (qtdAtual >= limitePermitido) {
            throw new BusinessException(
                    String.format("Limite de vagas excedido! O pacote permite apenas %d ingressos para %s.", limitePermitido, tipo)
            );
        }
    }

    private int obterLimitePorTipo(Pacote pacote, TipoConvidado tipo) {
        return switch (tipo) {
            case VIP -> pacote.getLimiteVips();
            case JOGADOR -> pacote.getLimiteJogadores();
            case CONVIDADO_COMUM -> pacote.getLimiteConvidados();
        };
    }

}
