/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.dao;

import at.htlpinkafeld.sms.pojo.Duty;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author DarkHell2
 */
public interface DutyDao extends BaseDao<Duty>{
    
    List<Duty> getDutiesByRange(LocalDateTime startTime, LocalDateTime endTime);
    
    List<Duty> getDutiesByRange(Date starttime, Date endtime);
    
    List<Duty> getDutiesInTime(LocalDateTime stime);


    Duty findByDutyId(int dutyId);

    List<Duty> findByUserId(int userId);

}
