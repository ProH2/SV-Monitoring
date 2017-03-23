package at.htlpinkafeld.sms.dao;

import at.htlpinkafeld.sms.pojo.User;
import java.util.List;

public interface UserDao extends BaseDao<User>{

    User findByName(String name);

    User findByUserId(int userId);
}
