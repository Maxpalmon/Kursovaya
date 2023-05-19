package com.example.kursovaya.client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox adminRulesCheck;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField mailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button onBackButton;

    @FXML
    private Button registerButton;

    @FXML
    void backButtonClick(ActionEvent event) {
        ClientApplication.app.switchScene("WelcomeWindowView.fxml");
    }

    @FXML
    void registerButton(ActionEvent event) {
        if(fullNameField.getText().isEmpty() || passwordField.getText().isEmpty() ||mailField.getText().isEmpty())
            return;
        ClientApplication.connection.registerRequest(fullNameField.getText(), passwordField.getText(), mailField.getText(), adminRulesCheck.isSelected());
    }
    @FXML
    void initialize() {

    }

}
