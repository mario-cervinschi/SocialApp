package service;

import domain.Prietenie;
import domain.Utilizator;
import repository.database.FriendshipDB;
import repository.database.UserDB;

import java.util.Objects;
import java.util.Optional;

public class SocialService {

    private final UserDB repositoryUser;
    private final FriendshipDB repositoryFriendship;

    public SocialService(UserDB repositoryUser, FriendshipDB repositoryFriendship) {
        this.repositoryUser = repositoryUser;
        this.repositoryFriendship = repositoryFriendship;
    }

    public Iterable<Utilizator> getUsers() {
        return repositoryUser.findAll();
    }

    public Optional<Utilizator> getUser(long id) {
        return repositoryUser.findOne(id);
    }

    public Iterable<Prietenie> getFriendships() {
        return repositoryFriendship.findAll();
    }

    public Optional<Prietenie> getFriend(long id) {
        return repositoryFriendship.findOne(id);
    }

    private Long getNewUserID(){
        Long id = 0L;
        for(var u : getUsers()) {
            id = u.getId();
        }
        id++;
        return id;
    }

    private Long getNewFriendshipID(){
        Long id = 0L;
        for(var u : getFriendships()) {
            id = u.getId();
        }
        id++;
        return id;
    }

    public void addUtilizator(Utilizator utilizator) {
        utilizator.setId(getNewUserID());
        repositoryUser.save(utilizator);
    }

    public void removeUtilizator(Long id){

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

    public void addPrietenie(Prietenie prietenie) {

        var friendships = getFriendships();
        if(prietenie.getIdUser1().equals(prietenie.getIdUser2())) {
            throw new IllegalArgumentException("ID-uri invalide");
        }

        if(friendships != null){
            for(var u : friendships){
                if(Objects.equals(u.getIdUser1(), prietenie.getIdUser1()) && Objects.equals(u.getIdUser2(), prietenie.getIdUser2())){
                    throw new IllegalArgumentException("Prietenie already exists");
                }
            }
        }

        if(repositoryUser.findOne(prietenie.getIdUser1()).isEmpty() || repositoryUser.findOne(prietenie.getIdUser2()).isEmpty()) {
            throw new IllegalArgumentException("Prietenie not found");
        }

        prietenie.setId(getNewFriendshipID());
        repositoryFriendship.save(prietenie);

        Optional<Utilizator> utilizator1 = repositoryUser.findOne(prietenie.getIdUser1());
        Optional<Utilizator> utilizator2 = repositoryUser.findOne(prietenie.getIdUser2());
        if(utilizator1.orElse(null) != null){
            utilizator1.orElse(null).addFriend(utilizator2.orElse(null));
        }
        if(utilizator2.orElse(null) != null){
            utilizator2.orElse(null).addFriend(utilizator1.orElse(null));
        }

    }

    public void deletePrietenie(Long id1, Long id2){

        var friendships = getFriendships();
        Long id = 0L;
        if(friendships != null){
            for(var u : friendships){
                if(Objects.equals(u.getIdUser1(), id1) && Objects.equals(u.getIdUser2(), id2)){
                    id = u.getId();
                }
            }
        }
        if(id.equals(0L)){
            throw new IllegalArgumentException("Prietenie nu exista");
        }

        repositoryFriendship.delete(id);
        Optional<Utilizator> utilizator1 = repositoryUser.findOne(id1);
        Optional<Utilizator> utilizator2 = repositoryUser.findOne(id2);
        if(utilizator1.orElse(null) != null){
            utilizator1.orElse(null).removeFriend(utilizator2.orElse(null));
        }
        if(utilizator2.orElse(null) != null){
            utilizator2.orElse(null).removeFriend(utilizator1.orElse(null));
        }
    }
}
