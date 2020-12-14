package org.example;

import org.example.data.User;

import java.sql.*;
import java.util.ArrayList;

public class DBConnection {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private static SQLCommands sqlCommand;

    public DBConnection(){
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
        }
    }
    public void connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:DBChat.db");
        sqlCommand = new SQLCommands(connection);
    }

    public Connection getConnection() {
        return connection;
    }

    public void addUser(String login, String pass, String nick) throws SQLException {
        sqlCommand.addUserDataBase(login, pass, nick);
    }

    public User getUser(String login, String pass) throws SQLException {
        return sqlCommand.getUserDataBase(login, pass);
    }

    public void changeNick(String oldNick, String newNick) throws SQLException {
        sqlCommand.changeNickDataBase(oldNick, newNick);
    }

    public ArrayList<User> getAllUsers() {
        try {
            return sqlCommand.getAllUsersDataBase();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public static ArrayList<User> getAllUsers(String column) {
        try {
            return sqlCommand.getAllUsersDataBase();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void disconnect(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
