package com.example.reteasocialafx;

import com.example.reteasocialafx.controller.LogInController;
import com.example.reteasocialafx.domain.validators.PrietenieValidator;
import com.example.reteasocialafx.domain.validators.UtilizatorValidator;
import com.example.reteasocialafx.repository.database.FriendshipDB;
import com.example.reteasocialafx.repository.database.MessageDB;
import com.example.reteasocialafx.repository.database.UserDB;
import com.example.reteasocialafx.service.SocialService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private SocialService service;

    public void setService(SocialService service) {
        this.service = service;
    }
    public SocialService getService() {
        return service;
    }

    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Starting application...");

        UserDB repoUser;
        repoUser = new UserDB(new UtilizatorValidator());
        FriendshipDB repoFriend;
        repoFriend = new FriendshipDB(new PrietenieValidator(repoUser));
        MessageDB repoMessage;
        repoMessage = new MessageDB(repoUser);
        this.service = new SocialService(repoUser, repoFriend, repoMessage);

        initView(stage);
        stage.show();
    }

    public void initView(Stage stage) throws     IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("login-interface.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        LogInController controller = fxmlLoader.getController();
        controller.setSocialService(this.service);
    }

}