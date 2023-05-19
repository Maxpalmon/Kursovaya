package com.example.kursovaya.client;

import com.example.kursovaya.models.Session;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApplication extends Application {
    public static ServerConnection connection;
    public static ClientApplication app;
    public static Stage stage;
    public static Session session;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("WelcomeWindowView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        stage.setTitle("Окно приветствия");
        stage.setScene(scene);
        scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                connection.disconnect();
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();
        this.stage = stage;
        this.app = this;
        this.session = new Session();
    }
    public void switchScene(String fxmlFile){
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource(fxmlFile));
        Parent root;
        try {
            root = (Parent)loader.load();
            this.stage.setScene(new Scene(root));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        connection = new ServerConnection();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(connection);
        launch();
    }
}
