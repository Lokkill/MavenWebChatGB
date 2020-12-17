package org.example.server;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) {
        try {
            new MyServer().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
