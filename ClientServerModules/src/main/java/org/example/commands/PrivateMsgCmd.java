package org.example.commands;

import java.io.Serializable;

public class PrivateMsgCmd implements Serializable {
    private final String receiver;
    private final String message;

    public String getReceiver() {
        return receiver;
    }

    public PrivateMsgCmd(String sernder, String message) {
        this.receiver = sernder;
        this.message = message;
    }
}
