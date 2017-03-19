/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.dao;

import at.htlpinkafeld.sms.pojo.Duty;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author DarkHell2
 */
public interface DutyDao {
    
    void insertDuty(Integer dutyId, Integer userId, LocalDateTime startTime, LocalDateTime endtime, Integer notifyart);
    
    void deleteDuty(Integer dutyId);
    
    List<Duty> getDutiesByRange(LocalDateTime startTime, LocalDateTime endTime);

    Duty findByDutyId(int dutyId);

    List<Duty> findByUserId(int userId);

    List<Duty> findAll();
}
