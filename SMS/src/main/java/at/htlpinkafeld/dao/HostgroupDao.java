/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.dao;

import at.htlpinkafeld.sms.pojos.Hostgroup;
import java.util.List;

/**
 *
 * @author DarkHell2
 */
public interface HostgroupDao {
    
    void insertHostgroup(Hostgroup group);
    
    void deleteHostgroup(Integer hostgroupNr);

    Hostgroup findByHostgroupNr(int hostgroupNr);

    List<Hostgroup> findAll();
    
}
