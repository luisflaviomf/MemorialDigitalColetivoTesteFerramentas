package com.example.application.repository;

import com.example.application.entity.Grupo;
import com.example.application.entity.Lista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepositoryListaImpl implements RepositoryLista {
    @Override
    public void createLista(Lista lista) {
        try {
            Connection connection = Conexao.get();
            String sql = "INSERT INTO lista (id_grupo, lista) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, lista.getGrupo().getIdGrupo());
            stmt.setString(2, lista.getLista());
            stmt.execute();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateLista(Lista lista) {
        try {
            Connection connection = Conexao.get();
            String sql = "UPDATE lista SET lista = ? WHERE id_lista = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, lista.getLista());
            stmt.setInt(2, lista.getIdLista());
            stmt.execute();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteLista(Lista lista) {
        try {
            Connection connection = Conexao.get();
            String sql = "DELETE FROM lista WHERE id_lista = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, lista.getIdLista());
            stmt.execute();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Lista getListaById(Integer id) {
        try {
            Connection connection = Conexao.get();
            String sql = "SELECT * FROM lista WHERE id_lista = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Lista lista = new Lista();
            if (rs.next()) {
                lista.setIdLista(rs.getInt("id_lista"));
                lista.setLista(rs.getString("lista"));
                Grupo grupo = new Grupo();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                lista.setGrupo(grupo);
            }
            connection.close();
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Lista> getAllByGroup(Grupo grupo) {
        List<Lista> listas = new ArrayList<>();
        try {
            Connection connection = Conexao.get();
            String sql = "SELECT * FROM lista WHERE id_grupo = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, grupo.getIdGrupo());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Lista lista = new Lista();
                lista.setIdLista(rs.getInt("id_lista"));
                lista.setGrupo(grupo);
                lista.setLista(rs.getString("lista"));
                listas.add(lista);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listas;
    }

}
