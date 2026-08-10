package br.com.vitortheof.checkin.repository;

import br.com.vitortheof.checkin.model.Evento;
import br.com.vitortheof.checkin.model.enums.CategoriaEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByProdutorId(Long produtorId);

    @Query("SELECT e FROM Evento e WHERE " +
            "e.ativo = true " +
            "AND (:busca IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :busca, '%')) " +
            "     OR LOWER(e.local) LIKE LOWER(CONCAT('%', :busca, '%'))) " +
            "AND (:categoria IS NULL OR e.categoria = :categoria) " +
            "ORDER BY e.data ASC")
    List<Evento> buscarEventos(@Param("busca") String busca, @Param("categoria") CategoriaEvento categoria);
}
