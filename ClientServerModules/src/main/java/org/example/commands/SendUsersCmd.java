package org.example.commands;

import org.example.data.User;

import java.io.Serializable;
import java.util.List;

public class SendUsersCmd implements Serializable {
    private List<User> users;

    public SendUsersCmd(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
