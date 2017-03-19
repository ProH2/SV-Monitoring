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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public User findByUserId(int userid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", userid);

        String sql = "SELECT * FROM users WHERE userid=:userid";

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
    public void insert(User o) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "INSERT INTO users(userid, name, username, password, phonenr, email) VALUES (:userid, :name, :username, :password, :phonenr, :email)";
        
        params.put("userid", o.getUserNr());
        params.put("name", o.getName());
        params.put("username", o.getUsername());
        params.put("password", o.getPassword());
        params.put("phonenr", o.getPhoneNr());
        params.put("email", o.getEmail());

        template.update(sql, params);
        System.out.println("Inserted User");
        
        try {
            db.dataSource().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Integer userid) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "DELETE FROM users WHERE userid = :userid";
        
        params.put("userid", userid);
        template.update(sql, params);
        
        System.out.println("Deleted Record with EMPID = " + userid );
        
        try {
            db.dataSource().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(User o) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql= "UPDATE duty SET name=:name, username=:username, password=:password, phonenr=:phonenr, email=:email WHERE userid=:userid";
        
        params.put("userid", o.getUserNr());
        params.put("name", o.getName());
        params.put("username", o.getUsername());
        params.put("password", o.getPassword());
        params.put("phonenr", o.getPhoneNr());
        params.put("email", o.getEmail());

        template.update(sql, params);
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
