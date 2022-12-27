package com.example.application.views.perfil;

import com.example.application.entity.Usuario;
import com.example.application.repository.RepositoryUsuario;
import com.example.application.repository.RepositoryUsuarioImpl;
import com.example.application.views.MainLayout;
import com.example.application.views.SessionService;
import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Perfil")
@Route(value = "perfil", layout = MainLayout.class)
public class PerfilView extends VerticalLayout {
    private H2 titulo;
    private TextField nomeField;
    private TextField emailField;
    private PasswordField senhaField;
    private RepositoryUsuario repositoryUsuario = new RepositoryUsuarioImpl();

    private Button atualizarButton;
    private Button deletarButton;

    private Usuario usuario;

    public PerfilView(@Autowired SessionService service) {
        usuario = service.getUsuario();
        if (usuario == null) {
            Notification.show("Você precisa estar logado para acessar essa página");
            UI.getCurrent().navigate(LoginView.class);
            return;
        }
        titulo = new H2("Olá " + usuario.getNome() + "!");
        nomeField = new TextField("Nome");
        nomeField.setValue(usuario.getNome());
        emailField = new TextField("Email");
        emailField.setReadOnly(true);
        emailField.setValue(usuario.getEmail());
        senhaField = new PasswordField("Senha");

        atualizarButton = new Button("Atualizar");
        atualizarButton.addClickListener(event -> {
            if (nomeField.getValue() != null && !nomeField.getValue().isEmpty()) {
                usuario.setNome(nomeField.getValue());

            }

            if (senhaField.getValue() != null && !senhaField.getValue().isEmpty()) {
                usuario.setSenha(senhaField.getValue());
            }

            repositoryUsuario.updateUsuario(usuario);
            Notification.show("Usuário atualizado com sucesso");
        });

        deletarButton = new Button("Deletar");
        deletarButton.addClickListener(event -> {
            repositoryUsuario.deleteUsuario(usuario);
            Notification.show("Usuário deletado com sucesso");
            service.setUsuario(null);
            UI.getCurrent().navigate(LoginView.class);
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout(atualizarButton, deletarButton);
        add(titulo, nomeField, emailField, senhaField, horizontalLayout);
    }

//    @Override
//    public void onAttach(AttachEvent attachEvent) {
//        if (usuario == null) {
//            attachEvent.getUI().navigate(LoginView.class);
//        }
//    }
}
