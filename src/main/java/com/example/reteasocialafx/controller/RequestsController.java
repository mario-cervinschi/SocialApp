package com.example.reteasocialafx.controller;

import com.example.reteasocialafx.domain.FriendRequest;
import com.example.reteasocialafx.domain.Prietenie;
import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.service.SocialService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RequestsController implements Initializable {

    private final ObservableList<Prietenie> incomingRequests = FXCollections.observableArrayList();
    private final ObservableList<Prietenie> outgoingRequests = FXCollections.observableArrayList();
    @FXML
    public ListView<Prietenie> tableIncoming = new ListView<>();
    @FXML
    public ListView<Prietenie> tableOutgoing = new ListView<>();

    private SocialService service;
    private Utilizator currentUser;

    public void setSocialService(SocialService service) {
        this.service = service;
    }

    public SocialService getSocialService() {
        return service;
    }

    public void setCurrentUser(Utilizator currentUser) {
        this.currentUser = currentUser;
    }

    public Utilizator getCurrentUser() {
        return currentUser;
    }

    public void init(){
        tableIncoming.getItems().clear();
        tableOutgoing.getItems().clear();

        List<Prietenie> incoming = service.getIncomingFriendships(currentUser);
        List<Prietenie> outgoing = service.getOutgoingFriendships(currentUser);

        incomingRequests.addAll(incoming);
        outgoingRequests.addAll(outgoing);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        tableIncoming.setCellFactory(listView -> new ListCell<Prietenie>() {
            @Override
            protected void updateItem(Prietenie prietenie, boolean empty) {
                super.updateItem(prietenie, empty);
                if (empty || prietenie == null) {
                    setText(null);
                } else {
                    String from = service.getUser(prietenie.getIdUser1()).get().toString();
                    String date = prietenie.getDate().format(formatter);
                    setText("Request from: " + from + ". Date sent: " + date);
                }
            }
        });

        tableOutgoing.setCellFactory(listView -> new ListCell<Prietenie>() {
            @Override
            protected void updateItem(Prietenie prietenie, boolean empty) {
                super.updateItem(prietenie, empty);
                if (empty || prietenie == null) {
                    setText(null);
                } else {
                    String to = service.getUser(prietenie.getIdUser2()).get().toString();
                    String date = prietenie.getDate().format(formatter);
                    setText("Sent to: " + to + " | Date sent: " + date + " | Status: " + prietenie.getFriendRequest().toString());
                }
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableIncoming.setItems(incomingRequests);
        tableOutgoing.setItems(outgoingRequests);
    }

    public void handleDeleteRequest(ActionEvent actionEvent) {
        if(tableIncoming.getSelectionModel().getSelectedItem() == null && tableOutgoing.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(null);
            alert.setContentText("Nothing selected.");
            alert.showAndWait();
            return;
        }

        if(tableIncoming.getSelectionModel().getSelectedItem() != null) {
            Prietenie selection = tableIncoming.getSelectionModel().getSelectedItem();
            UUID friendshipID = selection.getId();
            service.deletePrietenieByID(friendshipID);
            incomingRequests.remove(selection);
            tableIncoming.refresh();
            //init();
        }else {
            Prietenie selection = tableOutgoing.getSelectionModel().getSelectedItem();
            UUID friendshipID = selection.getId();
            service.deletePrietenieByID(friendshipID);
            outgoingRequests.remove(selection);
            //tableOutgoing.refresh();
            //init();
        }
    }

    public void handleDeclineRequest(ActionEvent actionEvent) {
        var selection = tableIncoming.getSelectionModel().getSelectedItem();

        if(selection != null){
            UUID friendship_id = selection.getId();

            Prietenie friendship = service.getFriend(friendship_id).orElse(null);
            assert friendship != null;
            friendship.setFriendRequest(FriendRequest.DECLINED);
            incomingRequests.remove(selection);
            service.modifyFriendship(friendship);
            //incomingRequests.add(service.modifyFriendship(friendship));
            //tableIncoming.refresh();
            //init();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete error");
            alert.setHeaderText(null);
            alert.setContentText("Can't decline outgoing requests. Only delete them.");
            alert.showAndWait();
            return;
        }
    }

    public void handleAcceptRequest(ActionEvent actionEvent) {
        var selection = tableIncoming.getSelectionModel().getSelectedItem();

        if(selection != null){
            UUID friendship_id = selection.getId();

            Prietenie friendship = service.getFriend(friendship_id).orElse(null);
            if (!friendship.getFriendRequest().equals(FriendRequest.DECLINED)) {
                friendship.setFriendRequest(FriendRequest.ACCEPTED);
            }

            service.modifyFriendship(friendship);
            incomingRequests.remove(selection);
            // tableIncoming.refresh();
            //init();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete error");
            alert.setHeaderText(null);
            alert.setContentText("Can't accept outgoing requests. Only delete them.");
            alert.showAndWait();
            return;
        }
    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/main-interface.fxml"));
        Parent root = stageLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        UserController controller = stageLoader.getController();
        controller.setSocialService(this.service);
        controller.initApp(currentUser);

        stage.show();
    }
}
