/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.pojos;

import java.util.List;

/**
 *
 * @author neume
 */
public class Hostgroup {

    private int hostGroupNr;
    private String name;

    // List with the hostnames
    private List<String> hostlist;

    public Hostgroup() {

    }

    public Hostgroup(int hostGroupNr, String name, List<String> hostlist) {
        this.hostGroupNr = hostGroupNr;
        this.name = name;
        this.hostlist = hostlist;
    }

    public int getHostGroupNr() {
        return hostGroupNr;
    }

    public void setHostGroupNr(int hostGroupNr) {
        this.hostGroupNr = hostGroupNr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getHostlist() {
        return hostlist;
    }

    public void setHostlist(List<String> hostlist) {
        this.hostlist = hostlist;
    }

}
