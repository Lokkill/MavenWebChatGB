package org.example.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.sample.controllers.AuthStageController;
import org.example.sample.controllers.Controller;
import org.example.sample.models.Network;

import java.util.Arrays;
import java.util.List;

public class NetClient extends Application {

    public static final List<String> USERS_DATA = Arrays.asList("Vasya", "JhonY", "Mishka");
    public Stage primaryStage;
    private Stage authStage;
    private Network network;
    private Controller controller;
    private Thread t1;
    private boolean isActiveThread;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

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

        checkTimeout();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NetClient.class.getResource("/views/sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Messenger");
        primaryStage.setScene(new Scene(root, 600, 400));

        network = new Network();
        AuthStageController authStageController = authLoader.getController();
        authStageController.setNetwork(network);
        authStageController.setNetClient(this);

        if (!network.connection()){
            showErrorMessage("", "Server connection error");
        }
        controller = loader.getController();
        controller.setNetwork(network);

    }

    // TODO Добавить отключение неавторизованных пользователей по таймауту (120 сек. ждём после подключения клиента, и если он не авторизовался за это время, закрываем соединение).
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
}
