package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);
    List<Serie> findAllByAtoresContainingIgnoreCaseAndAvaliacaoIsGreaterThanEqual(String nome, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);
    List<Serie> findByAvaliacaoIsGreaterThanEqualAndTotalTemporadasIsLessThanEqual(Double avaliacao, Integer temporadas);
}
