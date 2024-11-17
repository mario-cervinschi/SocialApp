package com.example.reteasocialafx;

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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RequestsController implements Initializable {

   

    private final ObservableList<String> incomingRequests = FXCollections.observableArrayList();
    private final ObservableList<String> outgoingRequests = FXCollections.observableArrayList();
    @FXML
    public ListView<String> tableIncoming;
    @FXML
    public ListView<String> tableOutgoing;

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

        for(var u : incoming) {
            incomingRequests.add("ID: " + u.getId().toString() + " " + "From:" + " " + Objects.requireNonNull(service.getUser(u.getIdUser1()).orElse(null)).getFirstName() + " " + Objects.requireNonNull(service.getUser(u.getIdUser1()).orElse(null)).getLastName() + " " + "To: YOU" + " " + "Date:" + " " + u.getDate().toString() + " " + u.getFriendRequest().toString());
        }
        for(var u : outgoing) {
            outgoingRequests.add("ID: " + u.getId().toString() + " " + "From:" + " " + "YOU" + " " + "To:" + " " + Objects.requireNonNull(service.getUser(u.getIdUser2()).orElse(null)).getFirstName() + " " + Objects.requireNonNull(service.getUser(u.getIdUser2()).orElse(null)).getLastName() + " " + "Date:" + " " + u.getDate().toString() + " " + u.getFriendRequest().toString());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableIncoming.setItems(incomingRequests);
        tableOutgoing.setItems(outgoingRequests);
    }

    public void handleDeleteRequest(ActionEvent actionEvent) {
        if(tableIncoming.getSelectionModel().getSelectedItem() == null && tableOutgoing.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        if(tableIncoming.getSelectionModel().getSelectedItem() != null) {
            String selection = tableIncoming.getSelectionModel().getSelectedItem().toString();
            long friendshipID = Long.parseLong(selection.split(" ")[1]);
            service.deletePrietenieByID(friendshipID);
            init();
        }else {
            String selection = tableOutgoing.getSelectionModel().getSelectedItem().toString();
            long friendshipID = Long.parseLong(selection.split(" ")[1]);
            service.deletePrietenieByID(friendshipID);
            init();
        }
    }

    public void handleDeclineRequest(ActionEvent actionEvent) {
        var selection = tableIncoming.getSelectionModel().getSelectedItem().toString();

        long friendship_id = Long.parseLong(selection.split(" ")[1]);

        Prietenie friendship = service.getFriend(friendship_id).orElse(null);
        assert friendship != null;
        friendship.setFriendRequest(FriendRequest.DECLINED);

        service.modifyFriendship(friendship);
        init();
    }

    public void handleAcceptRequest(ActionEvent actionEvent) {
        var selection = tableIncoming.getSelectionModel().getSelectedItem().toString();

        long friendship_id = Long.parseLong(selection.split(" ")[1]);

        Prietenie friendship = service.getFriend(friendship_id).orElse(null);
        assert friendship != null;
        if(!friendship.getFriendRequest().equals(FriendRequest.DECLINED)) {
            friendship.setFriendRequest(FriendRequest.ACCEPTED);
        }

        service.modifyFriendship(friendship);
        init();
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
