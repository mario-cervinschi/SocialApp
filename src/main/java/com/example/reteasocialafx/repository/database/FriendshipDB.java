package com.example.reteasocialafx.repository.database;


import com.example.reteasocialafx.domain.FriendRequest;
import com.example.reteasocialafx.domain.Prietenie;
import com.example.reteasocialafx.domain.validators.PrietenieValidator;
import com.example.reteasocialafx.repository.FriendshipRepository;
import com.example.reteasocialafx.service.DataBaseRun;
import com.example.reteasocialafx.util.paging.Page;
import com.example.reteasocialafx.util.paging.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class FriendshipDB implements FriendshipRepository {

    private PrietenieValidator prietenieValidator;

    public FriendshipDB(PrietenieValidator prietenieValidator) {
        this.prietenieValidator = prietenieValidator;
    }

    @Override
    public Optional<Prietenie> findOne(UUID aLong) {
        Prietenie friendship = null;
        try(var connection = DataBaseRun.connect()){
            String query = "SELECT * FROM friendship where id_friendship = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, aLong.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id_friendship"));
                UUID id_user1 = UUID.fromString(resultSet.getString("id_user1"));
                UUID id_user2 = UUID.fromString(resultSet.getString("id_user2"));
                Timestamp timestamp = resultSet.getTimestamp("date");
                String status = resultSet.getString("status");

                LocalDateTime date = timestamp != null ? timestamp.toLocalDateTime() : null;

                friendship = new Prietenie(id_user1, id_user2, FriendRequest.valueOf(status));
                friendship.setId(id);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(friendship);
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Map<UUID, Prietenie> entities = new HashMap<>();
        try(var connection = DataBaseRun.connect()){
            String query = "SELECT * FROM friendship";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id_friendship"));
                UUID id_user1 = UUID.fromString(resultSet.getString("id_user1"));
                UUID id_user2 = UUID.fromString(resultSet.getString("id_user2"));
                Timestamp timestamp = resultSet.getTimestamp("date");
                String status = resultSet.getString("status");

                LocalDateTime date = timestamp != null ? timestamp.toLocalDateTime() : null;

                Prietenie friendship = new Prietenie(id_user1, id_user2, FriendRequest.valueOf(status));
                friendship.setId(id);
                friendship.setDate(date);
                entities.put(friendship.getId(), friendship);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return entities.values();
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entity cannot be null");
        }
        String query = "INSERT INTO friendship (id_friendship, id_user1, id_user2, date, status) VALUES (?, ?, ?, ?, ?)";

        try(var connection = DataBaseRun.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, entity.getId().toString());
            preparedStatement.setString(2, entity.getIdUser1().toString());
            preparedStatement.setString(3, entity.getIdUser2().toString());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
            preparedStatement.setString(5, entity.getFriendRequest().name());
            preparedStatement.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<Prietenie> delete(UUID aLong) {
        String query = "DELETE FROM friendship WHERE id_friendship = ?";

        Prietenie friendshipToDelete = null;
        for(var friendship : findAll()){
            if(Objects.equals(friendship.getId(), aLong)){
                friendshipToDelete = friendship;
            }
        }

        try(var connection = DataBaseRun.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, aLong.toString());
            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(friendshipToDelete);
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Friendship ID cannot be null");
        }

        String query = "UPDATE friendship SET id_user1 = ?, id_user2 = ?, date = ?, status = ? WHERE id_friendship = ?";

        try (var connection = DataBaseRun.connect()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, entity.getIdUser1().toString());
            ps.setString(2, entity.getIdUser2().toString());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
            ps.setString(4, entity.getFriendRequest().name());
            ps.setString(5, entity.getId().toString());

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private int count(Connection connection, String userID, String type) throws SQLException {
        String sql = "select count(*) as count from friendship";
        if(type.equals("Following")){
            sql += " where id_user1 = ? AND status = 'ACCEPTED'";
        }
        else{
            sql += " where id_user2 = ? AND status = 'ACCEPTED'";
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userID);
            try (ResultSet result = statement.executeQuery()) {
                int totalNumberOfMovies = 0;
                if (result.next()) {
                    totalNumberOfMovies = result.getInt("count");
                }
                return totalNumberOfMovies;
            }
        }
    }

    private List<Prietenie> findAllOnPage(Connection connection, Pageable pageable, String userID, String type) throws SQLException {
        List<Prietenie> friendsOnPage = new ArrayList<>();
        // Using StringBuilder rather than "+" operator for concatenating Strings is more performant
        // since Strings are immutable, so every operation applied on a String will create a new String
        String sql = "select * from friendship";
        if(type.equals("Following")){
            sql += " where id_user1 = ? AND status = 'ACCEPTED'";
        }
        else{
            sql += " where id_user2 = ? AND status = 'ACCEPTED'";
        }
        sql += " limit ? offset ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 0;
            statement.setString(++paramIndex, userID);
            statement.setInt(++paramIndex, pageable.getPageSize());

            statement.setInt(++paramIndex, pageable.getPageSize() * pageable.getPageNumber());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UUID id = UUID.fromString(resultSet.getString("id_friendship"));
                    UUID id_user1 = UUID.fromString(resultSet.getString("id_user1"));
                    UUID id_user2 = UUID.fromString(resultSet.getString("id_user2"));
                    Timestamp timestamp = resultSet.getTimestamp("date");
                    String status = resultSet.getString("status");

                    LocalDateTime date = timestamp != null ? timestamp.toLocalDateTime() : null;

                    Prietenie friendship = new Prietenie(id_user1, id_user2, FriendRequest.valueOf(status));
                    friendship.setId(id);
                    friendship.setDate(date);
                    friendsOnPage.add(friendship);
                }
            }
        }
        return friendsOnPage;
    }


    @Override
    public Page<Prietenie> findAllFollowingOnPage(Pageable pageable, String userID) {
        try (Connection connection = DataBaseRun.connect()) {
            int totalNumberOfFriendships = count(connection, userID, "Following");
            List<Prietenie> friendsOnPage;
            if (totalNumberOfFriendships > 0) {
                friendsOnPage = findAllOnPage(connection, pageable, userID, "Following");
            } else {
                friendsOnPage = new ArrayList<>();
            }
            return new Page<>(friendsOnPage, totalNumberOfFriendships);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Prietenie> findAllFollowersOnPage(Pageable pageable, String userID) {
        try (Connection connection = DataBaseRun.connect()) {
            int totalNumberOfFriendships = count(connection, userID, "Followers");
            List<Prietenie> friendsOnPage;
            if (totalNumberOfFriendships > 0) {
                friendsOnPage = findAllOnPage(connection, pageable, userID, "Followers");
            } else {
                friendsOnPage = new ArrayList<>();
            }
            return new Page<>(friendsOnPage, totalNumberOfFriendships);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
