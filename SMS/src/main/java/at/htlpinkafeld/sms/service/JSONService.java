/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.service;

import at.htlpinkafeld.sms.pojos.Host;
import at.htlpinkafeld.sms.service.threads.HostAndServiceTimerTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import org.apache.commons.io.IOUtils;

//import org.apache.commons.io.IOUtils;
/**
 *
 * @author neume
 */
public class JSONService {

    public static String NAGIOS = "http://192.168.23.131";
    private static List<Host> hosts = null;

    public static void refresh() {
        Timer timer = new Timer();
        timer.schedule(new HostAndServiceTimerTask(), 0, 10000);
    }

    public static List<Host> getHostsFromNagios() throws IOException {
        hosts = new ArrayList<>();
        String url;

        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("nagiosadmin", "admin".toCharArray());
            }
        });
        InputStream in = new URL(NAGIOS + "/nagios/cgi-bin/statusjson.cgi?query=hostlist").openStream();
        String source = IOUtils.toString(in);
        HashMap<String, Object> result = new ObjectMapper().readValue(source, HashMap.class);
        IOUtils.closeQuietly(in);

        HashMap<String, Object> hostmap = (HashMap<String, Object>) ((HashMap) result.get("data")).get("hostlist");
//        System.out.println(hostlist);

        List<String> hostlist = new ArrayList<>();
        hostlist.addAll(hostmap.keySet());
        HashMap<String, List> map = new HashMap<>();

        //Ausgabe
        Iterator entries;
        entries = hostmap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            Integer value = (Integer) entry.getValue();
//            System.out.println("Key = " + key + ", Value = " + value);
            
            url = "http://192.168.23.131/nagios/cgi-bin/statusjson.cgi?query=host&hostname="+key;
            in = new URL(url).openStream();
            source = IOUtils.toString(in);
            result = new ObjectMapper().readValue(source, HashMap.class);
            IOUtils.closeQuietly(in);
//            System.out.println(result);
            result = (HashMap<String, Object>) ((HashMap) result.get("data")).get("host");
//            System.out.println(result);
            hosts.add(Host.createHostFromJson(result));
        }

        for (String s : hostlist) {
            url = NAGIOS + "/nagios/cgi-bin/statusjson.cgi?query=servicelist&hostname=" + s;
            in = new URL(url).openStream();
            source = IOUtils.toString(in);
            result = new ObjectMapper().readValue(source, HashMap.class);
            IOUtils.closeQuietly(in); 

            HashMap<String, Object> servicemap = (HashMap<String, Object>) ((HashMap) (((HashMap) result.get("data")).get("servicelist"))).get(s);

            entries = servicemap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                Integer value = (Integer) entry.getValue();
//                System.out.println("Key = " + key + ", Value = " + value);
                JSONService.getServiceDetails(s, key);
            }
            List<String> list = new ArrayList<>();
            list.addAll(servicemap.keySet());
            map.put(s, list);

        }

//        System.out.println(map);
        return hosts;
    }

    public static void getServiceDetails(String host, String service) throws IOException {
        service = service.replace(" ", "+");
        InputStream in = new URL(NAGIOS + "/nagios/cgi-bin/statusjson.cgi?query=service&hostname=" + host + "&servicedescription=" + service).openStream();
        String source = IOUtils.toString(in);
        HashMap<String, Object> result = new ObjectMapper().readValue(source, HashMap.class);
        IOUtils.closeQuietly(in);

        HashMap<String, Object> servicedetails = (HashMap<String, Object>) ((HashMap) result.get("data")).get("service");
//        System.out.println(servicedetails);
    }

    public static List<Host> getHosts() {
        return hosts;
    }

    public static void setHosts(List<Host> h) {
        hosts = h;
    }
    
    public static HashMap<String, Object> getStatusFromAllHosts() throws IOException{
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("nagiosadmin", "admin".toCharArray());
            }
        });
        InputStream in = new URL(NAGIOS + "/nagios/cgi-bin/statusjson.cgi?query=hostlist").openStream(); 
        String source = IOUtils.toString(in);
        HashMap<String, Object> result = new ObjectMapper().readValue(source, HashMap.class);
        IOUtils.closeQuietly(in);

        HashMap<String, Object> hostmap = (HashMap<String, Object>) ((HashMap) result.get("data")).get("hostlist");
        return hostmap;
    }
}
