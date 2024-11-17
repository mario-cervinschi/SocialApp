package com.example.reteasocialafx.controller;

import com.example.reteasocialafx.domain.FriendRequest;
import com.example.reteasocialafx.domain.Prietenie;
import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.service.SocialService;
import javafx.collections.FXCollections;
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
import java.util.ResourceBundle;

public class UserListController implements Initializable {

    private SocialService service;
    private Utilizator currentUser;
    private final ObservableList<Utilizator> allUsers = FXCollections.observableArrayList();

    public void setService(SocialService service) {
        this.service = service;
    }

    public SocialService getService() {
        return service;
    }

    public void setAllUsers(Iterable<Utilizator> allUsers, Utilizator currentUser) {
        allUsers.forEach(this.allUsers::add);

        this.currentUser = currentUser;
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

        tableUsers.setItems(this.allUsers);

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
        if(user != null) {
            service.addPrietenie(new Prietenie(currentUser.getId(), user.getId(), FriendRequest.PENDING));
            allUsers.remove(user);
        }else{
            throw new IllegalArgumentException();
        }
    }
}
