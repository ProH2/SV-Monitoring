/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.dao;

import at.htlpinkafeld.sms.config.db.DataSourceManager;
import at.htlpinkafeld.sms.config.db.HsqlDataSource;
import at.htlpinkafeld.sms.pojo.AccountType;
import at.htlpinkafeld.sms.pojo.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author ÐarkHell2
 */
public class UserDaoImpl implements UserDao {

    /*HsqlDataSource db = HsqlDataSource.getInstance();
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db.dataSource());*/
    DataSourceManager db = DataSourceManager.getInstance();
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db.dataSource());

    @Override
    public User findByName(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);

        String sql = "SELECT * FROM User WHERE PersName=:PersName";
        User result = template.queryForObject(sql, params, new UserMapper());

        return result;
    }

    @Override
    public User findByUserId(int userid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", userid);

        String sql = "SELECT * FROM User WHERE usernr=:userid";

        User result = template.queryForObject(sql, params, new UserMapper());

        return result;
    }

    @Override
    public List<User> findAll() {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT * FROM User";

        List<User> result = template.query(sql, params, new UserDaoImpl.UserMapper());

        return result;
    }

    @Override
    public void insert(User user) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "INSERT INTO User(usernr, persname, username, password, email, accounttype, disabled) VALUES (:userid, :name, :username, :password, :email, :accounttype, :disabled)";

        params.put("userid", user.getId());
        params.put("name", user.getName());
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        params.put("accounttype", user.getAccountType().name());
        params.put("email", user.getEmail());
        params.put("disabled", user.isDisabled());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(sql, new AbstractSqlParameterSource() {
            @Override
            public boolean hasValue(String paramName) {
                return params.containsKey(paramName);
            }

            @Override
            public Object getValue(String paramName) throws IllegalArgumentException {
                return params.get(paramName);
            }

        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void delete(Integer userid) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "DELETE FROM User WHERE usernr = :userid";

        params.put("userid", userid);
        template.update(sql, params);

    }

    @Override
    public void update(User o) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "UPDATE User SET persname=:name, username=:username, password=:password, email=:email, disabled=:disabled, accounttype=:accounttype WHERE usernr=:userid";

        params.put("userid", o.getId());
        params.put("name", o.getName());
        params.put("username", o.getUsername());
        params.put("password", o.getPassword());
        params.put("accounttype", o.getAccountType().name());
        params.put("email", o.getEmail());
        params.put("disabled", o.isDisabled());

        template.update(sql, params);
    }

    private static final class UserMapper implements RowMapper<User> {

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("userNr"));
            user.setName(rs.getString("persname"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setAccountType(AccountType.valueOf(rs.getString("accounttype")));
            user.setDisabled(rs.getBoolean("disabled"));

            return user;
        }
    }

}
