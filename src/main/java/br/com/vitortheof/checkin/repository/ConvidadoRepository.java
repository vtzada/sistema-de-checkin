package br.com.vitortheof.checkin.repository;

import br.com.vitortheof.checkin.model.Convidado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvidadoRepository extends JpaRepository<Convidado, Long> {


}
