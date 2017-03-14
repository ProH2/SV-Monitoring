/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 *
 * @author irish
 */
public class AddHostsAndServices_Service {
    
    /*
    public static String CHECK_LOAD = "check_load;                            //!5.0,4.0,3.0!10.0,6.0,4.0";
    public static String CHECK_PROCESSES = "check_local_procs;                      //!250!400!RSZDT";
    public static String CHECK_LOCAL_USERS = "check_local_users;                          //!20!50";
    public static String CHECK_SSH = "check_ssh";
    public static String CHECK_FTP = "check_ftp";
    */

    private static String path = "/usr/local/nagios/etc/";

    public static void addHost(String name, String ip) {

        File f = new File(path + "hosts.cfg");  
        if (f.exists() && !f.isDirectory()) {
            System.out.println("File exists!");
        }

        try (FileWriter fw = new FileWriter(path + "hosts.cfg", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {

            
            out.append("define host{\r\n");
            out.append("use                             linux-box\r\n");
            out.append("host_name                       " + name+ "\r\n");
            out.append("alias                           " + name + " alias\r\n");
            out.append("address                         " + ip +"\r\n");
            out.append("}\r\n");
        } catch (IOException e) {
            //System.out.println(e);
        }
    }

    /*
    Available commands are:
    check_local_load!5.0,4.0,3.0!10.0,6.0,4.0
    check_local_procs!250!400!RSZDT
    check_local_users!20!50
    check_ssh
    check_ftp    
     */
    public static void addService(String hostname, ServiceCommandsInterface command, Map<KeyParameterEnum, String> parameterMap, String servicename) {

        try (FileWriter fw = new FileWriter(path + "services.cfg", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {

            //command = "check_local_users"; 
            String parameters = "";
            if(command instanceof LinuxServiceCommands && LinuxServiceCommands.check_ftp != command && LinuxServiceCommands.check_ssh != command && LinuxServiceCommands.check_dhcp != command && LinuxServiceCommands.check_http != command && LinuxServiceCommands.check_ping != command)
            parameters = "!" + parameterMap.get(KeyParameterEnum.WARNING) + "!" + parameterMap.get(KeyParameterEnum.CRITICAL);
            
            if(command instanceof WindowsServiceCommands && WindowsServiceCommands.CLIENTVERSION != command && WindowsServiceCommands.UPTIME != command){
                parameters = " -w " + parameterMap.get(KeyParameterEnum.WARNING) + " -c " + parameterMap.get(KeyParameterEnum.CRITICAL);
            }
            
            String nagiosCommand = command.getCommandName()+ parameters;

            out.append("define service{\r\n");
            out.append("use                     generic-service\r\n");
            out.append("host_name               " + hostname + "\r\n");
            out.append("service_description     " + servicename + "\r\n");
            out.append("check_command           " + nagiosCommand + "\r\n");
            out.append("}\r\n");
        } catch (IOException e) {
            //System.out.println(e);
        }
    }
    
    public static void restartNagios(){
        String[] args = new String[]{"/bin/bash", "-c", "service nagios restart"};
        try {
            Process proc = new ProcessBuilder(args).start();
        } catch (Exception e) {
            //System.out.println(e);
        }
    }
}
