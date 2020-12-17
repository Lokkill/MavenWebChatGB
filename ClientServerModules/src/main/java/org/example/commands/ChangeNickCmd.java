package org.example.commands;

import java.io.Serializable;

public class ChangeNickCmd implements Serializable {
    private final String oldNick;
    private final String newNick;

    public ChangeNickCmd(String oldNick, String newNick) {
        this.oldNick = oldNick;
        this.newNick = newNick;
    }

    public String getOldNick() {
        return oldNick;
    }

    public String getNewNick() {
        return newNick;
    }
}
