package com.example.application.views.comentarios;

import com.example.application.entity.Atividade;
import com.example.application.entity.Comentario;
import com.example.application.entity.Usuario;
import com.example.application.repository.RepositoryAtividade;
import com.example.application.repository.RepositoryAtividadeImpl;
import com.example.application.repository.RepositoryComentario;
import com.example.application.repository.RepositoryComentarioImpl;
import com.example.application.views.MainLayout;
import com.example.application.views.SessionService;
import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;


@Route(value = "comentarios", layout = MainLayout.class)
public class ComentariosView extends VerticalLayout implements HasUrlParameter<String>, HasDynamicTitle {
    private Atividade atividade;
    private RepositoryAtividade repositoryAtividade = new RepositoryAtividadeImpl();
    private RepositoryComentario repositoryComentario = new RepositoryComentarioImpl();
    private Grid<Comentario> grid;
    private H2 titulo = new H2();
    private Span descricao = new Span();

    private IntegerField idField;
    private TextArea comentarioField;

    private Button saveButton;
    private Button cancelButton;

    private Usuario usuario;


    public ComentariosView(@Autowired SessionService service) {
        usuario = service.getUsuario();
        if (usuario == null) {
            Notification.show("Você precisa estar logado para acessar essa página");
            UI.getCurrent().navigate(LoginView.class);
            return;
        }

        idField = new IntegerField("Id");
        idField.setReadOnly(true);

        comentarioField = new TextArea("Comentário");
        comentarioField.setHelperText("Deixe um comentario sobre a atividade para outros usuários.");
        grid = new Grid(ComentariosView.class, false);
        grid.addColumn(Comentario::getComentario).setHeader("Comentário");
        grid.addColumn(Comentario::getUsuario).setHeader("Usuário");
        grid.addComponentColumn(item -> {
            if (!item.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) return new Span();
            Button button = new Button("Editar");
            button.addClickListener(event -> {
                idField.setValue(item.getIdComentario());
                comentarioField.setValue(item.getComentario());
            });
            return button;
        });
        grid.addComponentColumn(item -> {
            if (!item.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) return new Span();
            Button button = new Button("Excluir");
            button.addClickListener(event -> {
                repositoryComentario.deleteComentario(item);
                grid.setItems(repositoryComentario.getComentariosByAtividade(atividade));
            });
            return button;
        });
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        saveButton = new Button("Salvar");
        saveButton.addClickListener(event -> {
            Comentario comentario = new Comentario();
            comentario.setUsuario(usuario);
            comentario.setIdComentario(idField.getValue());
            comentario.setComentario(comentarioField.getValue());
            comentario.setAtividade(atividade);
            if (comentario.getComentario().isBlank()) {
                Notification.show("Comentário não pode ser vazio");
                return;
            }
            if (comentario.getIdComentario() == null) {
                repositoryComentario.createComentario(comentario);
            } else {
                repositoryComentario.updateComentario(comentario);
            }
            grid.setItems(repositoryComentario.getComentariosByAtividade(atividade));
            cancelButton.click();
        });

        cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(event -> {
            idField.clear();
            comentarioField.clear();
        });

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        add(titulo, descricao, idField, comentarioField, buttons, grid);
    }

    @Override
    public String getPageTitle() {
        if (atividade == null) return "Comentários";
        return atividade.getAtividade();
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        if (usuario == null) return;
        atividade = repositoryAtividade.getAtividadeById(Integer.parseInt(s));
        grid.setItems(repositoryComentario.getComentariosByAtividade(atividade));
        titulo.setText(atividade.getAtividade());
        descricao.setText(atividade.getDescricao());
    }
}
