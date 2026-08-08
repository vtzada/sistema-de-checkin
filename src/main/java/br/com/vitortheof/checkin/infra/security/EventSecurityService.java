package br.com.vitortheof.checkin.infra.security;

import br.com.vitortheof.checkin.model.*;
import br.com.vitortheof.checkin.model.enums.UserRole;
import br.com.vitortheof.checkin.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Function;

@Service("eventSecurity")
@RequiredArgsConstructor // Injeção de dependência via construtor (mais seguro que @Autowired em campo)
public class EventSecurityService {

    private final EventoRepository eventoRepository;
    private final PatrocinadorRepository patrocinadorRepository;
    private final ConvidadoRepository convidadoRepository;
    private final IngressoRepository ingressoRepository;
    private final PacoteRepository pacoteRepository;
    private final TipoIngressoRepository tipoIngressoRepository;
    private final LoteRepository loteRepository;
    private <T> boolean isOwnerOrAdmin(
            Long id, Usuario usuarioLogado, JpaRepository<T, Long> repository, Function<T, Usuario> produtorResolver) {

        if (usuarioLogado == null) return false; // Blindagem contra NPE
        if (usuarioLogado.getRole() == UserRole.ADMIN) return true;

        return repository.findById(id)
                .map(entidade -> {
                    Usuario produtor = produtorResolver.apply(entidade);
                    return produtor != null && produtor.getId().equals(usuarioLogado.getId());
                })
                .orElse(false);
    }

    // ========= PERMISSÕES DO EVENTO E ENTIDADES DEPENDENTES

    public boolean isOwnerOrAdminOfEvento(Long eventoId, Usuario usuarioLogado) {
        return isOwnerOrAdmin(eventoId, usuarioLogado, eventoRepository, Evento::getProdutor);
    }

    public boolean isOwnerOrAdminOfPatrocinador(Long patrocinadorId, Usuario usuarioLogado) {
        return isOwnerOrAdmin(patrocinadorId, usuarioLogado, patrocinadorRepository,
                patrocinador -> patrocinador.getEvento() != null ? patrocinador.getEvento().getProdutor() : null);
    }

    public boolean isOwnerOrAdminOfConvidado(Long convidadoId, Usuario usuarioLogado) {
        return isOwnerOrAdmin(convidadoId, usuarioLogado, convidadoRepository,
                convidado -> convidado.getPatrocinador() != null && convidado.getPatrocinador().getEvento() != null
                        ? convidado.getPatrocinador().getEvento().getProdutor()
                        : null);
    }

    public boolean isOwnerOrAdminOfPacote(Long pacoteId, Usuario usuarioLogado) {
        return isOwnerOrAdmin(pacoteId, usuarioLogado, pacoteRepository,
                pacote -> pacote.getEvento() != null ? pacote.getEvento().getProdutor() : null);
    }

    public boolean isOwnerOrAdminOfTipoIngresso(Long tipoIngressoId, Usuario usuarioLogado) {
        return isOwnerOrAdmin(tipoIngressoId, usuarioLogado, tipoIngressoRepository,
                tipoIngresso -> tipoIngresso.getEvento() != null ? tipoIngresso.getEvento().getProdutor() : null);
    }

    public boolean isOwnerOrAdminOfLote(Long loteId, Usuario usuarioLogado) {
        return isOwnerOrAdmin(loteId, usuarioLogado, loteRepository,
                lote -> lote.getTipoIngresso() != null && lote.getTipoIngresso().getEvento() != null
                        ? lote.getTipoIngresso().getEvento().getProdutor()
                        : null);
    }

    // ========= CHECK-IN (regra própria: não é "dono de uma entidade única",
    // e sim "dono do evento a que o QR pertence", com liberação extra pra PORTARIA)

    public boolean canCheckInByQR(UUID codigoQR, Usuario usuarioLogado) {
        if (usuarioLogado == null) return false;

        // Admin e portaria liberados para dar check-in em todos os eventos.
        if (usuarioLogado.getRole() == UserRole.ADMIN || usuarioLogado.getRole() == UserRole.PORTARIA) {
            return true;
        }

        // Se for produtor, só poderá dar check-in se o ingresso pertencer a um evento dele.
        // A origem do ingresso pode ser um Convidado (via patrocinador) ou um ItemPedido (compra direta);
        // cada caminho leva ao Evento por uma cadeia diferente.
        return ingressoRepository.findByCodigoQR(codigoQR)
                .map(ingresso -> resolveEvento(ingresso.getOrigem()))
                .map(evento -> evento != null &&
                        evento.getProdutor() != null &&
                        evento.getProdutor().getId().equals(usuarioLogado.getId()))
                .orElse(false); // Se o QR não existir, nega o acesso.
    }

    private Evento resolveEvento(Object origem) {
        if (origem instanceof Convidado convidado) {
            return convidado.getPatrocinador() != null ? convidado.getPatrocinador().getEvento() : null;
        }
        if (origem instanceof ItemPedido itemPedido) {
            return itemPedido.getLote() != null && itemPedido.getLote().getTipoIngresso() != null
                    ? itemPedido.getLote().getTipoIngresso().getEvento()
                    : null;
        }
        return null;
    }
}