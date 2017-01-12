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
public class Servicegroup {

    private int serviceGroupNr;
    private String name;

    //Liste von Integer, welche auf die servicenr in Service verweist;
    private List<Integer> servicelist;

    public Servicegroup() {

    }

    public Servicegroup(int serviceGroupNr, String name, List<Integer> servicelist) {
        this.serviceGroupNr = serviceGroupNr;
        this.name = name;
        this.servicelist = servicelist;
    }

    public int getServiceGroupNr() {
        return serviceGroupNr;
    }

    public void setServiceGroupNr(int serviceGroupNr) {
        this.serviceGroupNr = serviceGroupNr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getServicelist() {
        return servicelist;
    }

    public void setServicelist(List<Integer> servicelist) {
        this.servicelist = servicelist;
    }

    
}
