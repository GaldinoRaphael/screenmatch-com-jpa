package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodioDto;
import br.com.alura.screenmatch.dto.SerieDto;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    @Autowired
    SerieService serieService;

    @GetMapping
    public List<SerieDto> obterSeries(){
        return serieService.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDto> obterTop5Series(){
        return serieService.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDto> obterLancamentos() {
        return serieService.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDto obterDetalheSerie(@PathVariable Long id){
        return serieService.obterSerie(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDto> buscarTodasTemporadas(@PathVariable Long id){
        return serieService.obterTodasAsTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDto> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Long numero){
        return serieService.obterTemporadasPorNumero(id, numero);
    }

    @GetMapping("/categoria/{nomeGenero}")
    public List<SerieDto> obterSeriePorCategoria(@PathVariable String nomeGenero){
        return serieService.obterSeriePorCategoria(nomeGenero);
    }
}
