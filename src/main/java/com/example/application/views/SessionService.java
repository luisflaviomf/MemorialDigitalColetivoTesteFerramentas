package com.example.application.views;

import com.example.application.entity.Usuario;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import java.util.UUID;

@SpringComponent
@VaadinSessionScope
public class Session {
    private String uid = UUID.randomUUID().toString();

    private Usuario usuario;

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Usuario getUsuario() {
        if (usuario == null) {
            UI.getCurrent().getUI().ifPresent(ui -> ui.navigate("/"));
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getText() {
        return "session " + uid;
    }
}
