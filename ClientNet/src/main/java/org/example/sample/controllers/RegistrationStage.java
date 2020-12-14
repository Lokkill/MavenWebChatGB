package org.example.sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.Commands;
import org.example.DBConnection;
import org.example.sample.NetClient;
import org.example.sample.models.Network;

import java.io.IOException;
import java.sql.SQLException;

public class RegistrationStage {
    @FXML
    public TextField txt_nickname;
    @FXML
    public TextField txt_login;
    @FXML
    public PasswordField txt_password;
    @FXML
    public Button btn_Ok;
    @FXML
    public Button btn_Cancel;

    private Network network;

    private NetClient netClient;

    public void initialize(){
        btn_Ok.setOnAction(event -> onOk());
        btn_Cancel.setOnAction(event -> onClose());
    }

    public void onOk(){
        try {
            network.sendMessage(Commands.addNewUserCommand(txt_login.getText(), txt_password.getText(), txt_nickname.getText()));
            network.sendMessage(Commands.updateListClientsCommand());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClose(){
        netClient.closeRegistrationStage();
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }
}
