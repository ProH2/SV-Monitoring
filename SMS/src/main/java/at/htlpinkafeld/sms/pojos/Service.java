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
public class Service {

    public static int OK = 1;
    public static int WARNING = 2;
    public static int UNKNOWN = 3;
    public static int CRITICAL = 4;
    public static int PENDING = 5;

    public int HARDSTATE = 1;
    public int SOFTSTATE = 2;

    //Nr von dem Host, auf dem dieser Service läuft;
    private int hostnr;
    //Liste von Integers, welche auf die ServiceGroupNr in ServiceGroup verweist;   kann auch leer sein;
    private List<Integer> servicegroups;

    private int servicenr;
    private String name;
    private int status;
    private LocalDateTime lastChecked;
    private Duration duration;
    private int attempt;
    private String information;

    //in Sekunden; standardmäßig 90
    private int checkIntervall;

    private int state;
    private LocalDateTime nextCheck;
    private LocalDateTime lastStateChange;
    private boolean flapping;
    private boolean scheduledDowntime;

    private List<Comment> comments;

    public Service() {

    }

    public Service(int hostnr, int servicenr, String name, int status, LocalDateTime lastChecked, Duration duration, int attempt, String information) {
        this.hostnr = hostnr;
        this.servicenr = servicenr;
        this.name = name;
        this.status = status;
        this.lastChecked = lastChecked;
        this.duration = duration;
        this.attempt = attempt;
        this.information = information;
    }

    public int getServicenr() {
        return servicenr;
    }

    public void setServicenr(int servicenr) {
        this.servicenr = servicenr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
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
