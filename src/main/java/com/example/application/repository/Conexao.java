package com.example.application.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    public static Connection get() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/todolist", "postgres", "123456");
    }
}
