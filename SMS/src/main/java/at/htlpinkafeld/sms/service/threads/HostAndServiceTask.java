/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.service.threads;

import at.htlpinkafeld.sms.service.JSONService;
import java.io.IOException;
import java.util.concurrent.Exchanger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author neume
 */
public class HostAndServiceTask implements Runnable{

    public Exchanger ex;
    
    public HostAndServiceTask(Exchanger e){
        ex = e;
    }
    
    @Override
    public void run() {
        try {
            ex.exchange(JSONService.getHostsFromNagios());
             
            System.out.println(JSONService.getStatusFromAllHosts());
        } catch (InterruptedException ex) {
            Logger.getLogger(HostAndServiceTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HostAndServiceTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
