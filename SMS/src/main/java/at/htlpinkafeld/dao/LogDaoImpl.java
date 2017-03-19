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
    public void insertLog(Log log) {
        Map<String, Object> params = new HashMap<String, Object>();
        
        
        String sql = "INSERT INTO users(logid, timestamp, logcause, logentry) VALUES (:logid, :timestamp, :logcause, :logentry)";
        
        params.put("logid", log.getLogId());
        params.put("timestamp", log.getTimestamp());
        params.put("logcause", log.getLogCause());
        params.put("logentry", log.getLogEntry());

        template.update(sql, params);
        System.out.println("Inserted User");
        
        try {
            db.dataSource().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(LogDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteLog(Integer logid) {
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
