/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.pojo;

import java.util.Date;

/**
 *
 * @author DarkHell2
 */
public class Duty {
    private Integer dutyId;
    private int userId;
    private Date startTime;
    private Date endTime;
    private int notifyArt;

    public Duty(Integer dutyId, int userId, Date startTime, Date endTime, int notifyArt) {
        this.dutyId = dutyId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notifyArt = notifyArt;
    }

    public Duty() {
        
    }

    public Integer getDutyID() {
        return dutyId;
    }

    public void setDutyID(Integer dutyID) {
        this.dutyId = dutyID;
    }

    public int getUserID() {
        return userId;
    }

    public void setUserID(int userID) {
        this.userId = userID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getNotifyArt() {
        return notifyArt;
    }

    public void setNotifyArt(int notifyArt) {
        this.notifyArt = notifyArt;
    }

    @Override
    public String toString() {
	return "<br> DutyId: " +dutyId+", UserId: "+userId+" Start-Time: "+startTime+" End-Time: "+endTime+" Notification: "+notifyArt+"<br>";
    }

}