package br.com.vitortheof.checkin.repository;

import br.com.vitortheof.checkin.model.Patrocinador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatrocinadorRepository extends JpaRepository<Patrocinador, Long> {

    List<Patrocinador> findByEventoId(Long eventoId);
}
