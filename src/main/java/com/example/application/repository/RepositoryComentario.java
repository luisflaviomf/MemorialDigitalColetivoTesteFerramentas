package com.example.application.repository;

import com.example.application.entity.Atividade;
import com.example.application.entity.Comentario;

import java.util.List;

public interface RepositoryComentario {
    public void createComentario(Comentario comentario);
    public void updateComentario(Comentario comentario);
    public void deleteComentario(Comentario comentario);
    public List<Comentario> getComentariosByAtividade(Atividade atividade);
}
