package com.example.kursovaya.client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class WelcomeWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button EnterButton;

    @FXML
    private Button RegisterButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void EnterButton(ActionEvent event) {
        if(loginField.getText().isEmpty() || passwordField.getText().isEmpty())
            return;
        ClientApplication.connection.loginRequest(loginField.getText(), passwordField.getText());
    }

    @FXML
    void GoToRegisterWindow(ActionEvent event) {
        ClientApplication.app.switchScene("RegistrationWindowView.fxml");
    }

    @FXML
    void initialize() {

    }

}
