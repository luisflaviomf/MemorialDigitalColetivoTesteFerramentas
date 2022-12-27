package com.example.application.repository;

import com.example.application.entity.Atividade;
import com.example.application.entity.Lista;

import java.util.List;

public interface RepositoryAtividade {

    public void createAtividade(Atividade atividade);

    public void updateAtividade(Atividade atividade);

    public void deleteAtividade(Atividade atividade);

    public Atividade getAtividadeById(Integer id);

    public List<Atividade> getAtividadesByLista(Lista lista);
}
