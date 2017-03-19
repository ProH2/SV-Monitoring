/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.dao;

import at.htlpinkafeld.config.db.HsqlDataSource;
import at.htlpinkafeld.sms.pojo.Duty;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DarkHell2
 */
@Repository
public class DutyDaoImpl implements DutyDao {
    private static UserDao userdao = new UserDaoImpl();
    HsqlDataSource db = new HsqlDataSource();
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db.dataSource());

    @Override
    public Duty findByDutyId(int dutyid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dutyid", dutyid);

        String sql = "SELECT * FROM duty WHERE dutyid=:dutyid";

        Duty result = template.queryForObject(sql, params, new DutyMapper());
        
        return result;
    }

    @Override
    public List<Duty> findAll() {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "SELECT * FROM duty";

        List<Duty> result = template.query(sql, params, new DutyMapper());
        return result;
    }

    @Override
    public List<Duty> findByUserId(int userid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", userid);
        
        String sql = "SELECT * FROM duty WHERE userid:=userid";

        List<Duty> result = template.query(sql, params, new DutyMapper());
        return result;
    }

    /*@Override
    public void insertDuty(Integer dutyid, Integer userid, Date starttime, Date endtime, Integer notifyart) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "INSERT INTO duty(dutyid, userid, starttime, endtime, notifyart) VALUES (:dutyid, :userid, :starttime, :endtime, :notifyart)";
        
        params.put("dutyid", dutyid);
        params.put("userid", userid);
        params.put("starttime", starttime);
        params.put("endtime", endtime);
        params.put("notifyart", notifyart);

        template.update(sql, params);
        System.out.println("Inserted Duty");
    }*/
    
    @Override
    public void insert(Duty o) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "INSERT INTO duty(dutyid, userid, starttime, endtime) VALUES (:dutyid, :userid, :starttime, :endtime)";
        
        params.put("dutyid", o.getDutyID());
        params.put("userid", o.getUser().getUserNr());
        params.put("starttime", o.getStartTime());
        params.put("endtime", o.getEndTime());

        template.update(sql, params);
        System.out.println("Inserted Duty");
        
        try {
            db.dataSource().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(DutyDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Integer dutyid) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "DELETE FROM duty WHERE dutyid = :dutyid";
        
        params.put("dutyid", dutyid);
        template.update(sql, params);
        
        try {
            db.dataSource().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(DutyDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Duty> getDutiesByRange(LocalDateTime starttime, LocalDateTime endtime) {
        Date startt=new Date(starttime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        Date endt=new Date(endtime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        
        Map<String, Object> params = new HashMap<String, Object>();
        
        String sql = "SELECT * FROM duty WHERE starttime BETWEEN :starttime AND :endtime";
        params.put("starttime", startt);
        params.put("endtime", endt);
        
        List<Duty> result = template.query(sql, params, new DutyMapper());
        return result;
        
    }

    @Override
    public void update(Duty o) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql= "UPDATE duty SET userid=:userid, starttime=:starttime, endtime=:endtime WHERE dutyid=:dutyid";
        
        params.put("dutyid", o.getDutyID());
        params.put("userid", o.getUser().getUserNr());
        params.put("starttime", o.getStartTime());
        params.put("endtime", o.getEndTime());

        template.update(sql, params);
        
        try {
            db.dataSource().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(DutyDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static final class DutyMapper implements RowMapper<Duty> {

        public Duty mapRow(ResultSet rs, int rowNum) throws SQLException {
            Duty duty = new Duty();
            duty.setDutyID(rs.getInt("dutyId"));
            duty.setUser(userdao.findByUserId(rs.getInt("userid")));
            duty.setStartTime(rs.getDate("startTime"));
            duty.setEndTime(rs.getDate("endTime"));
            
            return duty;
        }
    }
}
