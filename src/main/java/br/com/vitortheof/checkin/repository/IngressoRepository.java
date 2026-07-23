package br.com.vitortheof.checkin.repository;

import br.com.vitortheof.checkin.model.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    Optional<Ingresso> findByCodigoQR(UUID codigoQR);

}
