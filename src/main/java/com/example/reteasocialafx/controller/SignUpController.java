package com.example.reteasocialafx.controller;

import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.domain.validators.UtilizatorValidator;
import com.example.reteasocialafx.domain.validators.ValidationException;
import com.example.reteasocialafx.service.SocialService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {

    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    @FXML
    public TextField email;
    @FXML
    public TextField password;
    @FXML
    public TextField confirmPassword;
    @FXML
    public Button btnSignUp;

    private SocialService socialService;
    private UtilizatorValidator validator = new UtilizatorValidator();

    public void setSocialService(SocialService socialService) {
        this.socialService = socialService;
    }

    public SocialService getSocialService() {
        return socialService;
    }

    @FXML
    public void onSignUpButtonCLick(ActionEvent actionEvent) throws IOException {

        Utilizator newUser = new Utilizator(firstName.getText(), lastName.getText(), email.getText(), password.getText());

        try{
            validator.validate(newUser);
        }
        catch(ValidationException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid/Missing required USER fields.");
            alert.showAndWait();
            return;
        }

        if(!password.getText().equals(confirmPassword.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match.");
            alert.showAndWait();
            return;
        }
        else{
            socialService.addUtilizator(newUser);

            FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/login-interface.fxml"));
            Parent root = stageLoader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            LogInController controller = stageLoader.getController();
            controller.setSocialService(this.socialService);

            stage.show();
        }

    }

    public void onBackButtonCLick(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/login-interface.fxml"));
        Parent root = stageLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        LogInController controller = stageLoader.getController();
        controller.setSocialService(this.socialService);

        stage.show();
    }
}
