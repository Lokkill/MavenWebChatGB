package org.example.commands;

import java.io.Serializable;

public class AuthErrorCmd implements Serializable {
    private final String errorMsg;

    public AuthErrorCmd(String message) {
        this.errorMsg = message;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
