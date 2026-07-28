package br.com.vitortheof.checkin.infra.security;

import br.com.vitortheof.checkin.model.Usuario;
import br.com.vitortheof.checkin.model.enums.UserRole;
import br.com.vitortheof.checkin.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("eventSecurity")
@RequiredArgsConstructor // Injeção de dependência via construtor (mais seguro que @Autowired em campo)
public class EventSecurityService {

    private final EventoRepository eventoRepository;
    private final PatrocinadorRepository patrocinadorRepository;
    private final ConvidadoRepository convidadoRepository;
    private final IngressoRepository ingressoRepository;
    private final PacoteRepository pacoteRepository;

    // ========= PERMISSÕES DO EVENTO

    public boolean isOwnerOrAdminOfEvento(Long eventoId, Usuario usuarioLogado) {
        if (usuarioLogado == null) return false; // Blindagem contra NPE
        if (usuarioLogado.getRole() == UserRole.ADMIN) return true;

        return eventoRepository.findById(eventoId)
                .map(evento -> evento.getProdutor() != null && evento.getProdutor().getId().equals(usuarioLogado.getId()))
                .orElse(false);
    }

    public boolean isOwnerOrAdminOfPatrocinador(Long patrocinadorId, Usuario usuarioLogado) {
        if (usuarioLogado == null) return false;
        if (usuarioLogado.getRole() == UserRole.ADMIN) return true;

        return patrocinadorRepository.findById(patrocinadorId)
                .map(patrocinador -> patrocinador.getEvento() != null &&
                        patrocinador.getEvento().getProdutor() != null &&
                        patrocinador.getEvento().getProdutor().getId().equals(usuarioLogado.getId()))
                .orElse(false);
    }

    public boolean isOwnerOrAdminOfConvidado(Long convidadoId, Usuario usuarioLogado) {
        if (usuarioLogado == null) return false;
        if (usuarioLogado.getRole() == UserRole.ADMIN) return true;

        return convidadoRepository.findById(convidadoId)
                .map(convidado -> convidado.getPatrocinador() != null &&
                        convidado.getPatrocinador().getEvento() != null &&
                        convidado.getPatrocinador().getEvento().getProdutor() != null &&
                        convidado.getPatrocinador().getEvento().getProdutor().getId().equals(usuarioLogado.getId()))
                .orElse(false);
    }

    public boolean canCheckIn(Long convidadoId, Usuario usuarioLogado) {
        if (usuarioLogado == null) return false;
        if (usuarioLogado.getRole() == UserRole.ADMIN || usuarioLogado.getRole() == UserRole.PORTARIA) {
            return true;
        }
        // Se for produtor, só pode dar check-in nos convidados do evento dele
        return isOwnerOrAdminOfConvidado(convidadoId, usuarioLogado);
    }

    // ATENÇÃO: Nome do método alterado para bater EXATAMENTE com o @PreAuthorize do Controller (QR maiúsculo)
    public boolean canCheckInByQR(UUID codigoQR, Usuario usuarioLogado) {
        if (usuarioLogado == null) return false;

        // Admin e portaria liberados para dar check-in em todos os eventos.
        if (usuarioLogado.getRole() == UserRole.ADMIN || usuarioLogado.getRole() == UserRole.PORTARIA) {
            return true;
        }

        // Se for produtor, só poderá dar check-in se o ingresso pertencer a um evento dele.
        return ingressoRepository.findByCodigoQR(codigoQR)
                .map(ingresso -> ingresso.getConvidado() != null &&
                        ingresso.getConvidado().getPatrocinador() != null &&
                        ingresso.getConvidado().getPatrocinador().getEvento() != null &&
                        ingresso.getConvidado().getPatrocinador().getEvento().getProdutor() != null &&
                        ingresso.getConvidado().getPatrocinador().getEvento().getProdutor().getId().equals(usuarioLogado.getId()))
                .orElse(false); // Se o QR não existir, nega o acesso.
    }

    public boolean isOwnerOrAdminOfPacote(Long pacoteId, Usuario usuarioLogado) {
        if (usuarioLogado == null) return false;
        if (usuarioLogado.getRole() == UserRole.ADMIN) return true;

        return pacoteRepository.findById(pacoteId)
                .map(pacote -> pacote.getEvento() != null &&
                        pacote.getEvento().getProdutor() != null &&
                        pacote.getEvento().getProdutor().getId().equals(usuarioLogado.getId()))
                .orElse(false);
    }
}