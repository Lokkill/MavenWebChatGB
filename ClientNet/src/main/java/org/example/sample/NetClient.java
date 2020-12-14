package org.example.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.Commands;
import org.example.DBConnection;
import org.example.data.User;
import org.example.sample.controllers.AuthStageController;
import org.example.sample.controllers.Controller;
import org.example.sample.controllers.NickChangeController;
import org.example.sample.controllers.RegistrationStage;
import org.example.sample.models.Network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetClient extends Application {

    public static final List<String> USERS_DATA = Arrays.asList("");
    //public static List<String> USERS_DATA;
    public Stage primaryStage;
    private Stage authStage;
    private Stage changeNickStage;
    private Network network;
    private Controller controller;
    private Thread t1;
    private boolean isActiveThread;
    private Stage registrationStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        network = new Network();
        //fillUsersLists();
        if (!network.connection()){
            showErrorMessage("", "Ошибка подключения к серверу");
        }

        openAuthStage(primaryStage);
        openMessangerStage(primaryStage);

        if (!network.connection()){
            showErrorMessage("", "Server connection error");
        }
        primaryStage.setOnCloseRequest(event -> network.close());

    }

    private void openMessangerStage(Stage primaryStage) throws java.io.IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NetClient.class.getResource("/views/sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Messenger");
        primaryStage.setScene(new Scene(root, 600, 400));
        controller = loader.getController();
        controller.setNetwork(network);
        controller.setNetClient(this);
        network.sendMessage(Commands.getUsersCommand());
    }

    private void openAuthStage(Stage primaryStage) throws java.io.IOException {
        FXMLLoader authLoader = new FXMLLoader();
        authLoader.setLocation(NetClient.class.getResource("/views/authStage.fxml"));
        Parent page = authLoader.load();
        authStage = new Stage();
        authStage.setTitle("Authentication");
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        authStage.setScene(scene);
        authStage.show();

        //checkTimeout();
        AuthStageController authStageController = authLoader.getController();
        authStageController.setNetwork(network);
        authStageController.setNetClient(this);
    }

    private void checkTimeout(){
        t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(120 * 1000);
                    if (!isActiveThread){
                        System.exit(-1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
    }

    public static void showErrorMessage(String message, String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Connection problem");
        alert.setHeaderText(error);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void openChat(){
        isActiveThread = true;
        authStage.close();
        primaryStage.show();
        primaryStage.setTitle(network.getUserName());
        network.waitMessage(controller);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void openChangeNick() throws Exception {
        FXMLLoader changeNickLoader = new FXMLLoader();
        changeNickLoader.setLocation(NetClient.class.getResource("/views/nickChangeStage.fxml"));
        Parent page = changeNickLoader.load();

        changeNickStage = new Stage();
        changeNickStage.setTitle("Смена никнейма");
        changeNickStage.initModality(Modality.WINDOW_MODAL);
        changeNickStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        NickChangeController nch = changeNickLoader.getController();
        nch.setNickInLabel(network);
        nch.setNetClient(this);
        changeNickStage.setScene(scene);
        changeNickStage.show();
    }

    public void openRegistrationStage() throws Exception {
        FXMLLoader registartionLoader = new FXMLLoader();
        registartionLoader.setLocation(NetClient.class.getResource("/views/registrationStage.fxml"));
        Parent page = registartionLoader.load();

        registrationStage = new Stage();
        registrationStage.setTitle("Регистрация");
        registrationStage.initModality(Modality.WINDOW_MODAL);
        registrationStage.initOwner(authStage);
        Scene scene = new Scene(page);
        RegistrationStage rs = registartionLoader.getController();
        rs.setNetwork(network);
        rs.setNetClient(this);
        registrationStage.setScene(scene);
        registrationStage.show();
    }

    public void closeRegistrationStage(){registrationStage.close();}

    public void closeChangeNickStage(){
        changeNickStage.close();
    }
}
