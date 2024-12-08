package com.example.reteasocialafx.repository.database;

import com.example.reteasocialafx.domain.Message;
import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.repository.Repository;
import com.example.reteasocialafx.service.DataBaseRun;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDB implements Repository<UUID, Message> {

    private final Repository<UUID, Utilizator> userRepo;

    public MessageDB(Repository<UUID, Utilizator> userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Optional<Message> findOne(UUID aLong) {
        Message message = null;
        if(aLong != null) {
            try(var connection = DataBaseRun.connect()){
                String query = "SELECT * FROM messages WHERE id_message = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, aLong.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    UUID id = UUID.fromString(resultSet.getString("id_message"));
                    UUID id_user_from = UUID.fromString(resultSet.getString("id_user_from"));
                    UUID id_user_to = UUID.fromString(resultSet.getString("id_user_to"));
                    String text_message = resultSet.getString("message");
                    Timestamp timestamp = resultSet.getTimestamp("data");
                    String idMessageReplyToString = resultSet.getString("id_message_reply_to");
                    UUID id_message_reply_to = idMessageReplyToString != null ? UUID.fromString(idMessageReplyToString) : null;

                    LocalDateTime date = timestamp != null ? timestamp.toLocalDateTime() : null;

                    message = new Message(text_message, id_user_from, id_user_to, date);
                    message.setId(id);
                    message.setReply(findOne(id_message_reply_to).orElse(null));
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return Optional.ofNullable(message);
    }

    @Override
    public Iterable<Message> findAll() {
        Map<UUID, Message> entities = new HashMap<>();
        try(var connection = DataBaseRun.connect()){
            String query = "SELECT * FROM messages";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id_message"));
                UUID id_user_from = UUID.fromString(resultSet.getString("id_user_from"));
                UUID id_user_to = UUID.fromString(resultSet.getString("id_user_to"));
                String text_message = resultSet.getString("message");
                Timestamp timestamp = resultSet.getTimestamp("data");
                UUID id_message_reply_to = UUID.fromString(resultSet.getString("id_message_reply_to"));

                LocalDateTime date = timestamp != null ? timestamp.toLocalDateTime() : null;

                Message message = new Message(text_message, id_user_from, id_user_to, date);
                message.setId(id);
                message.setReply(findOne(id_message_reply_to).orElse(null));

                entities.put(id, message);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return entities.values();
    }

    public List<Message> getConversation(UUID fromId, UUID toId) {
        List<Message> conversation = new ArrayList<>();
        String query = "SELECT * FROM messages WHERE (id_user_from = ? AND id_user_to = ?) OR (id_user_from = ? AND id_user_to = ?) ORDER BY data ASC";

        try (var connection = DataBaseRun.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, fromId.toString());
            preparedStatement.setString(2, toId.toString());
            preparedStatement.setString(3, toId.toString());
            preparedStatement.setString(4, fromId.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id_message"));
                UUID id_user_from = UUID.fromString(resultSet.getString("id_user_from"));
                UUID id_user_to = UUID.fromString(resultSet.getString("id_user_to"));
                String text_message = resultSet.getString("message");
                Timestamp timestamp = resultSet.getTimestamp("data");
                String idMessageReplyToString = resultSet.getString("id_message_reply_to");
                UUID id_message_reply_to = idMessageReplyToString != null ? UUID.fromString(idMessageReplyToString) : null;

                LocalDateTime date = timestamp != null ? timestamp.toLocalDateTime() : null;
                Message message = new Message(text_message, id_user_from, id_user_to, date);
                message.setId(id);
                message.setReply(findOne(id_message_reply_to).orElse(null)); // Can be optimized too

                conversation.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conversation;
    }

    @Override
    public Optional<Message> save(Message entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entity cannot be null");
        }
        String query = "INSERT INTO messages (id_message, id_user_from, id_user_to, message, data, id_message_reply_to) VALUES (?, ?, ?, ?, ?, ?)";

        try(var connection = DataBaseRun.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, entity.getId().toString());
            preparedStatement.setString(2, entity.getFrom().toString());
            preparedStatement.setString(3, entity.getTo().toString());
            preparedStatement.setString(4, entity.getMessage());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(entity.getData()));
            if (entity.getReply() != null) {
                preparedStatement.setString(6, entity.getReply().getId().toString());
            } else {
                preparedStatement.setNull(6, Types.VARCHAR);  // Use the correct type for the ID column
            }
            preparedStatement.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<Message> delete(UUID aLong) {
        String query = "DELETE FROM messages WHERE id_message = ?";

        Message messageToDelete = null;
        for(var message : findAll()){
            if(Objects.equals(message.getId(), aLong)){
                messageToDelete = message;
            }
        }

        try(var connection = DataBaseRun.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, aLong.toString());
            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(messageToDelete);
    }

    @Override
    public Optional<Message> update(Message entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Message ID cannot be null");
        }

        String query = "UPDATE messages SET id_message = ?, id_user_from = ?, id_user_to = ?, message = ?, data = ?, id_message_reply_to = ? WHERE id_message = ?";

        try (var connection = DataBaseRun.connect()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, entity.getId().toString());
            ps.setString(2, entity.getFrom().toString());
            ps.setString(3, entity.getTo().toString());
            ps.setString(4, entity.getMessage());
            ps.setTimestamp(5, Timestamp.valueOf(entity.getData()));
            ps.setString(6, entity.getReply().getId().toString());

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

