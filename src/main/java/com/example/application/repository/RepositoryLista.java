package com.example.application.repository;

import com.example.application.entity.Grupo;
import com.example.application.entity.Lista;

import java.util.List;

public interface RepositoryLista {
    public void createLista(Lista lista);

    public void updateLista(Lista lista);

    public void deleteLista(Lista lista);

    public Lista getListaById(Integer id);

    public List<Lista> getAllByGroup(Grupo grupo);
}
