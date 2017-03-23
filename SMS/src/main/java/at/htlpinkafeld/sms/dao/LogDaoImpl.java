/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.dao;

import at.htlpinkafeld.sms.config.db.DataSourceManager;
import at.htlpinkafeld.sms.config.db.HsqlDataSource;
import at.htlpinkafeld.sms.pojo.Log;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author DarkHell2
 */
public class LogDaoImpl implements LogDao {

    /*HsqlDataSource db =  HsqlDataSource.getInstance();
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db.dataSource());*/

    DataSourceManager db = DataSourceManager.getInstance();
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db.dataSource());

    @Override
    public void insert(Log log) {
        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "INSERT INTO Log(logid, timestamp, logcause, logentry) VALUES (:logid, :timestamp, :logcause, :logentry)";

        params.put("logid", log.getId());
        params.put("timestamp", Timestamp.valueOf(log.getTimestamp()));
        params.put("logcause", log.getLogCause());
        params.put("logentry", log.getLogEntry());

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
        log.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void delete(Integer logid) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "DELETE FROM Log WHERE logid = :logid";

        params.put("logid", logid);
        template.update(sql, params);

        try {
            db.dataSource().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(LogDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public List<Log> findAll() {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT * FROM Log";

        List<Log> result = template.query(sql, params, new LogDaoImpl.LogMapper());

        return result;
    }

    @Override
    public void update(Log o) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "UPDATE Log SET timestamp=:timestamp, logcause=:logcause, logentry=:logentry WHERE logid=:logid";

        params.put("logid", o.getId());
        params.put("timestamp", Timestamp.valueOf(o.getTimestamp()));
        params.put("logcause", o.getLogCause());
        params.put("logentry", o.getLogEntry());

        template.update(sql, params);
    }

    private static final class LogMapper implements RowMapper<Log> {

        public Log mapRow(ResultSet rs, int rowNum) throws SQLException {
            Log log = new Log();
            log.setId(rs.getInt("logId"));
            log.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            log.setLogCause(rs.getString("logcause"));
            log.setLogEntry(rs.getString("logentry"));

            return log;
        }
    }
}
