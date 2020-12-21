package org.example.commands;

import org.example.data.User;

import java.io.Serializable;

public class AddActiveUserCmd implements Serializable {
    private String login;
    private String password;

    public AddActiveUserCmd(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
