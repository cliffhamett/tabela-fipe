package br.com.alura.fipeapi.service;

import java.util.List;

public interface IConverteDadosService {
    <T> T obterDados(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T> classe);
}
