package com.example.application.views;

import com.example.application.entity.Usuario;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import java.util.UUID;

@SpringComponent
@VaadinSessionScope
public class SessionService {
    private String uid = UUID.randomUUID().toString();

    private Usuario usuario = null;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getText() {
        return "session " + uid;
    }
}
