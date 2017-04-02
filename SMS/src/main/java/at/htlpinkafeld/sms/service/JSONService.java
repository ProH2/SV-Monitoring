/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.service;

import at.htlpinkafeld.sms.dao.LogDao;
import at.htlpinkafeld.sms.dao.LogDaoImpl;
import at.htlpinkafeld.sms.gui.container.HashMapWithListeners;
import at.htlpinkafeld.sms.pojo.Host;
import at.htlpinkafeld.sms.pojo.Log;
import at.htlpinkafeld.sms.pojo.Service;
import at.htlpinkafeld.sms.service.threads.HostAndServiceTimerTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
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

    //Local auf Rechner
    //public static String NAGIOS = "http://192.168.23.131";
    //für SVISS-Server
    public static String NAGIOS = "http://192.168.14.111";

    private static List<Host> hosts = null;

    private static HashMapWithListeners<String, Host> HOSTS = null;
    private static HashMapWithListeners<String, Service> SERVICES = null;
    private static LogDao logdao = new LogDaoImpl();

    private static Timer timer;

    public static void refresh() {
        timer = new Timer();
        timer.schedule(new HostAndServiceTimerTask(), 0, 10000);
    }

    public static void kill() {
        timer.cancel();
    }

    public static List<Host> getHostsFromNagios() throws IOException {
        try {
            if (HOSTS == null) {
                HOSTS = new HashMapWithListeners<>();
            }
            if (SERVICES == null) {
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
//            key.replace("     ", " ");
//            if (key.contains("host-test")) {
//                key = "host-test+++++2017%2F02%2F10+13-28-04";
//            }
                key = URLEncoder.encode(key, "UTF-8");
                //System.out.println("key2= " + key);
                url = NAGIOS + "/nagios/cgi-bin/statusjson.cgi?query=host&hostname=" + key;
                in = new URL(url).openStream();
                source = IOUtils.toString(in);
                result = new ObjectMapper().readValue(source, HashMap.class);
                IOUtils.closeQuietly(in);
//            System.out.println(result);
                result = (HashMap<String, Object>) ((HashMap) result.get("data")).get("host");
                //System.out.println(result);
                Host h = Host.createHostFromJson(result);
                hosts.add(h);

                Host old = HOSTS.put(h.getHostname(), h);
                if (old != null && old.statusChanged(h)) {
                    EmailService.sendNotificationMailHost(h.getHostname(), old.getStatus(), h.getStatus(), h.getInformation());
                    logdao.insert(new Log(LocalDateTime.now(), h.getHostname(), "Status changed from " + old.getStatus().toString() + " to " + h.getStatus().toString() + "; Plugin Output: " + h.getInformation()));
                }
            }

            for (String s : hostlist) {
                //System.out.println(s);
                String temp = s;
                //            key.replace("     ", " ");
//            if (key.contains("host-test")) {
//                key = "host-test+++++2017%2F02%2F10+13-28-04";
//            }
                s = URLEncoder.encode(s, "UTF-8");
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

                if (result != null) {
                    List<String> list = new ArrayList<>();
                    list.addAll(result.keySet());
                    map.put(s, list);
                }
            }

//        System.out.println("map: " + map);
            int i = 0;
            for (String host : map.keySet()) {
                List<String> olist = (List<String>) map.get(host);
//            System.out.println("olist: " + olist);

                for (String o : olist) {
//                System.out.println("serviceputs: " + host + " " + o);
                    Service s = getServiceDetails(host, o);
//                System.out.println(i++ + " " + s + " " + s.getName());
                    if (s != null) {
                        Service old = SERVICES.put(host + "/" + s.getName(), s);
                        if (old != null && old.hasChanged(s)) {
                            EmailService.sendNotificationMailService(host + "/" + s.getName(), old.getStatus(), s.getStatus(), s.getInformation());
                            logdao.insert(new Log(LocalDateTime.now(), host + "/" + s.getName(), "Status changed from " + old.getStatus().toString() + " to " + s.getStatus().toString() + "; Plugin Output: " + s.getInformation()));
                        }
                    }
                }
            }

            SERVICES.fireMapChanged();
            HOSTS.fireMapChanged();
//        System.out.println("SERVICES OUTPUT: size=" + SERVICES.keySet().size());
//        for (String s : SERVICES.keySet()) {
//            System.out.println(s + "\t" + SERVICES.get(s));
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hosts;
    }

    public static Service getServiceDetails(String host, String service) throws IOException {
        //service = service.replace(" ", "+");

//         System.out.println("h+s:" + host + " " + service);
        InputStream in = new URL(NAGIOS + "/nagios/cgi-bin/statusjson.cgi?query=service&hostname=" + host + "&servicedescription=" + service).openStream();
        String source = IOUtils.toString(in);
        HashMap<String, Object> result = new ObjectMapper().readValue(source, HashMap.class);
        IOUtils.closeQuietly(in);

        HashMap<String, Object> servicedetails = (HashMap<String, Object>) ((HashMap) result.get("data")).get("service");
//        System.out.println("servicedetails: " + servicedetails);

        Service s = Service.createServiceFromJson(servicedetails, host);

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

    public static HashMapWithListeners<String, Host> getHOSTS() {
        return HOSTS;
    }

    public static void setHOSTS(HashMapWithListeners<String, Host> HOSTS) {
        JSONService.HOSTS = HOSTS;
    }

    public static HashMapWithListeners<String, Service> getSERVICES() {
        return SERVICES;
    }

    public static void setSERVICES(HashMapWithListeners<String, Service> SERVICES) {
        JSONService.SERVICES = SERVICES;
    }

}
