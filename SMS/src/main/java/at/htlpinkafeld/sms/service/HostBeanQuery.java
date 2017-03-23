/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.service;

import at.htlpinkafeld.sms.pojo.Host;
import java.util.List;
import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;

/**
 *
 * @author neume
 */
public class HostBeanQuery extends AbstractBeanQuery<Host> {

    @Override
    protected Host constructBean() {
        return new Host();
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List<Host> loadBeans(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void saveBeans(List<Host> list, List<Host> list1, List<Host> list2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
