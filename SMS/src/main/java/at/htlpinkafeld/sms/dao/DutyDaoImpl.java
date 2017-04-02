/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.dao;

import at.htlpinkafeld.sms.config.db.DataSourceManager;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DarkHell2
 */
@Repository
public class DutyDaoImpl implements DutyDao {

    private static UserDao userdao = new UserDaoImpl();
    /*HsqlDataSource db = HsqlDataSource.getInstance();
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db.dataSource());*/

    DataSourceManager db = DataSourceManager.getInstance();
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db.dataSource());

    @Override
    public Duty findByDutyId(int dutyid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dutyid", dutyid);

        String sql = "SELECT * FROM Duty WHERE dutyid=:dutyid";

        Duty result = template.queryForObject(sql, params, new DutyMapper());

        return result;
    }

    @Override
    public List<Duty> findAll() {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "SELECT * FROM Duty";

        List<Duty> result = template.query(sql, params, new DutyMapper());
        return result;
    }

    @Override
    public List<Duty> findByUserId(int userid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", userid);

        String sql = "SELECT * FROM Duty WHERE userid:=userid";

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
    }*/
    @Override
    public void insert(Duty duty) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "INSERT INTO Duty(dutyid, userid, starttime, endtime) VALUES (:dutyid, :userid, :starttime, :endtime)";

        params.put("dutyid", duty.getId());
        params.put("userid", duty.getUser().getId());
        params.put("starttime", new Timestamp(duty.getStartTime().getTime()));
        params.put("endtime", new Timestamp(duty.getEndTime().getTime()));

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
        duty.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void delete(Integer dutyid) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "DELETE FROM Duty WHERE dutyid = :dutyid";

        params.put("dutyid", dutyid);
        template.update(sql, params);
    }

    @Override
    public List<Duty> getDutiesByRange(LocalDateTime starttime, LocalDateTime endtime) {
        Date startt = new Date(starttime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        Date endt = new Date(endtime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "SELECT * FROM Duty WHERE starttime BETWEEN :starttime AND :endtime";
        params.put("starttime", startt);
        params.put("endtime", endt);

        List<Duty> result = template.query(sql, params, new DutyMapper());
        return result;
    }

    @Override
    public List<Duty> getDutiesInTime(LocalDateTime stime) {
        Date time = new Date(stime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "SELECT * FROM Duty WHERE :time BETWEEN starttime AND endtime";
        params.put("time", time);

        List<Duty> result = template.query(sql, params, new DutyMapper());
        return result;
    }

    @Override
    public List<Duty> getDutiesByRange(Date starttime, Date endtime) {
        Map<String, Object> params = new HashMap<String, Object>();

        String sql = "SELECT * FROM Duty WHERE starttime BETWEEN :starttime AND :endtime OR endtime BETWEEN :starttime AND :endtime OR starttime<:starttime AND endTime>=:endtime ORDER BY starttime";
        params.put("starttime", starttime);
        params.put("endtime", endtime);

        List<Duty> result = template.query(sql, params, new DutyMapper());
        return result;
    }

    @Override
    public void update(Duty o) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "UPDATE Duty SET userid=:userid, starttime=:starttime, endtime=:endtime WHERE dutyid=:dutyid";

        params.put("dutyid", o.getId());
        params.put("userid", o.getUser().getId());
        params.put("starttime", o.getStartTime());
        params.put("endtime", o.getEndTime());

        template.update(sql, params);
    }

    private static final class DutyMapper implements RowMapper<Duty> {

        public Duty mapRow(ResultSet rs, int rowNum) throws SQLException {
            Duty duty = new Duty();
            duty.setId(rs.getInt("dutyId"));
            duty.setUser(userdao.findByUserId(rs.getInt("userid")));
            duty.setStartTime(new java.util.Date(rs.getTimestamp("startTime").getTime()));
            duty.setEndTime(new java.util.Date(rs.getTimestamp("endTime").getTime()));

            return duty;
        }
    }
}
