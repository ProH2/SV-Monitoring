/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.container;

import at.htlpinkafeld.sms.dao.CommentDao;
import at.htlpinkafeld.sms.dao.CommentDaoImpl;
import at.htlpinkafeld.sms.dao.DutyDao;
import at.htlpinkafeld.sms.dao.DutyDaoImpl;
import at.htlpinkafeld.sms.dao.HostgroupDao;
import at.htlpinkafeld.sms.dao.HostgroupDaoImpl;
import at.htlpinkafeld.sms.dao.LogDao;
import at.htlpinkafeld.sms.dao.LogDaoImpl;
import at.htlpinkafeld.sms.dao.UserDao;
import at.htlpinkafeld.sms.dao.UserDaoImpl;
import at.htlpinkafeld.sms.gui.Host_Service_ManagementView;
import at.htlpinkafeld.sms.gui.OverviewView;
import at.htlpinkafeld.sms.gui.TimeManagementView;
import at.htlpinkafeld.sms.gui.UserManagementView;
import at.htlpinkafeld.sms.gui.overviewComponents.HostGroupOverviewTabPanel;
import at.htlpinkafeld.sms.gui.overviewComponents.HostOverviewTabPanel;
import at.htlpinkafeld.sms.gui.overviewComponents.HostPanel;
import at.htlpinkafeld.sms.gui.overviewComponents.ServiceOverviewTabPanel;
import at.htlpinkafeld.sms.gui.overviewComponents.ServicePanel;
import at.htlpinkafeld.sms.pojo.User;
import at.htlpinkafeld.sms.pojo.Comment;
import at.htlpinkafeld.sms.pojo.Host;
import at.htlpinkafeld.sms.pojo.Hostgroup;
import at.htlpinkafeld.sms.pojo.Log;
import at.htlpinkafeld.sms.pojo.Service;
import at.htlpinkafeld.sms.service.JSONService;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Table;
import com.vaadin.ui.components.calendar.event.CalendarEditableEventProvider;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class with multiple static Factory or Singelton methods to create various
 * Container.
 *
 * @author Martin Six
 */
public class ContainerFactory {

    private static HashMapWithListeners<String, Host> hostMap = null;

    private static HashMapWithListeners<String, Service> serviceMap = null;

    private static List<Hostgroup> hostgroups = null;

    private static BeanItemContainer<User> userContainer = null;

    private static DutyEventProvider dutyEventProvider = null;

    private static UserDao userdao = new UserDaoImpl();

    private static DutyDao dutydao = new DutyDaoImpl();

    private static CommentDao commentdao = new CommentDaoImpl();

    private static HostgroupDao hostgroupdao = new HostgroupDaoImpl();

    private static LogDao logdao = new LogDaoImpl();

    /**
     * Initializes hostMap
     */
    private static void initHostMap() {

        hostMap = JSONService.getHOSTS();
        if (hostMap == null || hostMap.isEmpty()) {

            List<Map.Entry<String, Host>> entries = new ArrayList<>();

//            for (int i = 1; i <= 10; i++) {
//                Host h = new Host(0, "Host " + i, (Host.Hoststatus) OverviewView.getRandomEnum(Host.Hoststatus.values()), LocalDateTime.now(), Duration.ZERO, "Test informationTest informationTest tionTest informationTest informationonTest informationTest information");
//                entries.add(new AbstractMap.SimpleEntry<>(h.getHostname(), h));
//            }
            LocalDateTime currentLocalDateTime = LocalDateTime.of(2017, Month.APRIL, 26, 16, 30);
            entries.add(new AbstractMap.SimpleEntry<>("nas", new Host(0, "nas", Host.Hoststatus.UP, currentLocalDateTime, Duration.ofDays(20).plusHours(3), "Das NAS im Standort Oberwart")));
            entries.add(new AbstractMap.SimpleEntry<>("yaestar", new Host(0, "yaestar", Host.Hoststatus.DOWN, currentLocalDateTime, Duration.ofDays(19).plusHours(6), "Das Yaestar VoIP-Gerät im Standort Oberwart")));
            entries.add(new AbstractMap.SimpleEntry<>("localhost", new Host(0, "localhost", Host.Hoststatus.UP, currentLocalDateTime, Duration.ofDays(23).plusHours(4), "Der Webserver für die Software")));
            entries.add(new AbstractMap.SimpleEntry<>("unify", new Host(0, "unify", Host.Hoststatus.UP, currentLocalDateTime, Duration.ofDays(20).plusHours(3), "Das UNIFY-System im Standort Oberwart")));

            hostMap = new HashMapWithListeners<>();
            entries.stream().sorted(Map.Entry.comparingByValue(new Comparator<Host>() {
                @Override
                public int compare(Host o1, Host o2) {
                    return Objects.compare(o1.getStatus(), o2.getStatus(), Comparator.nullsFirst(Comparator.reverseOrder()));
                }
            })).forEachOrdered(entry -> hostMap.put(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Initializes serviceMap
     */
    private static void initServiceMap() {
        serviceMap = JSONService.getSERVICES();
        if (serviceMap == null || serviceMap.isEmpty()) {
            List<Map.Entry<String, Service>> entries = new ArrayList<>();
            for (int i = 1; i <= 20; i++) {
                Service s = new Service(i % 10, 0, "Service " + i, (Service.Servicestatus) OverviewView.getRandomEnum(Service.Servicestatus.values()), LocalDateTime.now(), Duration.ofMinutes(10), 0, "Test informationTest informationTest tionTest informationTest informationonTest informationTest information");
                switch (i % 4) {
                    case 0:
                        s.setHostname("nas");
                        break;
                    case 1:
                        s.setHostname("yaestar");
                        break;
                    case 2:
                        s.setHostname("localhost");
                        break;
                    case 3:
                        s.setHostname("unify");
                }
//                s.setHostname("Host " + i % 10);
                entries.add(new AbstractMap.SimpleEntry<>(s.getHostname() + "/" + s.getName(), s));
            }

            serviceMap = new HashMapWithListeners<>();
            entries.stream().sorted(Map.Entry.comparingByValue(new Comparator<Service>() {
                @Override
                public int compare(Service o1, Service o2) {
                    return Objects.compare(o1.getStatus(), o2.getStatus(), Comparator.nullsFirst(Comparator.reverseOrder()));
                }
            })).forEachOrdered(entry -> serviceMap.put(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Initializes DutyEventProvider
     */
    private static void initDutyEventProvider() {
        dutyEventProvider = new DutyEventProvider(new DutyDaoImpl());

    }

    /**
     * Static Method to create the HostContainer for the {@link HostPanel} in
     * {@link HostOverviewTabPanel}
     *
     * @return {@link MapReferenceContainer} which contains the Hosts. May
     * return null if there is an error during initialisation
     */
    public static MapReferenceContainer<Host> createHostContainer() {

        HashMapWithListeners<String, Host> changeListener = JSONService.getHOSTS();

        if (hostMap == null || changeListener != null) {
            initHostMap();
        }
        try {
            return new MapReferenceContainer<>(hostMap, Host.class);
        } catch (IllegalAccessException | InstantiationException ex) {
            return null;
        }
    }

    /**
     * Static Method to create the ServiceContainer for the {@link ServicePanel}
     * in {@link ServiceOverviewTabPanel}
     *
     * @return {@link MapReferenceContainer} which contains the Services. May
     * return null if there is an error during initialisation
     */
    public static MapReferenceContainer<Service> createServiceContainer() {
        HashMapWithListeners<String, Service> changeListener = JSONService.getSERVICES();

        if (serviceMap == null || changeListener != null) {
            initServiceMap();
        }
        try {
            return new MapReferenceContainer<>(serviceMap, Service.class);
        } catch (IllegalAccessException | InstantiationException ex) {
            return null;
        }
    }

    /**
     * Static Method to create the {@link HostServiceHierarchical_Container} for
     * the {@link Table} in {@link Host_Service_ManagementView}
     *
     * @return {@link HostServiceHierarchical_Container} which contains the
     * Hosts and Services in a Hierarchical order
     */
    public static HostServiceHierarchical_Container createHostServiceHierarchical_Container() {
        return new HostServiceHierarchical_Container(hostMap.values(), serviceMap.values());
    }

    /**
     * Static Method to create the {@link HostgroupHierarchical_Container} for
     * the {@link Table} in {@link Host_Service_ManagementView}
     *
     * @return {@link HostgroupHierarchical_Container} which contains the
     * Hostgroups in a Hierarchical order
     */
    public static HostgroupHierarchical_Container createHostgroupHierarchical_Container() {
//        if (hostgroups == null) {
//            initHostgroupList();
//        }
        return new HostgroupHierarchical_Container(hostgroupdao);
    }

    /**
     * Static Method to create the Container for the Hostgroups in
     * {@link HostGroupOverviewTabPanel}
     *
     * @return {@link Container} which contains the Hostgroups
     */
    public static DaoDelegatingContainer<Hostgroup> createHostgroupContainer() {
//        if (hostgroups == null) {
//            initHostgroupList();
//        }
        return new DaoDelegatingContainer<>(Hostgroup.class, hostgroupdao);
    }

    /**
     * Static Method to create the Container for the User-{@link Grid} in
     * {@link UserManagementView}
     *
     * @return {@link Container} which contains the Users
     */
    public static DaoDelegatingContainer<User> createIndexedUserContainer() {
        return new DaoDelegatingContainer<>(User.class, userdao);
    }

    /**
     * Static Method to create the Container for the Log-{@link Grid} in
     * {@link UserManagementView}
     *
     * @return {@link Container} which contains the Users
     */
    public static DaoDelegatingContainer<Log> createLogContainer() {
        return new DaoDelegatingContainer<>(Log.class, logdao);
    }

    /**
     * Static Method to create the EventProvider for the Duty-{@link Calendar}
     * in {@link TimeManagementView}
     *
     * @return {@link CalendarEditableEventProvider} which contains the Duties
     */
    public static DutyEventProvider createDutyEventProvider() {
        if (dutyEventProvider == null) {
            initDutyEventProvider();
        }

        return dutyEventProvider;
    }

    /**
     * Static Method to create the Container for the Comment-{@link Grid} in
     * {@link HostPanel}
     *
     * @param hostname The name of the Host which Comments should be returned
     * @return Container with the corresponding Comments
     */
    public static BeanItemContainer<Comment> createHostCommentContainer(String hostname) {
//        BeanItemContainer<Comment> itemContainer = new BeanItemContainer<>(Comment.class);
//
//        for (int i = 0; i < 5; i++) {
//            //itemContainer.addBean(new Comment(LocalDateTime.now(), hostname, "Test Comment " + i + " at " + hostname));
//            itemContainer.addBean(new Comment("Testcomment", "Testcomment to" + hostname, 1, LocalDateTime.now()));
//        }
        return new DaoDelegatingContainer<>(Comment.class, commentdao, commentdao.findByCommentTo(hostname));
    }

    /**
     * Static Method to create the Container for the Comment-{@link Grid} in
     * {@link ServicePanel}
     *
     * @param serviceUId The unique identifier for the Service, which Comments
     * should be returned. The unique identifier is hostname/servicename
     * @return Container with the corresponding Comments
     */
    public static BeanItemContainer<Comment> createServiceCommentContainer(String serviceUId) {
//        BeanItemContainer<Comment> itemContainer = new BeanItemContainer<>(Comment.class);
//
//        for (int i = 0; i < 5; i++) {
//            itemContainer.addBean(new Comment("Testcomment", "Testcomment to" + serviceUId, 1, LocalDateTime.now()));
//        }
        return new DaoDelegatingContainer<>(Comment.class, commentdao, commentdao.findByCommentTo(serviceUId));
    }

}
