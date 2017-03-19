/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.dao;

import at.htlpinkafeld.sms.pojos.Log;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author DarkHell2
 */
public interface LogDao {
    
    void insertLog(Log log);

    void deleteLog(Integer logId);

    List<Log> findAll();
}
