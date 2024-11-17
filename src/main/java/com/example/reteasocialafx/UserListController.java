package com.example.reteasocialafx;

import com.example.reteasocialafx.domain.FriendRequest;
import com.example.reteasocialafx.domain.Prietenie;
import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.service.SocialService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserListController implements Initializable {

    private SocialService service;
    private Utilizator currentUser;
    private List<Utilizator> allUsers;
    private ObservableList<Utilizator> followingUsers;

    public void setService(SocialService service) {
        this.service = service;
    }

    public SocialService getService() {
        return service;
    }

    public void setAllUsers(Iterable<Utilizator> allUsers, Utilizator currentUser, ObservableList<Utilizator> followingUsers) {
        this.allUsers = StreamSupport.stream(allUsers.spliterator(), false)
                .collect(Collectors.toList());

        this.currentUser = currentUser;
        tableUsers.getItems().setAll(this.allUsers);
        this.followingUsers = followingUsers;
    }

    @FXML
    public TableView<Utilizator> tableUsers;
    @FXML
    public TableColumn<Utilizator, String> columnFirstName;
    @FXML
    public TableColumn<Utilizator, String> columnLastName;
    @FXML
    public Button btnAddFollowing;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        if (allUsers != null) {
            tableUsers.getItems().setAll(allUsers);
        } else {
            System.out.println("allUsers is null");
        }

        btnAddFollowing.setDisable(true);

        tableUsers.setOnMouseClicked(this::handleRowSelect);
    }

    private void handleRowSelect(MouseEvent mouseEvent) {
        if (tableUsers.getSelectionModel().getSelectedItem() != null) {
            btnAddFollowing.setDisable(false);
        }
    }


    @FXML
    public void handleAddFollowing(ActionEvent actionEvent) {
        Utilizator user = tableUsers.getSelectionModel().getSelectedItem();
        if(user != null && !user.equals(currentUser)) {
            service.addPrietenie(new Prietenie(currentUser.getId(), user.getId(), FriendRequest.PENDING));
        }else{
            throw new IllegalArgumentException();
        }
    }
}
