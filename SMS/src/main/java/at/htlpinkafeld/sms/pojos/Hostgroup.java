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

    //Liste von Integer, welche auf die servicenr in Service verweist;
    private List<Integer> hostlist;

    public Hostgroup() {

    }

    public Hostgroup(int hostGroupNr, String name, List<Integer> hostlist) {
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

    public List<Integer> getHostlist() {
        return hostlist;
    }

    public void setHostlist(List<Integer> hostlist) {
        this.hostlist = hostlist;
    }

    
}
