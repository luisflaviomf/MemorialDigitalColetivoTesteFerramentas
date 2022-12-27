package com.example.application.repository;

import com.example.application.entity.Usuario;

import java.util.List;

public interface RepositoryUsuario {
    public boolean createUsuario(Usuario usuario);
    public void updateUsuario(Usuario usuario);
    public void deleteUsuario(Usuario usuario);
    public Usuario getUsuarioById(Integer id);
    public Usuario login(String email, String senha);
}
