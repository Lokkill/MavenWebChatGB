package org.example.commands;

import java.io.Serializable;

public class PublicMsgCmd implements Serializable {
    private final String sender;
    private final String message;

    public String getSender() {
        return sender;
    }

    public PublicMsgCmd(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }
}
