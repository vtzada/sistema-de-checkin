package br.com.vitortheof.checkin.infra.security;

import br.com.vitortheof.checkin.model.Usuario;
import br.com.vitortheof.checkin.model.enums.UserRole;
import br.com.vitortheof.checkin.repository.ConvidadoRepository;
import br.com.vitortheof.checkin.repository.EventoRepository;
import br.com.vitortheof.checkin.repository.IngressoRepository;
import br.com.vitortheof.checkin.repository.PatrocinadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("eventSecurity")
public class EventSecurityService {

    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private PatrocinadorRepository patrocinadorRepository;
    @Autowired
    private ConvidadoRepository convidadoRepository;
    @Autowired
    private IngressoRepository ingressoRepository;


    // ========= PERMISSÕES DO EVENTO

    public boolean isOwnerOrAdminOfEvento(Long eventoId, Usuario usuarioLogado) {
        if (usuarioLogado.getRole() == UserRole.ADMIN) return true;

        return eventoRepository.findById(eventoId)
                .map(evento -> evento.getProdutor().getId().equals(usuarioLogado.getId()))
                .orElse(false);
    }

    public boolean isOwnerOrAdminOfPatrocinador(Long patrocinadorId, Usuario usuarioLogado) {
        if (usuarioLogado.getRole() == UserRole.ADMIN) return true;

        return patrocinadorRepository.findById(patrocinadorId)
                .map(patrocinador -> patrocinador.getEvento().getProdutor().getId().equals(usuarioLogado.getId()))
                .orElse(false);
    }

    public boolean isOwnerOrAdminOfConvidado(Long convidadoId, Usuario usuarioLogado) {
        if (usuarioLogado.getRole() == UserRole.ADMIN) return true;

        return convidadoRepository.findById(convidadoId)
                .map(convidado -> convidado.getPatrocinador().getEvento().getProdutor().getId().equals(usuarioLogado.getId()))
                .orElse(false);
    }

    public boolean canCheckIn(Long convidadoId, Usuario usuarioLogado) {
        if (usuarioLogado.getRole() == UserRole.ADMIN || usuarioLogado.getRole() == UserRole.PORTARIA) {
            return true;
        }
        // Se for produtor, só pode dar check-in nos convidados do evento dele
        return isOwnerOrAdminOfConvidado(convidadoId, usuarioLogado);
    }

    public boolean canCheckInByQr(UUID codigoQR, Usuario usuarioLogado) {
        // admin e portaria liberado dar checkin em tds os eventos.
        if (usuarioLogado.getRole() == UserRole.ADMIN || usuarioLogado.getRole() == UserRole.PORTARIA) {
            return true;
        }

        // se for produtor, so podera dar checkin se o ingresso pertencer a um evento dele.
        return ingressoRepository.findByCodigoQR(codigoQR)
                .map(ingresso ->
                        ingresso.getConvidado()
                                .getPatrocinador()
                                .getEvento()
                                .getProdutor()
                                .getId()
                                .equals(usuarioLogado.getId()))
                .orElse(false); // se o qr nao existir, nega o acesso.
    }

}
