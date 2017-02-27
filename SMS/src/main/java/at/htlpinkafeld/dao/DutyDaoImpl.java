/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.dao;


import at.htlpinkafeld.sms.pojo.Duty;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 *
 * @author DarkHell2
 */

@Repository
public class DutyDaoImpl implements DutyDao {

	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	@Override
	public Duty findByDutyId(int dutyId) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("name", dutyId);
        
            String sql = "SELECT * FROM duty WHERE dutyid=:dutyId";
		
            Duty result = namedParameterJdbcTemplate.queryForObject(sql, params, new DutyMapper());
                    
        //new BeanPropertyRowMapper(Customer.class));
            return result;
	}

	@Override
	public List<Duty> findAll (){		
            Map<String, Object> params = new HashMap<String, Object>();
            String sql = "SELECT * FROM duty";
		
            List<Duty> result = namedParameterJdbcTemplate.query(sql, params, new DutyMapper());
            return result;
	}

    @Override
    public List<Duty> findByUserId(int userId) {
        Map<String, Object> params = new HashMap<String, Object>();
            String sql = "SELECT * FROM duty WHERE userid:=userId";
		
            List<Duty> result = namedParameterJdbcTemplate.query(sql, params, new DutyMapper());
            return result;
	}
   

        private static final class DutyMapper implements RowMapper<Duty> {

		public Duty mapRow(ResultSet rs, int rowNum) throws SQLException {
			Duty duty = new Duty();
			duty.setDutyID(rs.getInt("dutyId"));
			duty.setUserID(rs.getInt("userId"));
                        duty.setStartTime(rs.getDate("startTime"));
                        duty.setEndTime(rs.getDate("endTime"));
                        duty.setNotifyArt(rs.getInt("notifyArt"));
                        
			return duty;
		}
	}
}