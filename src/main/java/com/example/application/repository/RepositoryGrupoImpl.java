package com.example.application.repository;

import com.example.application.entity.Grupo;
import org.postgresql.core.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepositoryGrupoImpl implements RepositoryGrupo {
    @Override
    public void createGrupo(Grupo grupo) {
        try {
            Connection conn = Conexao.get();
            String sql = "INSERT INTO grupo (grupo) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, grupo.getGrupo());
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGrupo(Grupo grupo) {
        try {
            Connection conn = Conexao.get();
            String sql = "UPDATE grupo SET grupo = ? WHERE id_grupo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, grupo.getGrupo());
            stmt.setInt(2, grupo.getIdGrupo());
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteGrupo(Grupo grupo) {
        try {
            Connection conn = Conexao.get();
            String sql = "DELETE FROM grupo WHERE id_grupo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, grupo.getIdGrupo());
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Grupo getGrupoById(Integer id){
        try {
            Connection conn = Conexao.get();
            String sql = "SELECT * FROM grupo WHERE id_grupo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Grupo grupo = new Grupo();
            while (rs.next()) {
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setGrupo(rs.getString("grupo"));
            }
            conn.close();
            return grupo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Grupo> getAll() {
        List<Grupo> grupos = new ArrayList<>();
        try {
            Connection conn = Conexao.get();
            String sql = "SELECT * FROM grupo";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Grupo grupo = new Grupo();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setGrupo(rs.getString("grupo"));
                grupos.add(grupo);
            }
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return grupos;
    }
}
