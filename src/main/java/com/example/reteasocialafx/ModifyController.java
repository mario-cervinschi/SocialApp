package com.example.reteasocialafx;

import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.domain.validators.ValidationException;
import com.example.reteasocialafx.service.SocialService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jdk.jshell.execution.Util;

import java.io.IOException;

public class ModifyController {

    SocialService socialService;
    Utilizator utilizator;

    public void setSocialService(SocialService socialService) {
        this.socialService = socialService;
    }

    public SocialService getSocialService() {
        return socialService;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    @FXML
    public TextField currentPassword;
    @FXML
    public TextField newPassword;
    @FXML
    public TextField confirmNewPassword;
    @FXML
    public Button btnModify;

    public void onModifyButtonClick(ActionEvent actionEvent) throws IOException {

        if(!firstName.getText().isEmpty() || !lastName.getText().isEmpty()) {
            if(currentPassword.getText().equals(utilizator.getPassword())) {
                if(newPassword.getText().equals(confirmNewPassword.getText())) {
                    Utilizator newUser = new Utilizator(firstName.getText(), lastName.getText(), utilizator.getEmail(), newPassword.getText());
                    newUser.setId(utilizator.getId());
                    socialService.modifyUser(newUser);
                    FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/login-interface.fxml"));
                    Parent root = stageLoader.load();

                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));

                    LogInController controller = stageLoader.getController();
                    controller.setSocialService(this.socialService);

                    stage.show();
                }
            }else{

                Utilizator newUser = new Utilizator(firstName.getText(), lastName.getText(), utilizator.getEmail(), utilizator.getPassword());
                newUser.setId(utilizator.getId());
                socialService.modifyUser(newUser);
                FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/main-interface.fxml"));
                Parent root = stageLoader.load();

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));

                UserController controller = stageLoader.getController();
                controller.setSocialService(this.socialService);
                controller.initApp(newUser);

                stage.show();
            }
        }
        else
            if(currentPassword.getText().equals(utilizator.getPassword())) {
                if (newPassword.getText().equals(confirmNewPassword.getText())) {
                    Utilizator newUser = new Utilizator(utilizator.getFirstName(), utilizator.getLastName(), utilizator.getEmail(), newPassword.getText());
                    newUser.setId(utilizator.getId());
                    socialService.modifyUser(newUser);

                    FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/login-interface.fxml"));
                    Parent root = stageLoader.load();

                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));

                    LogInController controller = stageLoader.getController();
                    controller.setSocialService(this.socialService);

                    stage.show();
                }
            }
        }
}
