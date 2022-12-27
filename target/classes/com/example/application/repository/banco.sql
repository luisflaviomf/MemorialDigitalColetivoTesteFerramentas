CREATE DATABASE TODOLIST;

CREATE TABLE GRUPO
(
    id_grupo SERIAL PRIMARY KEY,
    grupo    VARCHAR(120)
);

CREATE TABLE LISTA
(
    id_lista SERIAL PRIMARY KEY,
    id_grupo INTEGER REFERENCES GRUPO (id_grupo) ON DELETE CASCADE,
    lista    VARCHAR(120)
);

CREATE TABLE ATIVIDADE
(
    id_atividade SERIAL PRIMARY KEY,
    id_lista     INTEGER REFERENCES LISTA (id_lista) ON DELETE CASCADE,
    atividade    VARCHAR(120),
    descricao    TEXT,
    concluida    BOOLEAN
);

CREATE TABLE USUARIO
(
    id_usuario SERIAL PRIMARY KEY,
    nome       VARCHAR(120),
    email      VARCHAR(120),
    senha      VARCHAR(120)
);

CREATE TABLE COMENTARIO
(
    id_comentario SERIAL PRIMARY KEY,
    id_usuario    INTEGER REFERENCES USUARIO (id_usuario) ON DELETE CASCADE,
    id_atividade  INTEGER REFERENCES ATIVIDADE (id_atividade) ON DELETE CASCADE,
    comentario    TEXT
);
