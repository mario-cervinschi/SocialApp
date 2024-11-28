package com.example.reteasocialafx.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Prietenie extends Entity<UUID> {

    LocalDateTime date;

    UUID idUser1;
    UUID idUser2;

    FriendRequest friendRequest;

    /**
     * Constructor of the Prietenie from idUser1 and idUser2
     * @param idUser1
     * The id of the first user
     * @param idUser2
     * The id of the second user
     */
    public Prietenie(UUID idUser1, UUID idUser2) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.setId(UUID.randomUUID());
        this.date = LocalDateTime.now();
    }

    public Prietenie(UUID idUser1, UUID idUser2, FriendRequest friendRequest) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.friendRequest = friendRequest;
        this.date = LocalDateTime.now();
        this.setId(UUID.randomUUID());
    }

    public UUID getIdUser1() {
        return idUser1;
    }

    public UUID getIdUser2() {
        return idUser2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public FriendRequest getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
    }
}
