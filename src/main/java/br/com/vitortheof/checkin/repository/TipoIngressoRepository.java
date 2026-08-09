package br.com.vitortheof.checkin.repository;

import br.com.vitortheof.checkin.model.TipoIngresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoIngressoRepository extends JpaRepository<TipoIngresso, Long> {

    List<TipoIngresso> findByEventoId(Long eventoId);

}
