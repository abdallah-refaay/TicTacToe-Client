package com.example.tictactoe.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.tictactoe.*;

import java.io.IOException;
import java.util.Objects;

public class SignupController {

    @FXML
    TextField usrname;

    @FXML
    TextField password;

    @FXML
    TextField cpassword;

    @FXML
    Button Signup;
    @FXML
    Button switchToLogin;

    public void SignupBtnAction(ActionEvent event) throws IOException {
        System.out.println("Pressed action button");
        System.out.println(usrname.getText());
        System.out.println(cpassword.getText());
        System.out.println(password.getText());
        // If the fields are NOT empty && the passwords are similar
        if (!getUserName().isEmpty() && !getPassword().isEmpty() && !getcPassword().isEmpty()) {
            if ((password.getText().equals(cpassword.getText()))) {
                // Call client-server handler static signup function with user-provided username
                // & password
                boolean result = ClientServerHandler.signUp(getUserName(), getPassword());
                if (result) {
                    Stage stage;
                    Scene scene;
                    Parent root;
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/profile.fxml")));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        } else {
            ShowSignUpFailed();
        }
    }

    public void SwitchToLogin(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Login.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void ShowSignUpFailed() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Check your data and try again :)", ButtonType.OK);
        alert.getDialogPane().setMinHeight(100);
        alert.getDialogPane().setMinWidth(100);
        alert.setTitle("sign up Failed");
        alert.setResizable(false);
        alert.show();
    }

    public String getUserName() {
        return usrname.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public String getcPassword() {
        return password.getText();
    }
}