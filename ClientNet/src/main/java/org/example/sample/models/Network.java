package org.example.sample.models;

import org.example.sample.controllers.Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Network {
    private static final String AUTH_OK = "/authok";
    private static final String AUTH = "/auth";
    private static final String AUTH_ERROR = "/autherr";
    private static final String PRIVATE_MSG = "/w";

    private static final String ADDRESS = "localhost";
    private static final int PORT = 8189;

    private final String host;
    private final int port;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private String userName;

    public DataInputStream getDataInputStream() {
        return in;
    }

    public DataOutputStream getDataOutputStream() {
        return out;
    }

    public String getUserName() {
        return userName;
    }

    public Network(){
        this(ADDRESS, PORT);
    }

    public Network(String host, int port){
        this.host = host;
        this.port = port;
    }

    public boolean connection(){
        try {
            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (Exception e){
            System.out.println("Connection error");
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try {
            socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void waitMessage(Controller controller){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        String message = in.readUTF();
                        if (message.contains(PRIVATE_MSG)){
                            String[] checkPrivate = message.split("\\s");
                            String privateUser = checkPrivate[2];
                            if (userName.equals(privateUser)){
                                String finalMessage = correctPrivateMessage(checkPrivate);
                                controller.appendMessage(finalMessage);
                            }
                        } else {
                            controller.appendMessage(message);
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Lost connection");
                }
            }
        });
        t1.setDaemon(true);
        t1.start();
    }

    private String correctPrivateMessage(String[] checkPrivate) {
        String finalMessage = "";
        for (int i = 0; i < checkPrivate.length; i++) {
            if (i != 1 && i != 2){
                finalMessage+= " " + checkPrivate[i];
            }
        }
        return finalMessage.trim();
    }

    public String sendAuthCommand(String login, String password){
        try {
            out.writeUTF(String.format("%s %s %s", AUTH, login, password));
            String response = in.readUTF();
            if (response.startsWith(AUTH)){
                this.userName = response.split("\\s", 2)[1];
                return null;
            } else {
                return response.split("\\s", 2)[1];
            }
        } catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
