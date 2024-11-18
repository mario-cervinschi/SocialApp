package com.example.reteasocialafx.repository.database;

import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.domain.validators.UtilizatorValidator;
import com.example.reteasocialafx.repository.Repository;
import com.example.reteasocialafx.service.DataBaseRun;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class UserDB implements Repository<Long, Utilizator> {

    UtilizatorValidator utilizatorValidator;

    public UserDB(UtilizatorValidator utilizatorValidator) {
        this.utilizatorValidator = utilizatorValidator;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Map<Long, Utilizator> entities = new HashMap<>();
        try(var connection = DataBaseRun.connect()){
            String query = "SELECT * FROM users";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("user_id");
                String firstName= resultSet.getString("firstname");
                String lastName= resultSet.getString("lastname");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                Utilizator user = new Utilizator(firstName, lastName, email, password);
                user.setId(id);

                entities.put(user.getId(), user);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return entities.values();
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        if(entity.getId() == null) {
            throw new IllegalArgumentException("NULL USER");
        }

        String query = "INSERT INTO users(lastname, firstname, user_id, email, password) VALUES (?, ?, ?, ?, ?)";

        try(var connection = DataBaseRun.connect()){
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, entity.getLastName());
            ps.setString(2, entity.getFirstName());
            ps.setLong(3, entity.getId());
            ps.setString(4, entity.getEmail());
            ps.setString(5, entity.getPassword());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<Utilizator> delete(Long aLong) {
        String query = "DELETE FROM users WHERE user_id = ?";

        Utilizator userToDelete = null;
        for(var user : findAll()) {
            if(Objects.equals(user.getId(), aLong)) {
                userToDelete = user;
            }
        }

        try(var connection = DataBaseRun.connect()){
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, aLong);
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(userToDelete);
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {

        if (entity.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        String query = "UPDATE users SET firstname = ?, lastname = ?, email = ?, password = ? WHERE user_id = ?";

        try (var connection = DataBaseRun.connect()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
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

    @Override
    public Optional<Utilizator> findOne(Long aLong) {
        Utilizator user = null;
        try(var connection = DataBaseRun.connect()){
            String query = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("user_id");
                String firstName= resultSet.getString("firstname");
                String lastName= resultSet.getString("lastname");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                user = new Utilizator(firstName, lastName, email, password);
                user.setId(id);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }
}
