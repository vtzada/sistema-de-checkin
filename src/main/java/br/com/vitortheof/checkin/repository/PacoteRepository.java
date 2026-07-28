package br.com.vitortheof.checkin.repository;

import br.com.vitortheof.checkin.model.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacoteRepository extends JpaRepository<Pacote, Long> {

    List<Pacote> findByEventoId(Long eventoId);

}
