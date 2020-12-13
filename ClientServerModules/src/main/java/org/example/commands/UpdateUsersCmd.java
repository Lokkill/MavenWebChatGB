package org.example.commands;

import java.io.Serializable;
import java.util.List;

public class UpdateUsersCmd implements Serializable {
    private final List<String> users;

    public List<String> getUsers() {
        return users;
    }

    public UpdateUsersCmd(List<String> users) {
        this.users = users;
    }
}
