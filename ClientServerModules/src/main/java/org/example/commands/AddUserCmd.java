package org.example.commands;

import java.io.Serializable;

public class AddUserCmd implements Serializable {
    private final String nick;
    private final String login;
    private final String password;

    public AddUserCmd(String nick, String login, String password) {
        this.nick = nick;
        this.login = login;
        this.password = password;
    }

    public String getNick() {
        return nick;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
