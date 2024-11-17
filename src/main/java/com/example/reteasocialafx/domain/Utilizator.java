package com.example.reteasocialafx.domain;

import java.util.*;

public class Utilizator extends Entity<Long>{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    List<Utilizator> friends;

    public Utilizator(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        friends = new ArrayList<>();
        this.email = email;
    }

    public Utilizator(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        friends = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFriends(List<Utilizator> friends) {
        this.friends = friends;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setFirstName(String fName){
        this.firstName = fName;
    }

    public void setLastName(String lName){
        this.lastName = lName;
    }

    public void addFriend(Utilizator friend){
        friends.add(friend);
    }

    public void removeFriend(Utilizator friend){
        friends.remove(friend);
    }

    public List<Utilizator> getFriends(){
        return friends;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "fName='" + firstName + '\'' +
                ", lName='" + lastName + '\'' +
                ", prieteni=" + friends +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilizator that = (Utilizator) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(friends, that.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, friends);
    }
}
