package com.example.reteasocialafx.controller;

import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.service.SocialService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController {
    @FXML
    public Button btnLogIn;
    @FXML
    public TextField email;
    @FXML
    public Button btnSignUp;
    @FXML
    public PasswordField password;
    @FXML
    public ImageView bannerImage;

    private SocialService socialService;

    public void setSocialService(SocialService socialService) {
        this.socialService = socialService;
    }

    public SocialService getSocialService() {
        return socialService;
    }

    @FXML
    public void onLogInButtonCLick(ActionEvent actionEvent) throws IOException {
        Utilizator user = socialService.getUserByEmail(email.getText());

        if(user == null || !password.getText().equals(user.getPassword())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Wrong email or password");
            alert.showAndWait();
            return;
        }
        else{
            FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/main-interface.fxml"));
            Parent root = stageLoader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Successful");
            alert.setHeaderText(null);
            alert.setContentText("Log in successful");
            alert.showAndWait();

            UserController controller = stageLoader.getController();
            controller.setSocialService(this.socialService);
            controller.initApp(user);

            stage.show();
        }
    }

    @FXML
    public void onSignUpButtonCLick(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/signup-interface.fxml"));
        Parent root = stageLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        SignUpController controller = stageLoader.getController();
        controller.setSocialService(this.socialService);

        stage.show();
    }
}
