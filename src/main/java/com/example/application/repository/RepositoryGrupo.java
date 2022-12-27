package com.example.application.repository;

import com.example.application.entity.Grupo;

import java.util.List;

public interface RepositoryGrupo {
    public void createGrupo(Grupo grupo);
    public void updateGrupo(Grupo grupo);
    public void deleteGrupo(Grupo grupo);

    public Grupo getGrupoById(Integer id);
    public List<Grupo> getAll();
}
