package org.example.sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.sample.NetClient;
import org.example.sample.models.Network;

public class AuthStageController {

    @FXML
    public TextField txt_login_field;
    @FXML
    public PasswordField txt_pass_field;

    private Network network;
    private NetClient netClient;

    @FXML
    public void checkAuth(){
        String login = txt_login_field.getText();
        String password = txt_pass_field.getText();


        if ((login.trim()).isEmpty() || (password.trim()).isEmpty()){
            NetClient.showErrorMessage("Fields must be filled", "");
            return;
        }

        String authErrMsg = network.sendAuthCommand(login, password);
        if (authErrMsg == null){
            netClient.openChat();
        } else {
            NetClient.showErrorMessage("Auth error", "");
        }
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }
}
