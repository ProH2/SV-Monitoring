/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.pojos;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author neume
 */
public class Host {

    public static int UP = 1;
    public static int DOWN = 2;
    public static int UNREACHABLE = 3;
    public static int PENDING = 4;

    public int HARDSTATE = 1;
    public int SOFTSTATE = 2;

    
    //Liste von Integers, welche auf die ServiceGroupNr in ServiceGroup verweist;   kann auch leer sein;
    private List<Integer> hostgroups;
    
    private int hostnr;
    private String hostname;
    private int status;
    private LocalDateTime lastChecked;
    private Duration duration;
    private String information;
    
    //in Sekunden; standardmäßig 90
    private int checkIntervall;

    private int currentAttempt;
    private int state;
    private LocalDateTime nextCheck;
    private LocalDateTime lastStateChange;
    private boolean flapping;
    private boolean scheduledDowntime;
    private LocalDateTime lastUpdated;

    private List<Comment> comments;

    public Host() {

    }

    public Host(int hostnr, String hostname, int status, LocalDateTime lastChecked, Duration duration, String information) {
        this.hostnr = hostnr;
        this.hostname = hostname;
        this.status = status;
        this.lastChecked = lastChecked;
        this.duration = duration;
        this.information = information;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(LocalDateTime lastChecked) {
        this.lastChecked = lastChecked;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getHostnr() {
        return hostnr;
    }

    public void setHostnr(int hostnr) {
        this.hostnr = hostnr;
    }

}
