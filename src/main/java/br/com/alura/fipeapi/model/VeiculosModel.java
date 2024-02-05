package br.com.alura.fipeapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VeiculosModel(
        @JsonAlias("Valor")
        String valor,

        @JsonAlias("Marca")
        String marca,

        @JsonAlias("Modelo")
        String modelo,

        @JsonAlias("AnoModelo")
        Integer ano,

        @JsonAlias("Combustivel")
        String tipoCombustivel

) {
}
