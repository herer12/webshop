package InfiFirstTry.database.connectionClasses;

import InfiFirstTry.model.User;
import java.util.LinkedList;

public interface UserDataSource {
    LinkedList<User> getUsers();
    void saveUsers(LinkedList<User> users);
}
