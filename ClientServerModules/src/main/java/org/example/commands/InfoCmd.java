package org.example.commands;

import java.io.Serializable;

public class InfoCmd implements Serializable {
    private final String infoMsg;
    private final String sender;

    public String getInfoMsg() {
        return infoMsg;
    }

    public InfoCmd(String infoMsg, String sender) {
        this.infoMsg = infoMsg;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
}
