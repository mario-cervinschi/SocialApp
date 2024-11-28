package com.example.reteasocialafx.controller;

import com.example.reteasocialafx.domain.FriendRequest;
import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.service.SocialService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    public TableColumn<Utilizator, String> tableColumnLastName;
    @FXML
    public TableColumn<Utilizator, String> tableColumnFirstName;
    @FXML
    public TableView<Utilizator> tableFollowers;
    @FXML
    public Label connectedAsLabel;

    private final ObservableList<Utilizator> followersObs = FXCollections.observableArrayList();
    private final ObservableList<Utilizator> followingObs = FXCollections.observableArrayList();
    private final ObservableList<Utilizator> notSendRequestsObs = FXCollections.observableArrayList();

    @FXML
    public Button btnSeeUsers;
    @FXML
    public TableView<Utilizator> tableFollowing;
    @FXML
    public TableColumn<Utilizator, String> tableFollowingColumnFirstName;
    @FXML
    public TableColumn<Utilizator, String> tableFollowingColumnLastName;
    @FXML
    public Button btnDeleteFriend;
    @FXML
    public Button btnRequests;

    private SocialService socialService;
    private Utilizator mainUser;

    public void setSocialService(SocialService socialService) {
        this.socialService = socialService;
    }

    public SocialService getSocialService() {
        return socialService;
    }

    public void initApp(Utilizator user) {
        this.mainUser = user;

        var size = socialService.getIncomingFriendships(mainUser).size();
        if(size > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText("You have " + size + " pending friend requests.");
            alert.show();
        }

        tableFollowers.getItems().clear();
        tableFollowing.getItems().clear();

        ArrayList<Optional<Utilizator>> following = socialService.getFollowing(user.getId());
        ArrayList<Optional<Utilizator>> followers = socialService.getFollowers(user.getId());

        for (Optional<Utilizator> optionalFollower : followers) {
            optionalFollower.ifPresent(followersObs::add);
        }
        for (Optional<Utilizator> optionalFollowing : following) {
            optionalFollowing.ifPresent(followingObs::add);
        }

        var allUsers = socialService.getUsers();
        List<Utilizator> newAllUsers = new ArrayList<>();

        var allFollowing = socialService.getFollowing(mainUser.getId());

        for(Utilizator utilizator : allUsers) {
            if(!utilizator.equals(mainUser)) {
                boolean test = false;
                for(Optional<Utilizator> followingg : allFollowing){
                    if(followingg.get().equals(utilizator)) {
                        test = true;
                    }
                }
                for(var prietenie : socialService.getFriendships()){
                    if(prietenie.getIdUser1().equals(mainUser.getId()) && prietenie.getIdUser2().equals(utilizator.getId())) {
                        if(prietenie.getFriendRequest().equals(FriendRequest.PENDING) || prietenie.getFriendRequest().equals(FriendRequest.ACCEPTED)) {
                            test = true;
                        }
                    }
                }
                if(!test) {
                    newAllUsers.add(utilizator);
                }
            }
        }

        notSendRequestsObs.addAll(newAllUsers);

        setConnectedUser();
    }

    private void setConnectedUser() {
        connectedAsLabel.setText("Connected as @" + mainUser.getFirstName() + " " + mainUser.getLastName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableFollowers.setItems(followersObs);
        tableFollowingColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableFollowingColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableFollowing.setItems(followingObs);
    }

    @FXML
    public void handleSeeUsers(ActionEvent actionEvent) throws IOException {

        Stage popupStage = new Stage();
        popupStage.setTitle("Available users");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/available-users-interface.fxml"));
        VBox userWindow = loader.load();

        UserListController userListController = loader.getController();
        userListController.setService(socialService);


        Stage newStage = new Stage();
        Scene scene = new Scene(userWindow);
        newStage.setScene(scene);
        newStage.setTitle("Available Users");
        userListController.setAllUsers(notSendRequestsObs, mainUser);

        newStage.show();

    }

    @FXML
    public void handleDeleteUsers(ActionEvent actionEvent) {

        if(tableFollowers.getSelectionModel().getSelectedItem() == null && tableFollowing.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText("Nothing selected.");
            alert.showAndWait();
            return;
        }

        if(tableFollowing.getSelectionModel().getSelectedItem() == null) {
            Utilizator user = tableFollowers.getSelectionModel().getSelectedItem();
            socialService.deletePrietenie(user.getId(), mainUser.getId());
            followersObs.remove(user);
        }else {
            Utilizator user = tableFollowing.getSelectionModel().getSelectedItem();
            socialService.deletePrietenie(mainUser.getId(), user.getId());
            followingObs.remove(user);
        }
    }

    @FXML
    public void handleSettings(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/settings-interface.fxml"));
        Parent root = stageLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        SettingsController controller = stageLoader.getController();
        controller.setSocialService(this.socialService);
        controller.setCurrentUser(this.mainUser);

        stage.show();
    }

    @FXML
    public void handleRequestsUsers(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/requests-interface.fxml"));
        Parent root = stageLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        RequestsController controller = stageLoader.getController();
        controller.setSocialService(this.socialService);
        controller.setCurrentUser(this.mainUser);
        controller.init();

        stage.show();
    }

    public void handleMessages(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/messages-interface.fxml"));
        Parent root = stageLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        MessagesController controller = stageLoader.getController();
        controller.setSocialService(this.socialService);
        controller.setCurrentUser(this.mainUser);
        controller.init();

        stage.show();
    }
}