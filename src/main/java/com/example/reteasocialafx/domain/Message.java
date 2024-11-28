package com.example.reteasocialafx.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Message extends Entity<UUID>{

    private String message;
    private UUID from;
    private UUID to;
    private LocalDateTime data;
    private Message reply;

    public Message(String message, UUID from, UUID to) {
        this.message = message;
        this.from = from;
        this.to = to;
        this.data = LocalDateTime.now();
        this.reply = null;
        this.setId(UUID.randomUUID());
    }

    public Message(String message, UUID from, UUID to, LocalDateTime data) {
        this.message = message;
        this.from = from;
        this.to = to;
        this.data = data;
        this.reply = null;
        this.setId(UUID.randomUUID());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getFrom() {
        return from;
    }

    public void setFrom(UUID from) {
        this.from = from;
    }

    public UUID getTo() {
        return to;
    }

    public void setTo(UUID to) {
        this.to = to;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Message message1 = (Message) o;
        return Objects.equals(message, message1.message) && Objects.equals(from, message1.from) && Objects.equals(to, message1.to) && Objects.equals(data, message1.data) && Objects.equals(reply, message1.reply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), message, from, to, data, reply);
    }

    @Override
    public String toString() {
        return message;
    }
}
