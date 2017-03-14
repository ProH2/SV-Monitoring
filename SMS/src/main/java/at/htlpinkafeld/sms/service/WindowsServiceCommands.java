/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.service;

/**
 *
 * @author irish
 */
public enum WindowsServiceCommands implements ServiceCommandsInterface{
    CPULOAD,
    UPTIME,
    CLIENTVERSION,
    USEDDISKSPACE,
    MEMUSE;
    
    
    
    
    @Override
  public String toString() {
    switch(this) {
      case CPULOAD: return "CPU LOAD";
      case UPTIME: return "UPTIME";
      case CLIENTVERSION: return "CLIENTVERSION";
      case USEDDISKSPACE: return "USED DISKSPACE";
      case MEMUSE: return "RAM USAGE";
      default: throw new IllegalArgumentException();
    }
  }

    @Override
    public String getCommandName() {
        return "check_nt!" + this.name();
    }
}
