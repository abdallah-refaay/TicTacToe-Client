package com.example.tictactoe;

import com.example.tictactoe.models.CurrentPlayerModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class ClientServerListener extends Thread {
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;
    private static Socket socket;
    private static String currentMsg;
    private static Stage primaryStage;

    public ClientServerListener(){
        setDaemon(true);
        start();
    }

    @Override
    public void start() {
        super.start();
        socket = ClientServerHandler.socket;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        while(true){
            try {
                JsonObject jsonObject = JsonParser.parseString(dataInputStream.readUTF()).getAsJsonObject();
                String type = jsonObject.get("type").getAsString();
                System.out.println(type);
                switch (type){
                    case "invitationreceived":
                        CurrentPlayerModel.opponentId = jsonObject.get("sender").getAsInt();
                        CurrentPlayerModel.gameId = jsonObject.get("game_id").getAsInt();
                        CurrentPlayerModel.opponentUsername = jsonObject.get("opponentusername").getAsString();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Alert invitationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                                        "You received an invitation from User ID: " +
                                                CurrentPlayerModel.opponentId + " from user: " +
                                                CurrentPlayerModel.opponentUsername, ButtonType.NO, ButtonType.YES);
                                invitationAlert.setTitle(CurrentPlayerModel.opponentUsername + " invited you");
                                invitationAlert.setHeaderText("Do you want to accept?");
                                invitationAlert.setResizable(false);
                                invitationAlert.initOwner(primaryStage);
                                Optional<ButtonType> userAnswer = invitationAlert.showAndWait();
                                ButtonType button = userAnswer.orElse(ButtonType.NO);
                                if(button == ButtonType.YES){
                                    System.out.println("Accepted invitation");
                                    CurrentPlayerModel.playerTurn = true;
                                    CurrentPlayerModel.allowFire = true;
                                    CurrentPlayerModel.mySign = "X";
                                    ClientServerHandler.acceptInvitation();
                                }
                                else {
                                    System.out.println("Invitation rejected");
                                }
                            }
                        });
                        break;
                    case "yourinvetationaccepted":
                        int accepterId = jsonObject.get("whoaccepted").getAsInt();
                        CurrentPlayerModel.opponentId = Integer.valueOf(String.valueOf(accepterId));
                        CurrentPlayerModel.gameId = jsonObject.get("game_id").getAsInt();
                        CurrentPlayerModel.playerTurn = false;
                        CurrentPlayerModel.mySign = "O";
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                boolean playAgainstPC = false;
//                                System.out.println("Your invitation was accepted, joining game ID: " + CurrentPlayerModel.gameId.toString());
                                System.out.println("Your accepted invitation is: " + jsonObject);
                            }
                        });
                        break;
                    case "playermove":
                        break;
                    case "opponentmove":
                        break;
                    case "update-list":
                        System.out.println("received update-list");
                        break;
                    default:
                        System.out.println("Invalid server request");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}