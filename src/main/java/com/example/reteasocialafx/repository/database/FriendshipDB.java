package com.example.reteasocialafx.repository.database;


import com.example.reteasocialafx.domain.FriendRequest;
import com.example.reteasocialafx.domain.Prietenie;
import com.example.reteasocialafx.domain.validators.PrietenieValidator;
import com.example.reteasocialafx.repository.Repository;
import com.example.reteasocialafx.service.DataBaseRun;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class FriendshipDB implements Repository<Long, Prietenie> {

    private PrietenieValidator prietenieValidator;

    public FriendshipDB(PrietenieValidator prietenieValidator) {
        this.prietenieValidator = prietenieValidator;
    }

    @Override
    public Optional<Prietenie> findOne(Long aLong) {
        Prietenie friendship = null;
        try(var connection = DataBaseRun.connect()){
            String query = "SELECT * FROM friendship where id_friendship = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id_friendship");
                Long id_user1 = resultSet.getLong("id_user1");
                Long id_user2 = resultSet.getLong("id_user2");
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
        Map<Long, Prietenie> entities = new HashMap<>();
        try(var connection = DataBaseRun.connect()){
            String query = "SELECT * FROM friendship";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id_friendship");
                Long id_user1 = resultSet.getLong("id_user1");
                Long id_user2 = resultSet.getLong("id_user2");
                Timestamp timestamp = resultSet.getTimestamp("date");
                String status = resultSet.getString("status");

                LocalDateTime date = timestamp != null ? timestamp.toLocalDateTime() : null;

                Prietenie friendship = new Prietenie(id_user1, id_user2, FriendRequest.valueOf(status));
                friendship.setId(id);

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
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2, entity.getIdUser1());
            preparedStatement.setLong(3, entity.getIdUser2());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
            preparedStatement.setString(5, entity.getFriendRequest().name());
            preparedStatement.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<Prietenie> delete(Long aLong) {
        String query = "DELETE FROM friendship WHERE id_friendship = ?";

        Prietenie friendshipToDelete = null;
        for(var friendship : findAll()){
            if(Objects.equals(friendship.getId(), aLong)){
                friendshipToDelete = friendship;
            }
        }

        try(var connection = DataBaseRun.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, aLong);
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
            ps.setLong(1, entity.getIdUser1());
            ps.setLong(2, entity.getIdUser2());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
            ps.setString(4, entity.getFriendRequest().name());
            ps.setLong(5, entity.getId());

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
