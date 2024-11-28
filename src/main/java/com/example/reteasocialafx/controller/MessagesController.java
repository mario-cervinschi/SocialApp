package com.example.reteasocialafx.controller;

import com.example.reteasocialafx.domain.Message;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessagesController {

    @FXML
    public Button sendButton;
    @FXML
    public TextField messageInput;
    @FXML
    public ListView userListView;
    @FXML
    public TextField searchBox;
    @FXML
    public ListView currentConversationListView;
    @FXML
    private SplitPane splitPane;

    private SocialService socialService;
    private Utilizator utilizator;
    private ObservableList<Message> allCurrentMessages = FXCollections.observableArrayList();

    public void setSocialService(SocialService socialService) {
        this.socialService = socialService;
    }

    public void setCurrentUser(Utilizator mainUser) {
        this.utilizator = mainUser;
    }

    public void init() {
        splitPane.setDividerPositions(0.25);
        messageInput.setDisable(true);
        sendButton.setDisable(true);

        ObservableList<Utilizator> allUsers = FXCollections.observableArrayList();
        ObservableList<Utilizator> filteredUsers = FXCollections.observableArrayList();

        // Load all users except the current user
        socialService.getUsers().forEach(user -> {
            if (!Objects.equals(user.getId(), utilizator.getId())) {
                allUsers.add(user);
            }
        });

        // Initially display all users
        filteredUsers.setAll(allUsers);
        userListView.setItems(filteredUsers);

        // Add a listener to the searchBox to filter users dynamically
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchTerm = newValue.toLowerCase(); // Case-insensitive search
            filteredUsers.setAll(allUsers.filtered(user -> user.toString().toLowerCase().contains(searchTerm)));
        });

        userListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            currentConversationListView.setCellFactory(listView -> new ListCell<Message>() {
                @Override
                protected void updateItem(Message message, boolean empty) {
                    super.updateItem(message, empty);
                    if (empty || message == null) {
                        setText(null);
                    } else {
                        String senderName = ((Utilizator) Objects.requireNonNull(socialService.getUser(message.getFrom()).orElse(null))).toString();
                        setText(senderName + ": " + message.getMessage());
                    }
                }
            });

            boolean hasUserSelected = newSelection != null;
            messageInput.setDisable(!hasUserSelected);
            sendButton.setDisable(!hasUserSelected);

            if (hasUserSelected && oldSelection == newSelection) {
                currentConversationListView.refresh();
            } else if (hasUserSelected && newSelection != oldSelection) {
                allCurrentMessages.clear();
                allCurrentMessages.addAll(socialService.getConversation(utilizator.getId(), ((Utilizator) newSelection).getId()));
                currentConversationListView.setItems(allCurrentMessages);
            }
        });
    }


    public void handleSendMessage(ActionEvent actionEvent) {
        Utilizator selectedUser = (Utilizator) userListView.getSelectionModel().getSelectedItem();
        String message = messageInput.getText();

        if (selectedUser != null && !message.isEmpty()) {
            Message sentNow = new Message(message, utilizator.getId(), selectedUser.getId());
            allCurrentMessages.add(sentNow);
            currentConversationListView.refresh();
            socialService.sendMessage(sentNow);
            messageInput.clear();
        }
    }

    public void onBackButtonCLick(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialafx/main-interface.fxml"));
        Parent root = stageLoader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        UserController controller = stageLoader.getController();
        controller.setSocialService(this.socialService);
        controller.initApp(this.utilizator);

        stage.show();
    }
}
