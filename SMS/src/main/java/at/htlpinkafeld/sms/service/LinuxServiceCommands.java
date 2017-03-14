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
public enum LinuxServiceCommands implements ServiceCommandsInterface {
    /**
     *
     */
    check_http,
    check_local_load,
    check_local_disk,
    check_ping,
    check_dhcp,
    check_local_users,
    check_ssh,
    check_ftp;

    @Override
    public String toString() {
        switch (this) {
            case check_http:
                return "HTTP STATUS";
            case check_local_load:
                return "CPU LOAD";
            case check_local_disk:
                return "USED DISKSPACE";
            case check_ping:
                return "PING";
            case check_dhcp:
                return "DHCP STATUS";
            case check_local_users:
                return "LOGGED IN USERS";
            case check_ssh:
                return "SSH STATUS";
            case check_ftp:
                return "FTP STATUS";
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public String getCommandName() {
        return this.name();
    }
}
