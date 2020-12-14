package org.example;


import org.example.data.User;

import java.sql.*;
import java.util.ArrayList;

public class SQLCommands {
    private Connection connection;

    protected SQLCommands(Connection connection) {
        this.connection = connection;
    }

    protected void addUserDataBase(String login, String pass, String nick) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Users (login, password, nick) VALUES (?, ?, ?);");
        statement.setString(1, login);
        statement.setString(2, pass);
        statement.setString(3, nick);
        statement.executeUpdate();
    }

    protected User getUserDataBase(String login, String pass) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE (login=? AND password=?);");
        statement.setString(1, login);
        statement.setString(2, pass);
        ResultSet rs = statement.executeQuery();
        User user = null;
        while (rs.next()) {
            user = new User(rs.getString("login"), rs.getString("password"), rs.getString("nick"));
            return user;
        }
        return user;
    }

    protected void changeNickDataBase(String oldNick, String newNick) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE Users SET nick=? WHERE nick=?;");
        statement.setString(1, newNick);
        statement.setString(2, oldNick);
        statement.executeUpdate();
    }

    protected ArrayList<User> getAllUsersDataBase() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users;");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            users.add(new User(rs.getString("login"), rs.getString("password"), rs.getString("nick")));
        }
        return users;
    }

}
