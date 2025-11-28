package InfiFirstTry.database.dummyDataRepo;

import InfiFirstTry.database.UserRepository;
import InfiFirstTry.database.connectionClasses.DummyDataConnection;
import InfiFirstTry.model.User;

import java.util.LinkedList;

public class DummyDataUserRepo implements UserRepository {
    private static DummyDataConnection connection = new DummyDataConnection();
    private final String item = "users.json";

    @Override
    public User[] getAllUsers() {
        LinkedList<User> linkedList = connection.getList(item, User.class);
        LinkedList<User> result = new LinkedList<>();

        for (User user : linkedList) {
            if (!user.isDeleted()) {
                result.add(user);
            }
        }
        User[] users = new User[result.size()];
        return result.toArray(users);
    }

    @Override
    public User[] getUsersWithName(String userName) {
        User[] users = getAllUsers();
        LinkedList<User> result = new LinkedList<>();
        userName =userName.toLowerCase();
        for (User user : users) {
            if (user.getFirstName().toLowerCase().contains(userName)||user.getLastName().toLowerCase().contains(userName)) {
                result.addLast(user);
            }
        }
        return result.toArray(new User[0]);
    }


    @Override
    public User getUserWithId(int userId) {
        for (User user : getAllUsers()) {
            if (user.getIdUser() == userId) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        try {
            LinkedList<User> linkedList = connection.getList(item, User.class);
            linkedList.addLast(user);
            DummyDataConnection.saveChanges(linkedList, item);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean updateUser(User user) {
        try {
            LinkedList<User> linkedList = connection.getList(item, User.class);
            for (User u : linkedList) {
                if (u.getIdUser() == user.getIdUser()) {
                    int index = linkedList.indexOf(u);
                    linkedList.set(index, user);
                    DummyDataConnection.saveChanges(linkedList, item);
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteUser(User user) {
        try {
            LinkedList<User> linkedList = connection.getList(item, User.class);
            for (User u : linkedList) {
                if (u.getIdUser() == user.getIdUser()) {
                    u.setDeleted(true);
                    linkedList.set(linkedList.indexOf(u), u);
                    DummyDataConnection.saveChanges(linkedList, item);
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}