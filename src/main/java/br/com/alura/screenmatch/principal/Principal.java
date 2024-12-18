package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;

public class Principal {

    private SerieRepository repositorio;
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private List<Serie> series;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar série pelo ator
                    6 - Top 5 séries
                    7 - Buscar série por categoria
                    8 - Buscar pelo número máximo e temporadas e avaliação
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPeloAtor();
                    break;
                case 6:
                    buscarTopCincoSeries();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 8:
                    buscarSeriesPeloNumeroDeTemporadasMaximoEAvaliacaoMinima();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSeriePorCategoria() {
        System.out.println("Digite uma categoria: ");
        var entrada = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(entrada);

        List<Serie> series = repositorio.findByGenero(categoria);
        System.out.println("Series da " + entrada + " encontrada: ");
        series.forEach(System.out::println);
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();

        List<DadosTemporada> temporadas = new ArrayList<>();

        System.out.println("Qual serie você deseja buscar os episodios: ");
        var trechoTitulo = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(trechoTitulo);

        if(!serie.isPresent()) {
            System.out.println("Série não encontrada!");
         return;
        }

        var dadosSerie = serie.get();

        for (int i = 1; i <= dadosSerie.getTotalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(e -> new Episodio(t.numero(), e)))
                .toList();

        dadosSerie.setEpisodios(episodios);
        repositorio.save(dadosSerie);

    }

    private void buscarSeriePorTitulo(){
        System.out.println("Qual titulo da serie você deseja buscar: ");
        var trechoTitulo = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(trechoTitulo);

        if(!serie.isPresent()) {
            System.out.println("Série não encontrada!");
            return;
        }

        System.out.println(serie.get());
    }

    private void buscarSeriesPeloAtor(){
        System.out.println("Qual ator das serie você deseja buscar: ");
        var ator = leitura.nextLine();


       List<Serie> series =  repositorio.findAllByAtoresContainingIgnoreCaseAndAvaliacaoIsGreaterThanEqual(ator, 7.0);

        if(series.isEmpty()){
            System.out.println("Série não encontrada!");
            return;
        }

        System.out.println("Ele trabalhou nas séries: ");
        series.forEach(s -> System.out.println(s.getTitulo()));
        System.out.println();
    }

    private void buscarTopCincoSeries() {
        List<Serie> series = repositorio.findTop5ByOrderByAvaliacaoDesc();

        series.forEach(s -> System.out.println(s.getTitulo() + " " + s.getAvaliacao()));
        System.out.println();
    }

    private void buscarSeriesPeloNumeroDeTemporadasMaximoEAvaliacaoMinima(){
        System.out.println("Digite o número máximo de temporadas:");
        var numeroMaximoDeEntradas = leitura.nextInt();
        leitura.nextLine();

        System.out.println("Digite a avalição mínima:");
        var avaliacaoMinima = leitura.nextDouble();
        leitura.nextLine();

        List<Serie> series = repositorio.findByAvaliacaoIsGreaterThanEqualAndTotalTemporadasIsLessThanEqual(avaliacaoMinima, numeroMaximoDeEntradas);

        series.forEach(System.out::println);
    }
}