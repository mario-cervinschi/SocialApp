package domain;

import java.util.*;

public class Utilizator extends Entity<Long>{
    private String firstName;
    private String lastName;
    List<Utilizator> friends;

    /**
     * Constructor of Utilizator
     * @param firstName
     * First name of Utilizator
     * @param lastName
     * Last name of Utilizator**/
    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        friends = new ArrayList<>();
    }

    /**
     * @return
     * Returns the First Name of Utilizator
     */
    public String getFirstName(){
        return firstName;
    }

    /**
     * @return
     * Returns the Last Name of Utilizator
     */
    public String getLastName(){
        return lastName;
    }

    /**
     *
     * @param fName
     * String to set as First Name of Utilizator
     */
    public void setFirstName(String fName){
        this.firstName = fName;
    }

    /**
     *
     * @param lName
     * String to set as First Name of Utilizator
     */
    public void setLastName(String lName){
        this.lastName = lName;
    }

    /**
     *
     * @param friend
     * Friend to add to an Utilizator.
     */

    public void addFriend(Utilizator friend){
        friends.add(friend);
    }

    /**
     *
     * @param friend
     * Friend to delete from an Utilizator.
     */
    public void removeFriend(Utilizator friend){
        friends.remove(friend);
    }

    /**
     *
     * @return
     * All the friends of an Utilizator.
     */
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
