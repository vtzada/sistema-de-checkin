package br.com.vitortheof.checkin.repository;

import br.com.vitortheof.checkin.model.Convidado;
import br.com.vitortheof.checkin.model.enums.TipoConvidado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConvidadoRepository extends JpaRepository<Convidado, Long> {

    long countByPatrocinadorIdAndTipoConvidado(Long patrocinadorId, TipoConvidado tipoConvidado);
    boolean existsByEmail(String email);
    Optional<Convidado> findByTokenConfirmacao(String tokenConfirmacao);

}
