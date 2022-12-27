package com.example.application.views.listas;

import com.example.application.entity.Grupo;
import com.example.application.entity.Lista;
import com.example.application.repository.RepositoryGrupo;
import com.example.application.repository.RepositoryGrupoImpl;
import com.example.application.repository.RepositoryLista;
import com.example.application.repository.RepositoryListaImpl;
import com.example.application.views.MainLayout;
import com.example.application.views.atividades.AtividadesView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route(value = "listas", layout = MainLayout.class)
public class ListasView extends VerticalLayout implements HasUrlParameter<String>, HasDynamicTitle {

    private H2 titulo = new H2();
    private Grupo grupo;
    private RepositoryGrupo repositoryGrupo = new RepositoryGrupoImpl();

    private RepositoryLista repositoryLista = new RepositoryListaImpl();

    private IntegerField idField;
    private TextField listaField;

    private Grid<Lista> grid;
    private Button saveButton;
    private Button cancelButton;

    public ListasView() {
        idField = new IntegerField("Id");
        idField.setReadOnly(true);

        listaField = new TextField("Lista");
        listaField.setHelperText("Digite o nome da lista de atividades. Ex: Prioridades ou Artigos para escrever");

        grid = new Grid(ListasView.class, false);
        grid.addColumn(Lista::getLista).setHeader("Lista");
        grid.addComponentColumn(item -> {
            Button button = new Button("Atividades");
            button.addClickListener(event -> {
                getUI().ifPresent(ui ->
                        ui.navigate(AtividadesView.class, Integer.toString(item.getIdLista()))
                );
            });
            return button;
        });
        grid.addComponentColumn(item -> {
            Button button = new Button("Editar");
            button.addClickListener(event -> {
                idField.setValue(item.getIdLista());
                listaField.setValue(item.getLista());
            });
            return button;
        });
        grid.addComponentColumn(item -> {
            Button button = new Button("Excluir");
            button.addClickListener(event -> {
                repositoryLista.deleteLista(item);
                grid.setItems(repositoryLista.getAllByGroup(grupo));
            });
            return button;
        });

        saveButton = new Button("Salvar");
        cancelButton = new Button("Cancelar");
        saveButton.addClickListener(event -> {
            Lista lista = new Lista();
            lista.setIdLista(idField.getValue());
            lista.setLista(listaField.getValue());
            lista.setGrupo(grupo);
            if (lista.getLista().isBlank()) {
                Notification.show("Preencha o campo Lista");
                return;
            }
            if (lista.getIdLista() == null) {
                repositoryLista.createLista(lista);
            } else {
                repositoryLista.updateLista(lista);
            }
            grid.setItems(repositoryLista.getAllByGroup(grupo));
            cancelButton.click();
        });

        cancelButton.addClickListener(event -> {
            idField.clear();
            listaField.clear();
        });

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        add(titulo, idField, listaField, buttons, grid);
    }

    @Override
    public String getPageTitle() {
        return grupo.getGrupo();
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        grupo = repositoryGrupo.getGrupoById(Integer.parseInt(s));
        if (grupo == null) {
            getUI().ifPresent(ui ->
                    ui.navigate(MainLayout.class)
            );
        }

        grid.setItems(repositoryLista.getAllByGroup(grupo));
        titulo.setText(grupo.getGrupo());
    }
}
