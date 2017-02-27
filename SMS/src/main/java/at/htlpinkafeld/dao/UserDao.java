package at.htlpinkafeld.dao;

import at.htlpinkafeld.sms.pojo.User;
import java.util.List;


public interface UserDao {
    User findByName(String name);
    User findByUserId(int userId);
    List<User> findAll();
}