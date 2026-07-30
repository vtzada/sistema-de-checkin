package br.com.vitortheof.checkin.repository;

import br.com.vitortheof.checkin.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByProdutorId(Long produtorId);

}
