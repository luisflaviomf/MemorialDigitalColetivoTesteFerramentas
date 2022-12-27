package com.example.application.views.atividades;

import com.example.application.entity.Atividade;
import com.example.application.entity.Lista;
import com.example.application.repository.RepositoryAtividade;
import com.example.application.repository.RepositoryAtividadeImpl;
import com.example.application.repository.RepositoryLista;
import com.example.application.repository.RepositoryListaImpl;
import com.example.application.views.MainLayout;
import com.example.application.views.comentarios.ComentariosView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
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

@Route(value = "atividades", layout = MainLayout.class)
public class AtividadesView extends VerticalLayout implements HasUrlParameter<String>, HasDynamicTitle {

    private Lista lista;
    private RepositoryLista repositoryLista = new RepositoryListaImpl();

    private H2 titulo = new H2();
    private IntegerField idField;
    private TextField atividadeField;
    private TextArea descricaoField;
    private Checkbox concluidaField;

    private Grid<Atividade> grid;

    private RepositoryAtividade repositoryAtividade = new RepositoryAtividadeImpl();

    private Button saveButton;
    private Button cancelButton;


    public AtividadesView() {
        idField = new IntegerField("Id");
        idField.setReadOnly(true);

        atividadeField = new TextField("Atividade");
        atividadeField.setHelperText("Digite o nome da atividade. Ex: Ler artigo X ou Levar o lixo para fora");
        descricaoField = new TextArea("Descrição");
        descricaoField.setHelperText("Digite a descrição/explicação da atividade. Ex: O Fulano precisa ler o artigo até a página 15.");
        concluidaField = new Checkbox("Concluída");

        grid = new Grid(AtividadesView.class, false);
        grid.addColumn(Atividade::getAtividade).setHeader("Atividade");
        grid.addColumn(Atividade::getDescricao).setHeader("Descrição");
        grid.addColumn(Atividade::getConcluida).setHeader("Concluída");
        grid.addComponentColumn(item -> {
            Button button = new Button("Comentarios");
            button.addClickListener(event -> {
                getUI().ifPresent(ui ->
                        ui.navigate(ComentariosView.class, Integer.toString(item.getIdAtividade()))
                );
            });
            return button;
        });
        grid.addComponentColumn(item -> {
            Button button = new Button("Editar");
            button.addClickListener(event -> {
                idField.setValue(item.getIdAtividade());
                atividadeField.setValue(item.getAtividade());
                descricaoField.setValue(item.getDescricao());
                concluidaField.setValue(item.getConcluida());
            });
            return button;
        });
        grid.addComponentColumn(item -> {
            Button button = new Button("Excluir");
            button.addClickListener(event -> {
                repositoryAtividade.deleteAtividade(item);
                grid.setItems(repositoryAtividade.getAtividadesByLista(lista));
            });
            return button;
        });

        saveButton = new Button("Salvar");
        saveButton.addClickListener(event -> {
            Atividade atividade = new Atividade();
            atividade.setIdAtividade(idField.getValue());
            atividade.setAtividade(atividadeField.getValue());
            atividade.setDescricao(descricaoField.getValue());
            atividade.setConcluida(concluidaField.getValue());
            atividade.setLista(lista);
            if (atividade.getAtividade().isBlank()) {
                Notification.show("Atividade não pode ser vazia");
                return;
            }
            if (atividade.getIdAtividade() == null) {
                repositoryAtividade.createAtividade(atividade);
            } else {
                repositoryAtividade.updateAtividade(atividade);
            }
            grid.setItems(repositoryAtividade.getAtividadesByLista(lista));
            cancelButton.click();
        });
        cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(event -> {
            idField.clear();
            atividadeField.clear();
            descricaoField.clear();
            concluidaField.clear();
        });

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);


        add(titulo, idField, atividadeField, descricaoField, concluidaField, buttons, grid);

    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        lista = repositoryLista.getListaById(Integer.parseInt(parameter));
        grid.setItems(repositoryAtividade.getAtividadesByLista(lista));
        titulo.setText(lista.getLista());
    }

    @Override
    public String getPageTitle() {
        return lista.getLista();
    }

}
