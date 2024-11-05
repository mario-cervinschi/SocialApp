package repository.database;

import domain.Prietenie;
import domain.validators.PrietenieValidator;
import repository.Repository;
import service.DataBaseRun;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FriendshipDB implements Repository<Long, Prietenie> {

    private PrietenieValidator prietenieValidator;
    private final Map<Long, Prietenie> entities;

    public FriendshipDB(PrietenieValidator prietenieValidator) {
        this.prietenieValidator = prietenieValidator;
        entities = new HashMap<>();

        try(var connection = DataBaseRun.connect()){
            String query = "SELECT * FROM friendship";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id_friendship");
                Long id_user1 = resultSet.getLong("id_user1");
                Long id_user2 = resultSet.getLong("id_user2");
                Timestamp timestamp = resultSet.getTimestamp("date");

                LocalDateTime date = timestamp != null ? timestamp.toLocalDateTime() : null;

                Prietenie prietenie = new Prietenie(id_user1, id_user2);
                prietenie.setId(id);

                entities.put(id, prietenie);

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Optional<Prietenie> findOne(Long aLong) {
        if(aLong == null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        return Optional.ofNullable(entities.get(aLong));
    }

    @Override
    public Iterable<Prietenie> findAll() {
        return entities.values();
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entity cannot be null");
        }
        String query = "INSERT INTO friendship (id_friendship, id_user1, id_user2, date) VALUES (?, ?, ?, ?)";

        try(var connection = DataBaseRun.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2, entity.getIdUser1());
            preparedStatement.setLong(3, entity.getIdUser2());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
            preparedStatement.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<Prietenie> delete(Long aLong) {
        String query = "DELETE FROM friendship WHERE id_friendship = ?";

        try(var connection = DataBaseRun.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(entities.remove(aLong));
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        return Optional.empty();
    }
}
