/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.pojo;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author neume
 */
public class Service {

    public static enum Servicestatus {
        OK, PENDING, WARNING, UNKNOWN, CRITICAL;
    }

    public static enum Servicestate {
        HARDSTATE, SOFTSTATE;
    }

    //Nr von dem Host, auf dem dieser Service läuft;
    private Integer hostnr;
    //Liste von Integers, welche auf die ServiceGroupNr in ServiceGroup verweist;   kann auch leer sein;
    private List<Integer> servicegroups;

    private int servicenr;
    private String hostname;
    private String name;
    private Servicestatus status;
    private LocalDateTime lastChecked;
    private Duration duration;
    private int attempt;
    private String information;

    //in Sekunden; standardmäßig 90
    private int checkIntervall;

    private Servicestate state;
    private LocalDateTime nextCheck;
    private LocalDateTime lastStateChange;
    private boolean flapping;
    private boolean scheduledDowntime;

    private List<Comment> comments;

    public Service() {

    }

    public Service(Integer hostnr, int servicenr, String name, Servicestatus status, LocalDateTime lastChecked, Duration duration, int attempt, String information) {
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

    public Servicestatus getStatus() {
        return status;
    }

    public void setStatus(Servicestatus status) {
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

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getHostnr() {
        return this.hostnr;
    }

    public void setHostnr(Integer hostnr) {
        this.hostnr = hostnr;

    }

    public static Service createServiceFromJson(HashMap<String, Object> map, String hostname) {
//        System.out.println(map);

        Service service = new Service();
        service.setHostname(hostname);
        service.setName((String) map.get("description"));
        service.setInformation((String) map.get("plugin_output"));
//        System.out.println(map.get("last_check"));
        service.setLastChecked(LocalDateTime.ofEpochSecond((long) map.get("last_check"), 0, ZoneOffset.UTC));

        Timestamp stamp = new Timestamp((long) map.get("last_state_change"));
        LocalDateTime last_state_change = stamp.toLocalDateTime();
        //System.out.println(map.get("last_state_change"));

        LocalDateTime now = LocalDateTime.now();
        service.setDuration(Duration.between(last_state_change, now));

        switch ((Integer) map.get("status")) {
            case 2:
                service.setStatus(Service.Servicestatus.OK);
                break;
            default:
                service.setStatus(Service.Servicestatus.UNKNOWN);
        }

        return service;
    }

    @Override
    public String toString() {
        return hostname + " " + information + " " + this.lastChecked + " " + duration + " " + status;
    }

}
