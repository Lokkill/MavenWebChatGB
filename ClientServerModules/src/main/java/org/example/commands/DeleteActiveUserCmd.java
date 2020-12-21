package org.example.commands;

import java.io.Serializable;

public class DeleteActiveUserCmd implements Serializable {
    private final String nick;

    public DeleteActiveUserCmd(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }
}
