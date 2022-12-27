package com.example.application.repository;

import com.example.application.entity.Atividade;
import com.example.application.entity.Comentario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepositoryComentarioImpl implements RepositoryComentario {
    @Override
    public void createComentario(Comentario comentario) {
        try {
            Connection connection = Conexao.get();
            String sql = "INSERT INTO comentario (id_usuario, id_atividade, comentario) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, comentario.getUsuario().getIdUsuario());
            stmt.setInt(2, comentario.getAtividade().getIdAtividade());
            stmt.setString(3, comentario.getComentario());
            stmt.execute();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateComentario(Comentario comentario) {
        try {
            Connection connection = Conexao.get();
            String sql = "UPDATE comentario SET comentario = ? WHERE id_comentario = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, comentario.getComentario());
            stmt.setInt(2, comentario.getIdComentario());
            stmt.execute();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteComentario(Comentario comentario) {
        try {
            Connection connection = Conexao.get();
            String sql = "DELETE FROM comentario WHERE id_comentario = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, comentario.getIdComentario());
            stmt.execute();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Comentario> getComentariosByAtividade(Atividade atividade) {
        List<Comentario> comentarios = new ArrayList<>();
        try {
            Connection connection = Conexao.get();
            String sql = "SELECT * FROM comentario WHERE id_atividade = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, atividade.getIdAtividade());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Comentario comentario = new Comentario();
                comentario.setIdComentario(rs.getInt("id_comentario"));
                comentario.setUsuario(new RepositoryUsuarioImpl().getUsuarioById(rs.getInt("id_usuario")));
                comentario.setAtividade(new RepositoryAtividadeImpl().getAtividadeById(rs.getInt("id_atividade")));
                comentario.setComentario(rs.getString("comentario"));
                comentarios.add(comentario);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comentarios;
    }
}
