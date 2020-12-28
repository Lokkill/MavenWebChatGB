package org.example.server;

import org.example.Commands;
import org.example.DBConnection;
import org.example.data.User;
import org.example.handler.ClientHandler;
import org.example.service.AuthService;
import org.example.service.BaseAuthService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MyServer {
    private final int PORT = 8189;
    private DBConnection dbConnection;

    public List<ClientHandler> clients = new ArrayList<>();
    public static List<User> users = new ArrayList<>();
    private AuthService authService;
    private ServerSocket server;
    private static ExecutorService executorService;
    private static Logger logger;

    public AuthService getAuthService(){
        return authService;
    }

    public MyServer() throws IOException {
        logger = LogManager.getLogManager().getLogger(String.valueOf(MyServer.class));
        dbConnection = new DBConnection();
        authService = new BaseAuthService(dbConnection);
        authService.start();
        server = new ServerSocket(PORT);
        executorService = Executors.newFixedThreadPool(10);
    }

    public void startServer(){
        try{
            while (true){
                System.out.println("Server wait is connected...");
                Socket socket = server.accept();
                System.out.println("Client connected!");
                logger.log(Level.INFO, "Подключение нового пользователя");
                new ClientHandler(this, socket, executorService);
            }
        }catch (Exception e){
            logger.log(Level.WARNING, "Ошибка при подключении клиента");
            System.out.println("Server error");
        } finally {
            if (authService != null){
                authService.stop();
                dbConnection.disconnect();
                executorService.shutdown();
            }
        }

    }

    public void addUser(final String login, final String password){
        executorService.execute(() -> {
            try {
                User user = dbConnection.getUser(login, password);
                users.add(user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void createNewUser(final String nick, final String login, final String password){
        executorService.execute(() -> {
            try {
                dbConnection.addUser(login, password, nick);
            } catch (SQLException throwables) {
                logger.log(Level.INFO, "Ошибка создания нового пользователя");
                throwables.printStackTrace();
            }
        });
    }

    public synchronized void updateListUsers(){
        authService.updateClientsList();
    }

    public void changeNickname(final String login, final String newNick){
        executorService.execute(() -> {
            try {
                dbConnection.changeNick(login, newNick);
            } catch (SQLException throwables) {
                logger.log(Level.INFO, "Ошибка изменения никнейма");
                throwables.printStackTrace();
            }
        });
    }

    public synchronized void deleteActiveUser(String nick){
        for (User o : users){
            if (o.getNick().equals(nick)){
                users.remove(o);
            }
        }
    }

    public synchronized List<User> getUsersDataBase(){
        return users;
    }

    public synchronized boolean isNickBusy(String nick){
        for (ClientHandler o : clients) {
            if (o.getUserName().equals(nick)){
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMessage(ClientHandler sender, Commands commands){
        for (ClientHandler o : clients){
            if (o == sender){
                continue;
            }
            o.sendMessage(commands);
        }
    }

    public String getListOfConnected(){
        String listUsers = "/getList";
        for (ClientHandler o : clients){
            listUsers += o.getUserName() + ",";
        }
        listUsers.substring(0, listUsers.length() - 1);
        return listUsers;
    }

    public synchronized void unsubscribe(ClientHandler o){
        clients.remove(o);
    }

    public synchronized void subscribe(ClientHandler o){
        clients.add(o);
    }
}
