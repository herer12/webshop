package infiSecondTry.database;

import infiSecondTry.model.User;

public interface UserRepository {

    /**Reads all Users from the data and generates a User Object for each User in the data
     * Expecpt the deleted Users
     * @return an Array of all User Objects
     */
    User[] getAllUsers();

    /** Reads all Users from the data and checking where the userName is the of the input
     *  and returns an Array of User Objects with the matching userName
     * @param userName Name of the User to search for
     * @return Array of User Objects with the matching userName
     */
    User[] getUsersWithName(String userName);

    /**Reads all Users from the data and checking where the userId is the of the input
     * and returns an User Object with the matching userId
     * @param userId ID of the User to search for
     * @return User Object with the matching userId
     */
    User getUserWithId(int userId);

    /**Add a User to the data
     * @param user User to add to the data
     * @return if the write was successful
     */
    boolean addUser(User user);

    /**Updates the User with the same id in the data
     * @param user User to update to the data
     * @return if the write was successful
     */
    boolean updateUser(User user);

    /**Changes a Attributes that indicates that a User is Deleted
     * Changes the User with the same id in the data
     * @param user User to delete to the data
     * @return if the write was successful
     */
    boolean deleteUser(User user);
}
