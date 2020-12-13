package org.example.commands;

import java.io.Serializable;

public class AuthOkCmd implements Serializable {
    private final String nick;

    public AuthOkCmd(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }
}
