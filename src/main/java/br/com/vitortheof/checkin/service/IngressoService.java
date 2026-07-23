package br.com.vitortheof.checkin.service;

import br.com.vitortheof.checkin.dto.response.IngressoResponse;
import br.com.vitortheof.checkin.exception.BusinessException;
import br.com.vitortheof.checkin.exception.ResourceNotFoundException;
import br.com.vitortheof.checkin.mapper.IngressoMapper;
import br.com.vitortheof.checkin.model.Convidado;
import br.com.vitortheof.checkin.model.Ingresso;
import br.com.vitortheof.checkin.model.enums.StatusIngresso;
import br.com.vitortheof.checkin.repository.ConvidadoRepository;
import br.com.vitortheof.checkin.repository.IngressoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngressoService {

    private final IngressoRepository ingressoRepository;

    @Transactional
    public Ingresso gerarIngresso(Convidado convidado) {

        Ingresso ingresso = Ingresso.builder().codigoQR(UUID.randomUUID()).status(StatusIngresso.VALIDO).convidado(convidado).build();

        return ingressoRepository.save(ingresso);
    }

    @Transactional
    public IngressoResponse realizarCheckin(UUID codigoQR) {
        Ingresso ingresso = ingressoRepository.findByCodigoQR(codigoQR)
                .orElseThrow(() -> new ResourceNotFoundException("Ingresso não encontrado. Verifique o QR Code."));

        if (ingresso.getStatus() == StatusIngresso.UTILIZADO) {
            throw new BusinessException("Esse ingresso já foi utilizado!");
        }

        if (ingresso.getStatus() != StatusIngresso.VALIDO) {
            throw new BusinessException("Esse ingresso não é valido!");
        }

        ingresso.setStatus(StatusIngresso.UTILIZADO);
        ingresso.setDataHoraEntrada(LocalDateTime.now());

        Ingresso salvo = ingressoRepository.save(ingresso);

        return IngressoMapper.toIngressoResponse(salvo);
    }

    public List<IngressoResponse> findAll() {
        return ingressoRepository.findAll().stream().map(IngressoMapper::toIngressoResponse).toList();
    }



}
