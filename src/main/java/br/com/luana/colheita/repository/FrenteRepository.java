package br.com.luana.colheita.repository;

import br.com.luana.colheita.model.Frente;
import br.com.luana.colheita.model.StatusFrente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * O Spring gera a implementação automaticamente.
 * Métodos como save, findAll, findById e deleteById já vêm prontos;
 * os abaixo são criados só pelo nome do método.
 */
public interface FrenteRepository extends JpaRepository<Frente, Long> {

    Optional<Frente> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Frente> findByStatus(StatusFrente status);
}
