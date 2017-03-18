/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.dao;

import at.htlpinkafeld.config.db.HsqlDataSource;
import at.htlpinkafeld.sms.pojo.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 *
 * @author ÐarkHell2
 */
public class UserDaoImpl implements UserDao {

    HsqlDataSource db = new HsqlDataSource();
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db.dataSource());

    @Override
    public User findByName(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);

        String sql = "SELECT * FROM users WHERE name=:name";
        User result = template.queryForObject(sql, params, new UserMapper());

        return result;
    }

    @Override
    public User findByUserId(int userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", userId);

        String sql = "SELECT * FROM users WHERE userid=:userId";

        User result = template.queryForObject(sql, params, new UserMapper());

        return result;
    }

    @Override
    public List<User> findAll() {

        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT * FROM users";

        List<User> result = template.query(sql, params, new UserDaoImpl.UserMapper());

        return result;

    }

    @Override
    public void insertUser(Integer userid, String name, String username, String password, String email, String phonenr) {
        Map<String, Object> params = new HashMap<String, Object>();
        int result;
        String sql = "INSERT INTO users(userid, name, username, password, phonenr, email) VALUES (:userid, :name, :username, :password, :phonenr, :email)";
        params.put("userid", userid);
        params.put("name", name);
        params.put("username", username);
        params.put("password", password);
        params.put("phonenr", phonenr);
        params.put("email", email);

        template.update(sql, params);
    }

    @Override
    public void deleteUser(User userId) {
        System.out.println("DELETE_USER");
    }

    private static final class UserMapper implements RowMapper<User> {

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserNr(rs.getInt("userId"));
            user.setName(rs.getString("name"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setPhoneNr(rs.getString("phonenr"));

            return user;
        }
    }

}
