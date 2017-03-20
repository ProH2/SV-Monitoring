/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.pojos;

import at.htlpinkafeld.sms.pojo.IPojo;
import java.sql.Timestamp;

/**
 *
 * @author DarkHell2
 */
public class Log implements IPojo{
    private Integer logId;
    private Timestamp timestamp;
    private String logCause;
    private String logEntry;
    

    public Log(Integer logId, Timestamp timestamp, String logCause, String logEntry) {
        this.logId = logId;
        this.timestamp = timestamp;
        this.logCause = logCause;
        this.logEntry = logEntry;
    }

    public Log() {
    }

    @Override
    public Integer getId() {
        return logId;
    }

    @Override
    public void setId(Integer logId) {
        this.logId = logId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getLogCause() {
        return logCause;
    }

    public void setLogCause(String logCause) {
        this.logCause = logCause;
    }

    public String getLogEntry() {
        return logEntry;
    }

    public void setLogEntry(String logEntry) {
        this.logEntry = logEntry;
    }
    
    
}
