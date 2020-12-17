package org.example.data;

import java.util.Objects;

public class User {
    private String login;
    private String pass;
    private String nick;

    public User(String login, String pass, String nick) {
        this.login = login;
        this.pass = pass;
        this.nick = nick;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getNick() {
        return nick;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User entry = (User) obj;
        return Objects.equals(login, entry.login) && Objects.equals(pass, entry.pass) && Objects.equals(nick, entry.nick);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, pass, nick);
    }
}
