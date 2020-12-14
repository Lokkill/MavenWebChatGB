package org.example.handler;

import org.example.data.User;
import org.example.server.MyServer;

import java.io.*;
import java.net.Socket;
import org.example.commands.*;
import org.example.*;
import org.example.service.AuthService;

import java.util.List;

public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public ClientHandler(MyServer myServer, Socket socket){
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.userName = "";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        authentication();
                        readMessage();
                    }catch (Exception e){
                        e.printStackTrace();
                    } finally {
                        closeConnection();
                    }
                }
            }).start();
        } catch (Exception e){
            sendMessage(Commands.errorCommand("Ошибка авторизации пользователя"));
            //throw new RuntimeException("Client connection error");
        }
    }

    public void authentication() throws IOException {
            while (true){
                Commands commands = readCommand();
                if (commands == null){
                    continue;
                }
                if (commands.getType() == CommandsType.AUTH){
                    boolean isSucAuth = processAuth(commands);
                    if (isSucAuth) break;
                    else sendMessage(Commands.authErrorCommand("Ошибка авторизации!"));
                }
            }
    }

    private boolean processAuth(Commands command) throws IOException {
        AuthCmd cmdData = (AuthCmd) command.getData();
        String login = cmdData.getLogin();
        String password = cmdData.getPassword();

        AuthService authService = myServer.getAuthService();
        this.userName = authService.getNickByLoginPass(login, password);
        if (userName != null) {
            if (myServer.isNickBusy(userName)) {
                sendMessage(Commands.authErrorCommand("Логин уже используется"));
                return false;
            }

            sendMessage(Commands.authOkCommand(userName));
            String message = String.format(">>> %s присоединился к чату", userName);
            myServer.broadcastMessage(this, Commands.infoCommand(message, null));
            myServer.subscribe(this);
            return true;
        } else {
            sendMessage(Commands.authErrorCommand("Логин или пароль не соответствуют действительности"));
            return false;
        }
    }

    public void readMessage() throws IOException{

        while (true){
            Commands commands = readCommand();
            if (commands == null){
                continue;
            }

            switch (commands.getType()){
                case END:
                    return;
                case PUBLIC_MSG:
                    PublicMsgCmd data = (PublicMsgCmd) commands.getData();
                    String msg = data.getMessage();
                    String sender = data.getSender();
                    myServer.broadcastMessage(this, Commands.infoCommand(msg, sender));
                    break;
                case PRIVATE_MSG:
                    PrivateMsgCmd privateData = (PrivateMsgCmd) commands.getData();
                    String recipient = privateData.getReceiver();
                    String message = privateData.getMessage();
                    myServer.broadcastMessage(this, Commands.infoCommand(message, recipient));
                    break;
                case ADD_USER:
                    AddUserCmd addUserCmd = (AddUserCmd) commands.getData();
                    String nick = addUserCmd.getNick();
                    String login = addUserCmd.getLogin();
                    String password = addUserCmd.getPassword();
                    myServer.createNewUser(nick, login, password);
                    break;
                case CHANGE_NICK:
                    ChangeNickCmd changeNickCmd = (ChangeNickCmd) commands.getData();
                    String log = changeNickCmd.getOldNick();
                    String newNick = changeNickCmd.getNewNick();
                    myServer.changeNickname(log, newNick);
                    break;
                case UPDATE_LIST_CLIENTS:
                    UpdateListClientsCmd updateListClientsCmd = (UpdateListClientsCmd) commands.getData();
                    myServer.updateListUsers();
                    break;
                case GET_USERS:
                    List<User> users = getUsers();
                    sendMessage(Commands.sendUsersCommand(users));
                default:
                    String error = "Неизвестная комманда " + commands.getType();
                    System.err.println(error);
                    sendMessage(Commands.errorCommand(error));
            }

        }
    }

    private List<User> getUsers(){
        return myServer.getUsersDataBase();
    }

    private Commands readCommand() throws IOException {
        try {
            return (Commands) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Получен неизвестный объект";
            System.err.println(errorMessage);
            e.printStackTrace();
            return null;
        }
    }

    public void sendMessage(Commands commands){
        try {
            out.writeObject(commands);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        myServer.unsubscribe(this);
        try {
            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
