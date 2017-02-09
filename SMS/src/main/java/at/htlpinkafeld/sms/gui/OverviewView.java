/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.pojos.Host;
import at.htlpinkafeld.sms.pojos.Hostgroup;
import at.htlpinkafeld.sms.pojos.Service;
import at.htlpinkafeld.sms.pojos.Servicegroup;
import at.htlpinkafeld.sms.service.JSONService;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * View for the Overview of the Servicepoint Data
 *
 * @author Martin Six
 */
public class OverviewView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "overview";

    private final Tab hostGroupsTab;
    private final Tab serviceGroupsTab;
    private final Tab hostsTab;
    private final Tab servicesTab;

    public OverviewView() {
        super.addComponent(new MenuBarComponent());

        TabSheet tabSheet = new TabSheet();

        BeanItemContainer hostItemContainer = createIndexedContainer(Host.class);
        BeanItemContainer serviceItemContainer = createIndexedContainer(Service.class);
        BeanItemContainer hostgroupItemContainer = createIndexedContainer(Hostgroup.class);
        BeanItemContainer servicegroupItemContainer = createIndexedContainer(Servicegroup.class);

        tabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                // Find the tabsheet
                TabSheet tabsheet = event.getTabSheet();

                // Find the tab and cast it
                OverviewTabPanel tab = (OverviewTabPanel) tabsheet.getSelectedTab();

                for (Component c : tabsheet) {
                    if (c.equals(tab)) {
                        AutoResizingGridLayout argl = tab.getGridLayout();
                        if (argl != null) {
                            UI.getCurrent().getPage().addBrowserWindowResizeListener(argl);
                        }
                        tab.refreshLayout();

                    } else if (c instanceof OverviewTabPanel) {
                        UI.getCurrent().getPage().removeBrowserWindowResizeListener(((OverviewTabPanel) c).getGridLayout());
                    }
                }
            }
        });

        hostGroupsTab = tabSheet.addTab(new OverviewTabPanel(hostgroupItemContainer, getHostGroupsFilterMap()), "Host Groups");
        serviceGroupsTab = tabSheet.addTab(new OverviewTabPanel(servicegroupItemContainer, getServiceGroupsFilterMap()), "Service Groups");
        hostsTab = tabSheet.addTab(new OverviewTabPanel(hostItemContainer, getHostsFilterMap()), "Hosts");
        servicesTab = tabSheet.addTab(new OverviewTabPanel(serviceItemContainer, getServicesFilterMap()), "Services");

        super.addComponent(tabSheet);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    //Functions for creating the searchFilterMappings
    private Map<String, String> getHostGroupsFilterMap() {
        Map<String, String> filterMap = new HashMap<>();
        filterMap.put("Host Gruppe", "name");
        return filterMap;
    }

    private Map<String, String> getServiceGroupsFilterMap() {
        Map<String, String> filterMap = new HashMap<>();
        filterMap.put("Service Gruppe", "name");
        return filterMap;
    }

    private Map<String, String> getHostsFilterMap() {
        Map<String, String> filterMap = new HashMap<>();
        filterMap.put("Host", "hostname");
        return filterMap;
    }

    private Map<String, String> getServicesFilterMap() {
        Map<String, String> filterMap = new HashMap<>();
        filterMap.put("Host", "hostname");
        filterMap.put("Service", "name");
        return filterMap;
    }

    /**
     * Convenience Method to create a {@link Container} for testing purpose.
     *
     *
     * @param containerClass should be {@link Service.class} or
     * {@link Host.class}
     * @return
     */
    static protected BeanItemContainer createIndexedContainer(Class containerClass) {
        BeanItemContainer container;

        if (containerClass == Service.class) {
            container = new BeanItemContainer(containerClass);
        } else {
            container = new BeanItemContainer<>(containerClass);
        }

        List<Host> hosts = JSONService.getHosts();
        if (containerClass == Host.class && hosts != null) {
            container.addAll(hosts);
        } else {
            if (containerClass == Host.class) {
                for (int i = 1; i <= 30; i++) {
                    Host h = new Host(0, "Host " + i, (Host.Hoststatus) getRandomEnum(Host.Hoststatus.values()), LocalDateTime.now(), Duration.ZERO, "Test informationTest informationTest tionTest informationTest informationonTest informationTest information");
                    container.addBean(h);
                }
            } else if (containerClass == Service.class) {
                for (int i = 1; i <= 100; i++) {
                    Service s = new Service(i % 30, 0, "Service " + i, (Service.Servicestatus) getRandomEnum(Service.Servicestatus.values()), LocalDateTime.now(), Duration.ofMinutes(10), 0, "Test informationTest informationTest tionTest informationTest informationonTest informationTest information");
                    s.setHostname("Host " + i % 30);
                    container.addBean(s);
                }
            }
        }
        return container;
    }

    // Testing Function
    static private Enum getRandomEnum(Enum... e) {
        Random r = new Random();
        return e[r.nextInt(e.length)];
    }

}
