package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Comunitati {
    SocialService service;
    HashMap<Long, List<Long>> adjList;

    public Comunitati(SocialService service) {
        this.service = service;
    }

    private void DFS(Long v, HashMap<Long, Boolean> vizitat) {
        vizitat.put(v, true);
        System.out.println(v + " " + Objects.requireNonNull(service.getUser(v).orElse(null)).getFirstName() + " " + Objects.requireNonNull(service.getUser(v).orElse(null)).getLastName());
        if(adjList.containsKey(v)){
            adjList.get(v).stream().filter(user -> !vizitat.containsKey(user)).forEach(user -> DFS(user, vizitat));
        }
    }

    public int connectedCommunities(){
        adjList = new HashMap<>();

        service.getUsers().forEach(user -> {
            List<Long> prieteni = new ArrayList<>();
            service.getFriendships().forEach(friend -> {
                if(friend.getIdUser1().equals(user.getId())){
                    prieteni.add(friend.getIdUser2());
                }
                if(friend.getIdUser2().equals(user.getId())){
                    prieteni.add(friend.getIdUser1());
                }
            });
            if(!prieteni.isEmpty()){
                adjList.put(user.getId(), prieteni);
            }
        });

        List<Long> ids = new ArrayList<>();
        service.getUsers().forEach(user -> {
           ids.add(user.getId());
        });

        int nrCommunities = 0;
        HashMap<Long, Boolean> vizitat = new HashMap<>();
        for(var userID : ids){
            if(!vizitat.containsKey(userID)){
                DFS(userID, vizitat);
                nrCommunities++;
                System.out.println();
            }
        }
        return nrCommunities;
    }

    public List<Long> mostSociableCommunity(){
        adjList = new HashMap<>();
        List<Long> max = new ArrayList<>();
        for (var user : service.getUsers()){
            List<Long> prieteni = new ArrayList<>();
            for (var friend : service.getFriendships()){
                if(friend.getIdUser1().equals(user.getId())){
                    prieteni.add(friend.getIdUser2());
                }
                if(friend.getIdUser2().equals(user.getId())){
                    prieteni.add(friend.getIdUser1());
                }
            }
            if(!prieteni.isEmpty()){
                adjList.put(user.getId(), prieteni);
                if(max.size() < prieteni.size() + 1){
                    max = prieteni;
                    max.add(user.getId());
                }
            }
        }

        return max;
    }
    
}
