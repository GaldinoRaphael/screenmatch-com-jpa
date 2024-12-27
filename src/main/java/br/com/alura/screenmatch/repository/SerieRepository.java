package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);
    List<Serie> findAllByAtoresContainingIgnoreCaseAndAvaliacaoIsGreaterThanEqual(String nome, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);
    List<Serie> findByAvaliacaoIsGreaterThanEqualAndTotalTemporadasIsLessThanEqual(Double avaliacao, Integer temporadas);

    @Query("SELECT s FROM Serie s WHERE s.avaliacao >= :avaliacao AND s.totalTemporadas < :temporadas")
    List<Serie> findByAvaliacaoAndTemporadas(Double avaliacao, Integer temporadas);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ilike %:episodio%")
    List<Episodio> findByEpisodio(String episodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.titulo ilike %:titulo% ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> findTop5BySerie(String titulo);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.dataLancamento >= :data")
    List<Episodio> findEpsidiosByData(LocalDate data);

    @Query("SELECT s FROM Serie s " +
            "JOIN s.episodios e " +
            "GROUP BY s " +
            "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> lancamentosRecentes();

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
    List<Episodio> obterEpisodiosPorTemporada(Long id, Long numero);
}
