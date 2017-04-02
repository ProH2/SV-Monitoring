/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.pojo;

import java.time.LocalDateTime;

/**
 *
 * @author DarkHell2
 */
public class Log implements IPojo{
    private Integer logId;
    private LocalDateTime timestamp;
    private String logCause;
    private String logEntry;
    

    public Log(Integer logId, LocalDateTime timestamp, String logCause, String logEntry) {
        this.logId = logId;
        this.timestamp = timestamp;
        this.logCause = logCause;
        this.logEntry = logEntry;
    }

    public Log() {
    }

    public Log(LocalDateTime timestamp, String logCause, String logEntry) {
        this.timestamp = timestamp;
        this.logCause = logCause;
        this.logEntry = logEntry;
    }
    
    

    @Override
    public Integer getId() {
        return logId;
    }

    @Override
    public void setId(Integer logId) {
        this.logId = logId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
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
