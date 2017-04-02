/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.dao;

import at.htlpinkafeld.sms.pojo.Hostgroup;

/**
 *
 * @author DarkHell2
 */
public interface HostgroupDao extends BaseDao<Hostgroup>{

    Hostgroup findByHostgroupNr(int hostgroupNr);
    
}
