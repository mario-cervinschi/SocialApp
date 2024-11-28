package com.example.reteasocialafx.service;


import com.example.reteasocialafx.domain.FriendRequest;
import com.example.reteasocialafx.domain.Message;
import com.example.reteasocialafx.domain.Prietenie;
import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.repository.database.FriendshipDB;
import com.example.reteasocialafx.repository.database.MessageDB;
import com.example.reteasocialafx.repository.database.UserDB;

import java.util.*;
import java.util.stream.StreamSupport;

public class SocialService {

    private final UserDB repositoryUser;
    private final FriendshipDB repositoryFriendship;
    private final MessageDB repositoryMessage;

    public SocialService(UserDB repositoryUser, FriendshipDB repositoryFriendship, MessageDB repositoryMessage) {
        this.repositoryUser = repositoryUser;
        this.repositoryFriendship = repositoryFriendship;
        this.repositoryMessage = repositoryMessage;
    }

    public Iterable<Utilizator> getUsers() {
        return repositoryUser.findAll();
    }

    public Optional<Utilizator> getUser(UUID id) {
        return repositoryUser.findOne(id);
    }

    public Iterable<Prietenie> getFriendships() {
        return repositoryFriendship.findAll();
    }

    public Optional<Prietenie> getFriend(UUID id) {
        return repositoryFriendship.findOne(id);
    }

    public Iterable<Message> getMessages(){return repositoryMessage.findAll();}

    public Optional<Message> getMessage(UUID id){
        return repositoryMessage.findOne(id);
    }

    public ArrayList<Optional<Utilizator>> getFollowers(UUID userId) {
        ArrayList<Optional<Utilizator>> friends = new ArrayList<>();
        for(var friendship : getFriendships()) {
            if(friendship.getIdUser2().equals(userId) && "ACCEPTED".equals(friendship.getFriendRequest().name())) {
                friends.add(getUser(friendship.getIdUser1()));
            }
        }
        return friends;
    }

    public ArrayList<Optional<Utilizator>> getFollowing(UUID userId) {
        ArrayList<Optional<Utilizator>> friends = new ArrayList<>();
        for(var friendship : getFriendships()) {
            if(friendship.getIdUser1().equals(userId) && "ACCEPTED".equals(friendship.getFriendRequest().name())) {
                friends.add(getUser(friendship.getIdUser2()));
            }
        }
        return friends;
    }

    public void addUtilizator(Utilizator utilizator) {
        repositoryUser.save(utilizator);
    }

    public void removeUtilizator(UUID id){

        Optional<Utilizator> e = repositoryUser.findOne(id);

        if(e.isPresent()) {
            var it = getFriendships().iterator();

            while(it.hasNext()) {
                var u = it.next();
                if(Objects.equals(u.getIdUser1(), e.get().getId())) {
                    deletePrietenie(u.getIdUser1(), u.getIdUser2());
                    it = getFriendships().iterator();
                }
                else if(Objects.equals(u.getIdUser2(), e.get().getId())) {
                    deletePrietenie(u.getIdUser1(), u.getIdUser2());
                    it = getFriendships().iterator();
                }
            }
        }

        repositoryUser.delete(id);

    }

    public List<Message> getConversation(UUID fromId, UUID toId) {

        return new ArrayList<>(repositoryMessage.getConversation(fromId, toId));
    }

    public Optional<Message> getLastMessage(UUID fromId, UUID toId) {
        List<Message> conversation = getConversation(fromId, toId);
        if (!conversation.isEmpty()) {
            return Optional.of(conversation.get(conversation.size() - 1)); // Return the last message
        }
        return Optional.empty();
    }

    public void sendMessage(Message message){
//        message.setId(getNewMessageID());
        message.setReply(getLastMessage(message.getFrom(), message.getTo()).orElse(null));
        repositoryMessage.save(message);
    }

    public void addPrietenie(Prietenie prietenie) {

        var friendships = getFriendships();
        if(prietenie.getIdUser1().equals(prietenie.getIdUser2())) {
            throw new IllegalArgumentException("ID-uri invalide");
        }

        if(friendships != null){
            for(var u : friendships){
                if(Objects.equals(u.getIdUser1(), prietenie.getIdUser1()) && Objects.equals(u.getIdUser2(), prietenie.getIdUser2()) && !u.getFriendRequest().equals(FriendRequest.DECLINED)){
                    throw new IllegalArgumentException("Prietenie already exists");
                }
            }
        }

        if(repositoryUser.findOne(prietenie.getIdUser1()).isEmpty() || repositoryUser.findOne(prietenie.getIdUser2()).isEmpty()) {
            throw new IllegalArgumentException("Prietenie not found");
        }

//        prietenie.setId(getNewFriendshipID());
        repositoryFriendship.save(prietenie);

    }

    public void deletePrietenieByID(UUID id){
        repositoryFriendship.delete(id);
    }

    public void deletePrietenie(UUID id1, UUID id2){

        var friendships = getFriendships();
        UUID id = null;
        if(friendships != null){
            for(var u : friendships){
                if(Objects.equals(u.getIdUser1(), id1) && Objects.equals(u.getIdUser2(), id2)){
                    id = u.getId();
                }
            }
        }
        if(id == null){
            throw new IllegalArgumentException("Prietenie nu exista");
        }

        repositoryFriendship.delete(id);
    }

    public Utilizator getUserByEmail(String email){
        Iterable<Utilizator> it = repositoryUser.findAll();
        for(var u : it){
            if(u.getEmail().equals(email)){
                return u;
            }
        }
        return null;
    }

    public Utilizator modifyUser(Utilizator user){
        repositoryUser.update(user);
        return repositoryUser.findOne(user.getId()).orElse(null);
    }

    public Prietenie modifyFriendship(Prietenie friendship){
        repositoryFriendship.update(friendship);
        return repositoryFriendship.findOne(friendship.getId()).orElse(null);
    }

    public List<Prietenie> getIncomingFriendships(Utilizator utilizator){
        var friendships = getFriendships();

       List<Prietenie> incomingFriendships = new ArrayList<>();

        if(friendships != null){
            for(var u : friendships){
                if(u.getIdUser2().equals(utilizator.getId()) && ("PENDING".equals(u.getFriendRequest().name()))){
                    incomingFriendships.add(u);
                }
            }
        }
        return incomingFriendships;
    }

    public List<Prietenie> getOutgoingFriendships(Utilizator utilizator){
        var friendships = getFriendships();

        List<Prietenie> outgoingFriendships = new ArrayList<>();

        if(friendships != null){
            for(var u : friendships){
                if(u.getIdUser1().equals(utilizator.getId()) && ("PENDING".equals(u.getFriendRequest().name()) || "DECLINED".equals(u.getFriendRequest().name()))){
                    outgoingFriendships.add(u);
                }
            }
        }
        return outgoingFriendships;
    }

}
