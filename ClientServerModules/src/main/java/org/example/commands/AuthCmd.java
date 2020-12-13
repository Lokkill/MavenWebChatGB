package org.example.commands;

import java.io.Serializable;

public class AuthCmd implements Serializable {
    private final String login;
    private final String password;

    public AuthCmd(String login, String password) {
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
