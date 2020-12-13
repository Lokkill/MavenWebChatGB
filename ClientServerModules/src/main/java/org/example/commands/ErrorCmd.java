package org.example.commands;

import java.io.Serializable;

public class ErrorCmd implements Serializable {
    private final String errorMsg;

    public ErrorCmd(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
