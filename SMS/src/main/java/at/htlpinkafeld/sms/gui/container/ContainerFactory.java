/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.container;

import at.htlpinkafeld.dao.DutyDaoImpl;
import at.htlpinkafeld.sms.gui.Host_Service_ManagementView;
import at.htlpinkafeld.sms.gui.OverviewView;
import at.htlpinkafeld.sms.gui.TimeManagementView;
import at.htlpinkafeld.sms.gui.UserManagementView;
import at.htlpinkafeld.sms.gui.overviewComponents.HostOverviewTabPanel;
import at.htlpinkafeld.sms.gui.overviewComponents.HostPanel;
import at.htlpinkafeld.sms.gui.overviewComponents.ServiceOverviewTabPanel;
import at.htlpinkafeld.sms.gui.overviewComponents.ServicePanel;
import at.htlpinkafeld.sms.pojo.User;
import at.htlpinkafeld.sms.pojos.Comment;
import at.htlpinkafeld.sms.pojos.Host;
import at.htlpinkafeld.sms.pojos.Service;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Table;
import com.vaadin.ui.components.calendar.event.CalendarEditableEventProvider;
import java.time.Duration;
import java.time.LocalDateTime;
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

    private static BeanItemContainer<User> userContainer = null;

    private static CalendarEditableEventProvider dutyEventProvider = null;

    /**
     * Initializes hostMap
     */
    private static void initHostMap() {

        //TODO use real hosts
        List<Map.Entry<String, Host>> entries = null;
//        entries = JSONService.getHosts();
        if (entries == null) {
            entries = new ArrayList<>();
        }
        for (int i = 1; i <= 10; i++) {
            Host h = new Host(0, "Host " + i, (Host.Hoststatus) OverviewView.getRandomEnum(Host.Hoststatus.values()), LocalDateTime.now(), Duration.ZERO, "Test informationTest informationTest tionTest informationTest informationonTest informationTest information");
            entries.add(new AbstractMap.SimpleEntry<>(h.getHostname(), h));
        }

        hostMap = new HashMapWithListeners<>();
        entries.stream().sorted(Map.Entry.comparingByValue(new Comparator<Host>() {
            @Override
            public int compare(Host o1, Host o2) {
                return Objects.compare(o1.getStatus(), o2.getStatus(), Comparator.nullsFirst(Comparator.reverseOrder()));
            }
        })).forEachOrdered(entry -> hostMap.put(entry.getKey(), entry.getValue()));

    }

    /**
     * Initializes serviceMap
     */
    private static void initServiceMap() {
        List<Map.Entry<String, Service>> entries = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Service s = new Service(i % 10, 0, "Service " + i, (Service.Servicestatus) OverviewView.getRandomEnum(Service.Servicestatus.values()), LocalDateTime.now(), Duration.ofMinutes(10), 0, "Test informationTest informationTest tionTest informationTest informationonTest informationTest information");
            s.setHostname("Host " + i % 10);
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

    /**
     * Initializes UserContainer
     */
    private static void initUserContainer() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "Dogist", "1234", "Martin", "noplan@gmc.at", "5421575"));
        users.add(new User(2, "Irish", "4321", "Sebastian", "noplan@qmail.com", "5788775"));
        userContainer = new BeanItemContainer<>(User.class, users);

//        UserDao userDao = new UserDaoImpl();
//        userContainer = new BeanItemContainer<>(User.class, userDao.findAll());
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
        if (hostMap == null) {
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
        if (serviceMap == null) {
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
     * Static Method to create the Container for the User-{@link Grid} in
     * {@link UserManagementView}
     *
     * @return {@link Container} which contains the Users
     */
    public static BeanItemContainer<User> createIndexedUserContainer() {
        if (userContainer == null) {
            initUserContainer();
        }

        return userContainer;
    }

    /**
     * Static Method to create the EventProvider for the Duty-{@link Calendar}
     * in {@link TimeManagementView}
     *
     * @return {@link CalendarEditableEventProvider} which contains the Duties
     */
    public static CalendarEditableEventProvider createDutyEventProvider() {
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
        BeanItemContainer<Comment> itemContainer = new BeanItemContainer<>(Comment.class);
        for (int i = 0; i < 5; i++) {
            itemContainer.addBean(new Comment(LocalDateTime.now(), hostname, "Test Comment " + i + " at " + hostname));
        }
        return itemContainer;
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
        BeanItemContainer<Comment> itemContainer = new BeanItemContainer<>(Comment.class);
        for (int i = 0; i < 5; i++) {
            itemContainer.addBean(new Comment(LocalDateTime.now(), serviceUId, "Test Comment " + i + " at " + serviceUId));
        }
        return itemContainer;
    }

}
