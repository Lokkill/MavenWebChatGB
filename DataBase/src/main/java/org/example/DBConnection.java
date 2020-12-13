package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DBConnection {
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;

    public void connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:DBChat.db");
        statement = connection.createStatement();
    }

}
