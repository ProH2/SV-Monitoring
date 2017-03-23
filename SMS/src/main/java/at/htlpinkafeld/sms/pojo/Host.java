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
public class Host {

    public static enum Hoststatus {
        UP, DOWN, UNREACHABLE, PENDING;
    }

    public static enum Hoststate {
        HARDSTATE, SOFTSTATE;
    }

    //Liste von Integers, welche auf die HostGroupNr in ServiceGroup verweist;   kann auch leer sein;
    private List<Integer> hostgroups;

    private String hostname;
    private Hoststatus status;
    private LocalDateTime lastChecked;
    private Duration duration;
    private String information;

    //in Sekunden; standardmäßig 90
    private int checkIntervall;

    private String currentAttempt;
    private Hoststate state;
    private LocalDateTime nextCheck;
    private LocalDateTime lastStateChange;
    private boolean flapping;
    private boolean scheduledDowntime;
    private LocalDateTime lastUpdated;

    private List<Comment> comments;

    public Host() {

    }

    public Host(int hostnr, String hostname, Hoststatus status, LocalDateTime lastChecked, Duration duration, String information) {
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

    public Hoststatus getStatus() {
        return status;
    }

    public void setStatus(Hoststatus status) {
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

    public static Host createHostFromJson(HashMap<String, Object> map) {
//        System.out.println(map);

        Host host = new Host();
        host.setHostname((String) map.get("name"));
        host.setInformation((String) map.get("plugin_output"));
//        System.out.println(map.get("last_check"));
        //TODO long-Integer catch
        Object value = map.get("last_check");
        if (value instanceof Long) {
            host.setLastChecked(new Timestamp((long) value).toLocalDateTime());
        } else if (value instanceof Integer) {
            host.setLastChecked(new Timestamp((Integer) value).toLocalDateTime());
        }
        /*
        try {
            host.setLastChecked(LocalDateTime.ofEpochSecond((long) map.get("last_check"), 0, ZoneOffset.UTC));
        } catch (Exception e) {
            host.setLastChecked(LocalDateTime.ofEpochSecond((Integer) map.get("last_check"), 0, ZoneOffset.UTC));
        }
         */

        Timestamp stamp = new Timestamp((long) map.get("last_state_change"));
        LocalDateTime last_state_change = stamp.toLocalDateTime();
        //System.out.println(map.get("last_state_change"));

        LocalDateTime now = LocalDateTime.now();
        host.setDuration(Duration.between(last_state_change, now));

        switch ((Integer) map.get("status")) {
            case 1:
                host.setStatus(Hoststatus.PENDING);
                break;
            case 2:
                host.setStatus(Hoststatus.UP);
                break;
            case 16:
                host.setStatus(Hoststatus.DOWN);
                break;
            default:
                host.setStatus(Hoststatus.UNREACHABLE);
        }

        return host;
    }

    @Override
    public String toString() {

        return hostname + " " + information + " " + this.lastChecked + " " + duration + " " + status;
    }

    public boolean statusChanged(Host h) {
        return !this.status.equals(h.status);
    }

}
