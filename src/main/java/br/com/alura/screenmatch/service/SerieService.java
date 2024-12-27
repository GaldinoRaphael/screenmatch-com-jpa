package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDto;
import br.com.alura.screenmatch.dto.SerieDto;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    SerieRepository repository;

    public List<SerieDto> obterTodasAsSeries(){
        return converteSeriesParaDto(repository.findAll());
    }

    public List<SerieDto> obterTop5Series() {
        return converteSeriesParaDto(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDto> obterLancamentos() {
        return converteSeriesParaDto(repository.lancamentosRecentes());
    }

    private List<SerieDto> converteSeriesParaDto(List<Serie> series){
        return series.stream()
                .map(e -> new SerieDto(e.getId(), e.getTitulo(), e.getTotalTemporadas(), e.getAvaliacao(), e.getGenero(), e.getAtores(), e.getPoster(), e.getSinopse()))
                .collect(Collectors.toList());
    }

    public SerieDto obterSerie(Long id) {
        var serie = repository.findById(id);

        if(serie.isPresent()){
            var e = serie.get();
            return new SerieDto(e.getId(), e.getTitulo(), e.getTotalTemporadas(), e.getAvaliacao(), e.getGenero(), e.getAtores(), e.getPoster(), e.getSinopse());
        }

        return null;
    }

    public List<EpisodioDto> obterTodasAsTemporadas(Long id) {
        var serie = repository.findById(id);

        if(serie.isPresent()){
            var s = serie.get();
            return s.getEpisodios().stream().map(e ->  new EpisodioDto(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo())).collect(Collectors.toList());
        }

        return null;
    }

    public List<EpisodioDto> obterTemporadasPorNumero(Long id, Long numero) {
        return repository.obterEpisodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDto(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<SerieDto> obterSeriePorCategoria(String nomeCategoria) {
        Categoria genero = Categoria.fromPortugues(nomeCategoria);
        return converteSeriesParaDto(repository.findByGenero(genero));
    }
}
