package com.example.application.views.login;

import com.example.application.entity.Usuario;
import com.example.application.repository.RepositoryUsuario;
import com.example.application.repository.RepositoryUsuarioImpl;
import com.example.application.views.SessionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Login")
@Route(value = "")
@RouteAlias(value = "login")
public class LoginView extends VerticalLayout {

    private TextField emailField;
    private PasswordField passwordField;

    private Button loginButton;
    private Button registerButton;

    private RepositoryUsuario repositoryUsuario = new RepositoryUsuarioImpl();

    public LoginView(@Autowired SessionService sessionService) {

        sessionService.setUsuario(null);

        emailField = new TextField("Email");
        passwordField = new PasswordField("Senha");

        loginButton = new Button("Entrar");
        loginButton.addClickListener(event -> {
            Usuario usuario = repositoryUsuario.login(emailField.getValue(), passwordField.getValue());
            if (usuario == null) {
                Notification.show("Usuário ou senha inválidos");
                return;
            }
            System.out.println(sessionService.getText());
            sessionService.setUsuario(usuario);
            UI.getCurrent().navigate("grupos");
        });

        registerButton = new Button("Cadastrar");
        registerButton.addClickListener(event -> {
            Usuario usuario = new Usuario();
            usuario.setNome(emailField.getValue());
            usuario.setEmail(emailField.getValue());
            usuario.setSenha(passwordField.getValue());
            boolean success = repositoryUsuario.createUsuario(usuario);
            if (success) {
                Notification.show("Usuário cadastrado com sucesso");
                sessionService.setUsuario(usuario);
                UI.getCurrent().navigate("perfil");
            } else {
                Notification.show("Email já cadastrado");
            }
        });

        add(emailField, passwordField, loginButton, registerButton);
    }

}
