package com.example.application.repository;

import com.example.application.entity.Usuario;
import org.postgresql.core.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositoryUsuarioImpl implements RepositoryUsuario {
    @Override
    public boolean createUsuario(Usuario usuario) {
        try {
            Connection connection = Conexao.get();
            String sql = "SELECT email FROM usuario WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario.getEmail());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }

            sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?) RETURNING id_usuario";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario.getNome());
            preparedStatement.setString(2, usuario.getEmail());
            preparedStatement.setString(3, usuario.getSenha());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                usuario.setIdUsuario(resultSet.getInt("id_usuario"));
            }
            preparedStatement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUsuario(Usuario usuario) {
        try {
            Connection connection = Conexao.get();
            String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE id_usuario = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario.getNome());
            preparedStatement.setString(2, usuario.getEmail());
            preparedStatement.setString(3, usuario.getSenha());
            preparedStatement.setInt(4, usuario.getIdUsuario());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUsuario(Usuario usuario) {
        try {
            Connection connection = Conexao.get();
            String sql = "DELETE FROM usuario WHERE id_usuario = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, usuario.getIdUsuario());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Usuario getUsuarioById(Integer id) {
        try {
            Connection connection = Conexao.get();
            String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                preparedStatement.close();
                connection.close();
                return usuario;
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Usuario login(String email, String senha) {
        try {
            Connection connection = Conexao.get();
            String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, senha);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                preparedStatement.close();
                connection.close();
                return usuario;
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
