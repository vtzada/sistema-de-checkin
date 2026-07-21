package br.com.vitortheof.checkin.repository;

import br.com.vitortheof.checkin.model.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
}
