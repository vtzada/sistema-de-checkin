package br.com.vitortheof.checkin.repository;

import br.com.vitortheof.checkin.model.Lote;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LoteRepository extends JpaRepository<Lote, Long> {

    // Bloqueio pessimista: usado antes de reservar o estoque,
    //vai garantir que duas compras simultaneas nao leiam a mesma qntdd vendida
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lote l WHERE l.id = :id")
    Optional<Lote> findByIdForUpdate(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Lote l SET l.qtdVendida = l.qtdVendida + :qtd " +
            "WHERE l.id = :id AND l.qtdVendida + :qtd <= l.qtdTotal")
    int reservarEstoque(@Param("id") Long id, @Param("qtd") int qtd);

    @Modifying
    @Query("UPDATE Lote l SET l.qtdVendida = l.qtdVendida - :qtd " +
            "WHERE l.id = :id AND l.qtdVendida - :qtd >= 0")
    int liberarEstoque(@Param("id") Long id, @Param("qtd") int qtd);

    List<Lote> findByTipoIngressoId(Long tipoIngressoId);

    List<Lote> findByTipoIngressoEventoId(Long eventoId);

}
