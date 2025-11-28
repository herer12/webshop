package InfiFirstTry.model;

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
    private String sessionID;
    private boolean deleted;

    /**Nedd for JSON Serialization*/
    public User() {}


    /**Standard Konstruktor for Initializing all variables*/
    public User(int idUser, String firstName, String lastName, LocalDate birthday, String sessionID){
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.sessionID = sessionID;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", sessionID='" + sessionID + '\'' +
                ", deleted=" + deleted +
                '}';
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
    /**Standard Getter for the sessionID of the user*/
    public String getSessionID() {
        return sessionID;
    }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
