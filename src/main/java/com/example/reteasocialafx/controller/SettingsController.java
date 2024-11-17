package com.example.reteasocialafx.controller;

import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.service.SocialService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    public Button btnLogOut;
    @FXML
    public Button deleteAccount;
    @FXML
    public Button goBack;
    @FXML
    public Button btnModify;

    SocialService socialService;
    Utilizator currentUser;

    public void setSocialService(SocialService socialService) {
        this.socialService = socialService;
    }

    public void setCurrentUser(Utilizator currentUser) {
        this.currentUser = currentUser;
    }

    public SocialService getSocialService(){
        return socialService;
    }

    public void onLogOutButtonCLick(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/login-interface.fxml"));
        Parent root = stageLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        LogInController controller = stageLoader.getController();
        controller.setSocialService(this.socialService);

        stage.show();
    }

    public void onDeleteAccountButtonCLick(ActionEvent actionEvent) throws IOException {
        socialService.removeUtilizator(currentUser.getId());
        onLogOutButtonCLick(actionEvent);
    }

    public void onBackButtonCLick(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/main-interface.fxml"));
        Parent root = stageLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        UserController controller = stageLoader.getController();
        controller.setSocialService(this.socialService);
        controller.initApp(currentUser);

        stage.show();
    }

    public void onModifyButtonCLick(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/modify-interface.fxml"));
        Parent root = stageLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        ModifyController controller = stageLoader.getController();
        controller.setSocialService(this.socialService);
        controller.setUtilizator(currentUser);

        stage.show();
    }
}
