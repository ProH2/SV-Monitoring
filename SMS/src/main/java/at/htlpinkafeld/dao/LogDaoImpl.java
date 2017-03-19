/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.dao;

import at.htlpinkafeld.config.db.HsqlDataSource;
import at.htlpinkafeld.sms.pojos.Log;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author DarkHell2
 */
public class LogDaoImpl implements LogDao{
    HsqlDataSource db = new HsqlDataSource();
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db.dataSource());
    
    @Override
    public void insert(Log o) {
        Map<String, Object> params = new HashMap<String, Object>();
        
        
        String sql = "INSERT INTO users(logid, timestamp, logcause, logentry) VALUES (:logid, :timestamp, :logcause, :logentry)";
        
        params.put("logid", o.getLogId());
        params.put("timestamp", o.getTimestamp());
        params.put("logcause", o.getLogCause());
        params.put("logentry", o.getLogEntry());

        template.update(sql, params);
        System.out.println("Inserted User");
        
        try {
            db.dataSource().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(LogDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Integer logid) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "DELETE FROM log WHERE logid = :logid";
        
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
        String sql = "SELECT * FROM log";

        List<Log> result = template.query(sql, params, new LogDaoImpl.LogMapper());

        return result;
    }

    @Override
    public void update(Log o) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql= "UPDATE log SET timestamp=:timestamp, logcause=:logcause, logentry=:logentry WHERE logid=:logid";
        
        params.put("logid", o.getLogId());
        params.put("timestamp", o.getTimestamp());
        params.put("logcause", o.getLogCause());
        params.put("logentry", o.getLogEntry());

        template.update(sql, params);
    }
    
    private static final class LogMapper implements RowMapper<Log> {

    public Log mapRow(ResultSet rs, int rowNum) throws SQLException {
        Log log = new Log();
        log.setLogId(rs.getInt("logId"));
        log.setTimestamp(rs.getTimestamp("timestamp"));
        log.setLogCause(rs.getString("logcause"));
        log.setLogEntry(rs.getString("logentry"));

        return log;
        }
    }
}
