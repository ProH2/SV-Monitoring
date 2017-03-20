/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.pojos;

import at.htlpinkafeld.sms.pojo.IPojo;
import java.util.List;

/**
 *
 * @author neume
 */
public class Hostgroup implements IPojo{

    private Integer hostGroupNr;
    private String name;

    // List with the hostnames
    private List<String> hostlist;
    private String helplist;

    public Hostgroup() {

    }

    public Hostgroup(Integer hostGroupNr, String name, List<String> hostlist) {
        this.hostGroupNr = hostGroupNr;
        this.name = name;
        this.hostlist = hostlist;
    }

    @Override
    public Integer getId() {
        return hostGroupNr;
    }

    @Override
    public void setId(Integer hostGroupNr) {
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

    public void setHelplist(String helplist) {
        this.helplist = helplist;
    }

    public String getHelplist() {
        return helplist;
    }
    
    

}
