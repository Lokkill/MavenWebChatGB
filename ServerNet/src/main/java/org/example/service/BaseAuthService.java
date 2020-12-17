package org.example.service;

import org.example.DBConnection;
import org.example.data.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService{
    private final DBConnection dbConnection;

    private List<User> entries;

    @Override
    public void start(){
        System.out.println("Authentication my.company.service starting");
    }

    @Override
    public void stop(){
        System.out.println("Authentication my.company.service stopped");
    }

    public BaseAuthService(final DBConnection dbConnection) {
        this.dbConnection = dbConnection;
        updateClientsList();
    }

    @Override
    public void updateClientsList(){
        entries = dbConnection.getAllUsers();
    }

    @Override
    public String getNickByLoginPass(String login, String pass){
        try {
            User user = dbConnection.getUser(login, pass);
            return user.getNick();
        } catch (SQLException throwables) {
            return null;
        }
    }
}
