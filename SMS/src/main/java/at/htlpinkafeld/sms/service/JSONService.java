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

import at.htlpinkafeld.sms.gui.container.HashMapWithListeners;
import at.htlpinkafeld.sms.pojos.Service;

//import org.apache.commons.io.IOUtils;
/**
 *
 * @author neume
 */
public class JSONService {

    public static String NAGIOS = "http://192.168.23.131";
    private static List<Host> hosts = null;

    private static HashMapWithListeners<String, Object> HOSTS = null;
    private static HashMapWithListeners<String, Object> SERVICES = null;

    public static void refresh() {
        Timer timer = new Timer();
        timer.schedule(new HostAndServiceTimerTask(), 0, 10000);
    }

    public static List<Host> getHostsFromNagios() throws IOException {

        if (HOSTS == null) { 
            HOSTS = new HashMapWithListeners<>();
        }
        if(SERVICES == null){
            SERVICES = new HashMapWithListeners<>();
        }

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
        //System.out.println(hostmap);

        List<String> hostlist = new ArrayList<>();
        hostlist.addAll(hostmap.keySet());
        //System.out.println(hostlist);
        HashMap<String, List> map = new HashMap<>();

        //Ausgabe
        Iterator entries;
        entries = hostmap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            Integer value = (Integer) entry.getValue();
//            System.out.println("Key = " + key + ", Value = " + value);

            //System.out.println("url + key= " + key);
            key.replace("     ", " ");
            if (key.contains("host-test")) {
                key = "host-test+++++2017%2F02%2F10+13-28-04";
            }
            //System.out.println("key2= " + key);
            url = "http://192.168.23.131/nagios/cgi-bin/statusjson.cgi?query=host&hostname=" + key;
            in = new URL(url).openStream();
            source = IOUtils.toString(in);
            result = new ObjectMapper().readValue(source, HashMap.class);
            IOUtils.closeQuietly(in);
//            System.out.println(result);
            result = (HashMap<String, Object>) ((HashMap) result.get("data")).get("host");
            //System.out.println(result);
            Host h = Host.createHostFromJson(result);
            hosts.add(h);
            HOSTS.put(h.getHostname(), h);
        }

        for (String s : hostlist) {
            //System.out.println(s);
            String temp = s;
            if (s.contains("host-test")) {
                s = "host-test+++++2017%2F02%2F10+13-28-04";
            }
            url = NAGIOS + "/nagios/cgi-bin/statusjson.cgi?query=servicelist&hostname=" + s;
            in = new URL(url).openStream();
            source = IOUtils.toString(in);
            result = new ObjectMapper().readValue(source, HashMap.class);
            IOUtils.closeQuietly(in);

            //System.out.println("1: " + result);
            //System.out.println("2: " + (result = (HashMap<String, Object>) result.get("data")));
            //System.out.println("3: " + (result = (HashMap<String, Object>) result.get("servicelist")));
            //System.out.println("4: " + (result = (HashMap<String, Object>) result.get(temp)));
            //HashMap<String, Object> servicemap = (HashMap<String, Object>) ((HashMap) (((HashMap) result.get("data")).get("servicelist"))).get(s);
            
            result = (HashMap<String, Object>) result.get("data");
            result = (HashMap<String, Object>) result.get("servicelist");
            result = (HashMap<String, Object>) result.get(temp);
            
            
            entries = result.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                //Integer value = (Integer) entry.getValue();
//                System.out.println("Key = " + key + ", Value = " + value);
//                System.out.println("key: " + key);
                JSONService.getServiceDetails(s, key);
            }
            List<String> list = new ArrayList<>();
            list.addAll(result.keySet());
            map.put(s, list);
//
        }

//        System.out.println("map: " + map);
        for (String host : map.keySet()) {
            List<String> olist = (List<String>) map.get(host);
//            System.out.println("olist: " + olist);

            for (String o : olist) {
//                System.out.println("serviceputs: " + host + " " + o);
                Service s = getServiceDetails(host, o);
                SERVICES.put(host + "/" + s.getHostname(), s);
            }
        }

        SERVICES.fireMapChanged();

        System.out.println("SERVICES OUTPUT: size=" + SERVICES.keySet().size());
        for (String s : SERVICES.keySet()) {
            System.out.println(s + "\t" + SERVICES.get(s));
        }

        return hosts;
    }

    public static Service getServiceDetails(String host, String service) throws IOException {
        service = service.replace(" ", "+");

//         System.out.println("h+s:" + host + " " + service);
        
        InputStream in = new URL(NAGIOS + "/nagios/cgi-bin/statusjson.cgi?query=service&hostname=" + host + "&servicedescription=" + service).openStream();
        String source = IOUtils.toString(in);
        HashMap<String, Object> result = new ObjectMapper().readValue(source, HashMap.class);
        IOUtils.closeQuietly(in);

        HashMap<String, Object> servicedetails = (HashMap<String, Object>) ((HashMap) result.get("data")).get("service");
//        System.out.println("servicedetails: " + servicedetails);

        

        Service s =Service.createServiceFromJson(servicedetails);

//        System.out.println("Service: " + s);
        
        return s;
    }

    public static List<Host> getHosts() {
        return hosts;
    }

    public static void setHosts(List<Host> h) {
        hosts = h;
    }

    public static HashMap<String, Object> getStatusFromAllHosts() throws IOException {
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

    public static HashMapWithListeners<String, Object> getHOSTS() {
        if (HOSTS == null) {
            HOSTS = new HashMapWithListeners();
        }

        return HOSTS;
    }

    public static void setHOSTS(HashMapWithListeners<String, Object> HOSTS) {
        JSONService.HOSTS = HOSTS; 
    }
    
    public static HashMapWithListeners<String, Object> getSERVICES() {
        if (SERVICES == null) {
            SERVICES = new HashMapWithListeners();
        }

        return SERVICES;
    }

    public static void setSERVICES(HashMapWithListeners<String, Object> SERVICES) {
        JSONService.SERVICES = SERVICES; 
    }

}
