package com.example.application.views.grupos;

import com.example.application.entity.Grupo;
import com.example.application.repository.RepositoryGrupo;
import com.example.application.repository.RepositoryGrupoImpl;
import com.example.application.views.MainLayout;
import com.example.application.views.listas.ListasView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Grupos")
@Route(value = "grupos", layout = MainLayout.class)
public class GruposView extends VerticalLayout {
    public IntegerField idField;
    public TextField grupoField;

    public RepositoryGrupo repositoryGrupo = new RepositoryGrupoImpl();

    public Grid<Grupo> grid;

    public Button salvarButton;
    public Button cancelarButton;

    public GruposView() {
        idField = new IntegerField("Id");
        idField.setReadOnly(true);

        grupoField = new TextField("Grupo");
        grupoField.setHelperText("Digite o nome do grupo de Tarefas. Ex: Atividades ou Escola");

        grid = new Grid<>(Grupo.class, false);
        grid.addColumn(Grupo::getGrupo).setHeader("Grupo");
        grid.addComponentColumn(item -> {
            Button button = new Button("Visualizar");
            button.addClickListener(event -> {
                UI.getCurrent().getUI().ifPresent(ui ->
                        ui.navigate(ListasView.class, Integer.toString(item.getIdGrupo()))
                );
            });
            return button;
        });
        grid.addComponentColumn(item -> {
            Button button = new Button("Editar");
            button.addClickListener(event -> {
                idField.setValue(item.getIdGrupo());
                grupoField.setValue(item.getGrupo());
            });
            return button;
        });
        grid.addComponentColumn(item -> {
            Button button = new Button("Excluir");
            button.addClickListener(event -> {
                repositoryGrupo.deleteGrupo(item);
                grid.setItems(repositoryGrupo.getAll());
            });
            return button;
        });

        grid.setItems(repositoryGrupo.getAll());

        salvarButton = new Button("Salvar");
        salvarButton.addClickListener(event -> {
            Grupo grupo = new Grupo();
            grupo.setGrupo(grupoField.getValue());
            grupo.setIdGrupo(idField.getValue());
            if (grupo.getGrupo().isBlank()) {
                Notification.show("Precisa preencher o nome do grupo");
                return;
            }
            if (grupo.getIdGrupo() == null) {
                repositoryGrupo.createGrupo(grupo);
            } else {
                repositoryGrupo.updateGrupo(grupo);
            }
            grid.setItems(repositoryGrupo.getAll());
            cancelarButton.click();
        });

        cancelarButton = new Button("Cancelar");
        cancelarButton.addClickListener(event -> {
            idField.clear();
            grupoField.clear();
        });

        HorizontalLayout buttons = new HorizontalLayout(salvarButton, cancelarButton);

        add(idField, grupoField, buttons, grid);
    }
}
