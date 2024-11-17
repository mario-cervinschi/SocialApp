package com.example.reteasocialafx;

import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.service.SocialService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {
    @FXML
    public Button btnLogIn;
    @FXML
    public TextField email;
    @FXML
    public Button btnSignUp;
    @FXML
    public PasswordField password;

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
            throw new IllegalArgumentException("");
        }
        else{

            FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/main-interface.fxml"));
            Parent root = stageLoader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            UserController controller = stageLoader.getController();
            controller.setSocialService(this.socialService);
            controller.initApp(user);

            stage.show();
        }
    }

    @FXML
    public void onTextChanged(KeyEvent keyEvent) {

    }

    @FXML
    public void onPasswordChanged(KeyEvent keyEvent) {
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
