package org.example.sample.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.sample.NetClient;
import org.example.sample.models.Network;

public class Controller {

    @FXML
    public TextField textField;
    @FXML
    public TextArea txt_chat;
    @FXML
    public ListView listContact;
    @FXML
    public Button btnSend;

    private Network network;

    @FXML
    public void initialize(){
        listContact.setItems(FXCollections.observableArrayList(NetClient.USERS_DATA));
        btnSend.setOnAction(event -> Controller.this.sendMessage());
        textField.setOnAction(event -> Controller.this.sendMessage());
    }

    public void sendMessage() {
        String message = textField.getText();
        appendMessage(message);
        textField.clear();

        try {
            network.getDataOutputStream().writeUTF(message);
        } catch (Exception e){
            e.printStackTrace();
            String error = "Error sending message";
            NetClient.showErrorMessage(e.getMessage(), error);
        }
    }

    public void appendMessage(String message){
        txt_chat.appendText(message);
        txt_chat.appendText(System.lineSeparator());
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
