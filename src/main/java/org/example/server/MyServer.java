package org.example.server;

import org.example.handler.ClientHandler;
import org.example.service.AuthService;
import org.example.service.BaseAuthService;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    private final int PORT = 8189;

    private List<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService(){
        return authService;
    }

    public MyServer(){
        try (ServerSocket server = new ServerSocket(PORT)){
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true){
                System.out.println("Server wait is connected...");
                Socket socket = server.accept();
                System.out.println("Client connected!");
                new ClientHandler(this, socket);
            }
        }catch (Exception e){
            System.out.println("Server error");
        } finally {
            if (authService != null){
                authService.stop();
            }
        }
    }

    public synchronized boolean isNickBusy(String nick){
        for (ClientHandler o : clients) {
            if (o.getUserName().equals(nick)){
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender){
        for (ClientHandler o : clients){
            if (o == sender){
                continue;
            }
            o.sendMessage(message);
        }
    }

    public synchronized void unsubscribe(ClientHandler o){
        clients.remove(o);
    }

    public synchronized void subscribe(ClientHandler o){
        clients.add(o);
    }
}
