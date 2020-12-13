package org.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseAuthService implements AuthService{
    private class User {
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

    private List<User> entries;

    @Override
    public void start(){
        System.out.println("Authentication org.example.service starting");
    }

    @Override
    public void stop(){
        System.out.println("Authentication org.example.service stopped");
    }

    public BaseAuthService() {
        entries = new ArrayList<>();
        entries.add(new User("user1", "1234", "Vasya"));
        entries.add(new User("user2", "1234", "JhonY"));
        entries.add(new User("user3", "1234", "Mishka"));
    }

    @Override
    public String getNickByLoginPass(String login, String pass){
        for (User o : entries){
            if (o.login.equals(login) && o.pass.equals(pass)) return o.nick;
        }
        return null;
    }
}
