package at.htlpinkafeld.dao;

import at.htlpinkafeld.sms.pojo.User;
import java.util.List;

public interface UserDao {

    void insertUser(Integer userId, String name, String username, String password, String email, String phoneNr);

    void deleteUser(Integer userId);

    User findByName(String name);

    User findByUserId(int userId);

    List<User> findAll();
}
