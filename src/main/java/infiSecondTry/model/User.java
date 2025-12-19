package infiSecondTry.model;

import java.time.LocalDate;

public class User {

    //All in Database

    /**Unique ID for each user*/
    private int idUser;
    /**Firstname of the user*/
    private String firstName;
    /**Lastname of the user*/
    private String lastName;
    /**Birthday of the user*/
    private LocalDate birthday;
    /**SessionID of the user*/
    private double moneySpent;
    /**If the User is deleted*/
    private boolean deleted;

    /**Nedd for JSON Serialization*/
    public User() {}


    /**Standard Konstruktor for Initializing all variables*/
    public User(int idUser, String firstName, String lastName, LocalDate birthday, double moneySpent, boolean deleted){
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.moneySpent = moneySpent;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", sessionID='" + moneySpent + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    public void addMoneyToMoneySpent(double money){
        this.moneySpent += money;
    }


//Standard Getters and Setters

    /**Standard Getter for the ID of the user*/
    public int getIdUser() {
        return idUser;
    }
    /**Standard Getter for the firstname of the user*/
    public String getFirstName() {
        return firstName;
    }
    /**Standard Getter for the lastname of the user*/
    public String getLastName() {
        return lastName;
    }
    /**Standard Getter for the birthday of the user*/
    public LocalDate getBirthday() {
        return birthday;
    }
    /**Standard Getter for the moneySpent of the user*/
    public double getMoneySpent() {
        return moneySpent;
    }
    /**Standard Getter if the user is deleted*/
    public boolean isDeleted() { return deleted; }
    /**Standard Getter setting the user at the given deleted value*/
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
