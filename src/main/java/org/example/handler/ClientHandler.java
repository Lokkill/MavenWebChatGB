package org.example.handler;

import org.example.server.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String userName;

    private static final String AUTH_OK = "/authok";
    private static final String AUTH = "/auth";
    private static final String AUTH_ERROR = "/autherr";
    private static final String PRIVATE_MSG = "/w";

    public String getUserName() {
        return userName;
    }

    public ClientHandler(MyServer myServer, Socket socket){
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
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
            throw new RuntimeException("Client connection error");
        }
    }

    public void authentication() throws IOException {
        String str = in.readUTF();
        while (true){
            if (str.startsWith(AUTH)){
                String[] parts = str.split("\\s+", 3);
                String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                if (nick != null){
                    if (myServer.isNickBusy(nick)) {
                        out.writeUTF(AUTH_ERROR + " Login busy");
                    }
                    out.writeUTF(AUTH_OK + " " + nick);
                    userName = nick;
                    myServer.broadcastMessage(userName + " entered the chat", this);
                    myServer.subscribe(this);
                    return;
                } else {
                    out.writeUTF("This account is used");
                }
            } else {
                out.writeUTF("Incorrect login/password");
            }
        }
    }

    public void readMessage() throws IOException{
        while (true){
            String clientMessage = in.readUTF();
            System.out.println("of " + userName + ": " + clientMessage);
            if (clientMessage.equals("/end")){
                return;
            }
            myServer.broadcastMessage(userName + ": " + clientMessage, this);
        }
    }

    public void sendMessage(String message){
        try {
            out.writeUTF(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        myServer.unsubscribe(this);
        myServer.broadcastMessage(userName + " left the chat", this);
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
