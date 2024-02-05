package br.com.alura.fipeapi.principal;

import br.com.alura.fipeapi.model.DadosModel;
import br.com.alura.fipeapi.model.ModelosModel;
import br.com.alura.fipeapi.model.VeiculosModel;
import br.com.alura.fipeapi.service.ConsumoApiService;
import br.com.alura.fipeapi.service.ConverteDadosService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApiService consumo = new ConsumoApiService();
    private ConverteDadosService conversor = new ConverteDadosService();

    private final String URL_BASE = """
            https://parallelum.com.br/fipe/api/v1/%s/marcas""";
    public void exibeMenu() {
        var menu = """
                *** OPÇÕES ***
                1 - Carro
                2 - Moto
                3 - Caminhão
                
                Digite uma das opções para consultar:
                """;

        System.out.println(menu);

        var opcao = scanner.nextLine();

        String endereco;

        switch (opcao) {
            case "1" :
                endereco = URL_BASE.formatted("carros");
                break;
            case "2" :
                endereco = URL_BASE.formatted("motos");
                break;
            case "3" :
                endereco = URL_BASE.formatted("caminhões");
                break;
            default:
                throw new RuntimeException("Opção selecionada é inválida");
        }

        var json = consumo.obterDados(endereco);
        var marcas = conversor.obterLista(json, DadosModel.class);
        marcas.stream()
                .sorted(Comparator.comparing(DadosModel::codigo))
                .forEach(System.out::println);

        System.out.println("Informe a marca desejada: ");
        var codigoMarca = scanner.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(endereco);

        var modeloLista = conversor.obterDados(json, ModelosModel.class);

        System.out.println("\nModelos desta marca: ");

        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(DadosModel::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite um trecho do nome do carro a ser buscado: ");
        var nomeVeiculo = scanner.nextLine();

        List<DadosModel> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nMOdelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("\nDigite por favor o código do modelo para buscar os valores de avaliação: ");
        var codigoModelo = scanner.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);

        List<DadosModel> anos = conversor.obterLista(json, DadosModel.class);
        List<VeiculosModel> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);

            VeiculosModel veiculo = conversor.obterDados(json, VeiculosModel.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veículos filtrados com avaliações por ano: ");

        veiculos.forEach(System.out::println);
    }
}
