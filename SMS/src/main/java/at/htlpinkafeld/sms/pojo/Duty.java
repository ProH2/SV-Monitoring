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
public class Duty implements IPojo{

    private Integer dutyId;
    private User user;
    private Date startTime;
    private Date endTime;

    public Duty(Integer dutyId, User user, Date startTime, Date endTime) {
        this.dutyId = dutyId;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Duty() {

    }

    @Override
    public Integer getId() {
        return dutyId;
    }

    public void setId(Integer dutyID) {
        this.dutyId = dutyID;
    }

    public Integer getDutyId() {
        return dutyId;
    }

    public void setDutyId(Integer dutyId) {
        this.dutyId = dutyId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "Duty{" + "dutyId=" + dutyId + ", user=" + user + ", startTime=" + startTime + ", endTime=" + endTime + '}';
    }


}
