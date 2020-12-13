package org.example.commands;

import java.io.Serializable;

public class InfoCmd implements Serializable {
    private final String infoMsg;

    public String getInfoMsg() {
        return infoMsg;
    }

    public InfoCmd(String infoMsg) {
        this.infoMsg = infoMsg;
    }
}
