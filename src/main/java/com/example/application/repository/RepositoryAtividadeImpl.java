package com.example.application.repository;

import com.example.application.entity.Atividade;
import com.example.application.entity.Lista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepositoryAtividadeImpl implements RepositoryAtividade {
    @Override
    public void createAtividade(Atividade atividade) {
        try {
            Connection connection = Conexao.get();
            String sql = "INSERT INTO atividade (id_lista, atividade, descricao, concluida) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, atividade.getLista().getIdLista());
            stmt.setString(2, atividade.getAtividade());
            stmt.setString(3, atividade.getDescricao());
            stmt.setBoolean(4, atividade.getConcluida());
            stmt.execute();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAtividade(Atividade atividade) {
        try {
            Connection connection = Conexao.get();
            String sql = "UPDATE atividade SET atividade = ?, descricao = ?, concluida = ? WHERE id_atividade = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, atividade.getAtividade());
            stmt.setString(2, atividade.getDescricao());
            stmt.setBoolean(3, atividade.getConcluida());
            stmt.setInt(4, atividade.getIdAtividade());
            stmt.execute();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAtividade(Atividade atividade) {
        try {
            Connection connection = Conexao.get();
            String sql = "DELETE FROM atividade WHERE id_atividade = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, atividade.getIdAtividade());
            stmt.execute();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Atividade getAtividadeById(Integer id) {
        try {
            Connection connection = Conexao.get();
            String sql = "SELECT * FROM atividade WHERE id_atividade = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Atividade atividade = null;
            if (rs.next()) {
                atividade = new Atividade();
                atividade.setIdAtividade(rs.getInt("id_atividade"));
                atividade.setAtividade(rs.getString("atividade"));
                atividade.setDescricao(rs.getString("descricao"));
                atividade.setConcluida(rs.getBoolean("concluida"));
                atividade.setLista(
                        new RepositoryListaImpl().getListaById(rs.getInt("id_lista"))
                );
            }
            connection.close();
            return atividade;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Atividade> getAtividadesByLista(Lista lista) {
        List<Atividade> atividades = new ArrayList<>();
        try {
            Connection connection = Conexao.get();
            String sql = "SELECT * FROM atividade WHERE id_lista = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, lista.getIdLista());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Atividade atividade = new Atividade();
                atividade.setIdAtividade(rs.getInt("id_atividade"));
                atividade.setAtividade(rs.getString("atividade"));
                atividade.setDescricao(rs.getString("descricao"));
                atividade.setConcluida(rs.getBoolean("concluida"));
                atividade.setLista(lista);
                atividades.add(atividade);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return atividades;
    }
}
