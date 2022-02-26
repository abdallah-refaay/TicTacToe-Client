package com.example.tictactoe.controllers;

import com.example.tictactoe.models.CurrentPlayerModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.tictactoe.*;
import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    Button loginBtn;
    @FXML
    TextField userTxt;
    @FXML
    PasswordField passwordField;
    @FXML
    Button switchToSignUp;
    public static ProfileController myControllerHandle1;

    public void loginBtnAction(ActionEvent event) throws IOException {
        System.out.println("Pressed sign-in button");
        // If the fields are NOT empty
        if (!getUserName().isEmpty() && !getPassword().isEmpty()) {
            // Call client-server handler static signIn function with user-provided username
            // & password
            boolean response = ClientServerHandler.signIn(getUserName(), getPassword());
            // If the response is FALSE, show alert. Means username or password error
            if (!response) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong password or username");
                alert.show();
            }
        } else {
            // Show error if no input is given and button is pressed
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please input text before trying to sign in");
            alert.show();
        }

        // Continue by switching scenes upon successful login
        if (CurrentPlayerModel.login) {
            ClientServerListener clientServerListener = new ClientServerListener(); // Upon successful login, start a
                                                                                    // listener thread pointed at Server

            // Sleep for 1 second while we wait for updated-list from server -- Bug fix for
            // Freezing GUI
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // ClientServerHandler.sendInvitation(1); // Testing invitation system with
            // static data
            Stage stage;
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene;
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
            root = loader.load();
            myControllerHandle1 = loader.getController();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
    }

    public void SwitchToSignUp(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Signup.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public String getUserName() {
        return userTxt.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
}
