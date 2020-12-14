package org.example.sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.Commands;
import org.example.DBConnection;
import org.example.data.User;
import org.example.sample.NetClient;
import org.example.sample.models.Network;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class NickChangeController {
    @FXML
    private Label lbl_currentNick;

    @FXML
    private TextField txt_newNick;

    private Network network;

    private NetClient netClient;
    private List<User> users;
    private User thisUser;

    public void initialize() {
    }
    public void setNickInLabel(Network net){
        this.network = net;
        lbl_currentNick.setText(network.getUserName());

    }
    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

    //public void setNetwork(Network network) {
//        this.network = network;
//    }

    @FXML
    public void onOk(){
        try {
            network.sendMessage(Commands.changeNicknameCommand(network.getUserName(), txt_newNick.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClose() {
        netClient.closeChangeNickStage();
    }
}
