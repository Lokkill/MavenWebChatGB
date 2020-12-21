package org.example.sample.models;

import javafx.application.Platform;
import org.example.Commands;
import org.example.DBConnection;
import org.example.commands.*;
import org.example.data.User;
import org.example.sample.controllers.Controller;
import org.example.sample.log.ChatingHistory;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Network {

    private static final String ADDRESS = "localhost";
    private static final int PORT = 8189;

    private final String host;
    private final int port;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private String userName;

    public List<String> usersList;

    public ObjectInputStream getObjectInputStream() {
        return in;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return out;
    }

    public String getUserName() {
        return userName;
    }

    public Network() {
        this(ADDRESS, PORT);
    }

    public Network(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public boolean connection() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (Exception e) {
            System.out.println("Connection error");
            e.printStackTrace();
            return false;
        }
    }

    public void close(Controller controller) {
        try {
            sendMessage(Commands.deleteActiveUserCommand(userName));
            ChatingHistory ch = new ChatingHistory();
            ch.saveToFile(userName, controller.getChatingHistory());
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waitMessage(Controller controller) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Commands commands = readCommand();
                        if (commands.getType() == null) {
                            continue;
                        }
                        System.out.println(commands.getData());
                        switch (commands.getType()) {
                            case INFO_MSG:
                                InfoCmd infoCmd = (InfoCmd) commands.getData();
                                String msg = infoCmd.getInfoMsg();
                                String sender = infoCmd.getSender();
                                String formatMsg = sender != null ? String.format("%s: %s", sender, msg) : msg;
                                Platform.runLater(() -> {
                                    controller.appendMessage(formatMsg);
                                });
                                break;
                            case ERROR:
                                ErrorCmd errorCmd = (ErrorCmd) commands.getData();
                                String errorMsg = errorCmd.getErrorMsg();
                                Platform.runLater(() -> {
                                    controller.showError("Ошибка сервера", errorMsg);
                                });
                                break;
                            case SEND_USERS:
                                SendUsersCmd sendUsersCmd = (SendUsersCmd) commands.getData();
                                List<User> users = sendUsersCmd.getUsers();
                                controller.updateList(users);
                                break;
                            default:
                                Platform.runLater(() -> {
                                    controller.showError("Unknown command from server!", commands.getType().toString());
                                });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Lost connection");
                }
            }
        });
        t1.setDaemon(true);
        t1.start();
    }

    private Commands readCommand() throws IOException {
        try {
            return (Commands) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Получен неизвестный объект";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMessage(Commands.errorCommand(errorMessage));
            return null;
        }
    }

    public void sendMessage(String message) throws IOException {
        sendMessage(Commands.publicMsgCommand(userName, message));
    }

    public void sendMessage(Commands command) throws IOException {
        out.writeObject(command);
    }

    public void sendPrivateMessage(String message, String recipient) throws IOException {
        Commands command = Commands.privateMsgCommand(recipient, message);
        sendMessage(command);
    }

   public String sendAuthCommand(String login, String password) {

       try {
           Commands authCommand = Commands.authCommand(login, password);
           out.writeObject(authCommand);

           Commands commands = readCommand();
           if (commands == null) {
               return "Ошибка авторизации";
           }
           switch (commands.getType()) {
               case AUTH_OK:
                   AuthOkCmd authOkCmd = (AuthOkCmd) commands.getData();
                   this.userName = authOkCmd.getNick();
                   return null;
               case ERROR:
                   AuthErrorCmd authErrorCmd = (AuthErrorCmd) commands.getData();
                   return authErrorCmd.getErrorMsg();
               default:
                   return "Незивестная команда" + commands.getType();
           }
       } catch (Exception e) {
           e.printStackTrace();
           return e.getMessage();
       }
   }
}
