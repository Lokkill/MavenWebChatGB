package org.example.sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.data.User;
import org.example.sample.NetClient;
import org.example.sample.log.ChatingHistory;
import org.example.sample.models.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Controller {

    @FXML
    public TextField textField;
    @FXML
    public TextArea txt_chat;
    @FXML
    public ListView<String> listContact;
    @FXML
    public Button btnSend;
    @FXML
    public MenuItem MI_changeNick;

    private Network network;

    private NetClient netClient;
    private String selectedRecipient;
    private ArrayList<String> chatingHistory = new ArrayList<>();

    @FXML
    public void initialize() throws Exception {
        listContact.setItems(FXCollections.observableArrayList(NetClient.USERS_DATA));
        btnSend.setOnAction(event -> Controller.this.sendMessage());
        textField.setOnAction(event -> Controller.this.sendMessage());
        MI_changeNick.setOnAction(event -> Controller.this.openChangeNick());

        listContact.setCellFactory(lv -> {
            MultipleSelectionModel<String> selectionModel = listContact.getSelectionModel();
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                listContact.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (selectionModel.getSelectedIndices().contains(index)) {
                        selectionModel.clearSelection(index);
                        selectedRecipient = null;
                    } else {
                        selectionModel.select(index);
                        selectedRecipient = cell.getItem();
                    }
                    event.consume();
                }
            });
            return cell;
        });
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

    public void loadChatingHistory(String nickname) {
        ChatingHistory ch = new ChatingHistory();
        String[] list = ch.readFromFile(nickname);
        // TODO вывод 100 последних сообщений
        int startPosition = Math.max(list.length - 100, 0);
        for (int i = 0; i < list.length; i++) {
            if (i >= startPosition){
                txt_chat.appendText(list[i]);
                txt_chat.appendText(System.lineSeparator());
                chatingHistory.add(list[i]);
            } else {
                chatingHistory.add(list[i]);
            }
        }
    }

    public void sendMessage() {
        String message = textField.getText();
        appendMessage(message);
        textField.clear();

        try {
            if (selectedRecipient != null) {
                network.sendPrivateMessage(message, selectedRecipient);
            } else {
                network.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String error = "Error sending message";
            NetClient.showErrorMessage(e.getMessage(), error);
        }
    }

    public void showError(String title, String message) {
        NetClient.showErrorMessage(message, title);
    }

    public void appendMessage(String message) {
        txt_chat.appendText(message);
        txt_chat.appendText(System.lineSeparator());
        //if (!(message.trim()).isEmpty()){
        chatingHistory.add(message); //network.getUserName() + ": " +
        //}
    }

    public ArrayList<String> getChatingHistory() {
        return chatingHistory;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

//    public void updateUsers(List<String> users) {
//        listContact.setItems(FXCollections.observableArrayList(users));
//    }

    public void updateList(List<User> users) {
        List<String> nicknames = new ArrayList<>();
        for (User o : users) {
            nicknames.add(o.getNick());
        }

        listContact.setItems(FXCollections.observableArrayList(nicknames));
        listContact.refresh();
    }

    public void openChangeNick() {
        try {
            netClient.openChangeNick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
