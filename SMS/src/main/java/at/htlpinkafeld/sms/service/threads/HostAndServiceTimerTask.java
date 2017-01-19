/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.service.threads;

import at.htlpinkafeld.sms.pojos.Host;
import at.htlpinkafeld.sms.service.JSONService;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Exchanger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author neume
 */
public class HostAndServiceTimerTask extends TimerTask{

    public Exchanger ex;
    
    @Override
    public void run() {
        ex = new Exchanger();
        HostAndServiceTask task = new HostAndServiceTask(ex);
        new Thread(task).start();
        Object o = null;
        
        try {
            o = ex.exchange(null);
        } catch (InterruptedException ex) {
            Logger.getLogger(HostAndServiceTimerTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONService.setHosts((List)o);
        
        
//        for(Host h: JSONService.getHosts()){
//            System.out.println(h);
//        }
    }
    
}
