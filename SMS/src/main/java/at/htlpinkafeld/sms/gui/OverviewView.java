/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.gui.overviewComponents.OverviewTabPanel;
import at.htlpinkafeld.sms.gui.overviewComponents.HostGroupOverviewTabPanel;
import at.htlpinkafeld.sms.gui.overviewComponents.HostOverviewTabPanel;
import at.htlpinkafeld.sms.gui.overviewComponents.ServiceOverviewTabPanel;
import at.htlpinkafeld.sms.gui.container.MapReferenceContainer;
import at.htlpinkafeld.sms.pojos.Host;
import at.htlpinkafeld.sms.pojos.Hostgroup;
import at.htlpinkafeld.sms.pojos.Service;
import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.Random;

/**
 * View for the Overview of the Servicepoint Data
 *
 * @author Martin Six
 */
public class OverviewView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "overview";

    private final TabSheet tabSheet;
    private final Tab hostGroupsTab;
    private final Tab hostsTab;
    private final Tab servicesTab;

    /**
     * Constructor for OverviewView
     */
    public OverviewView() {
        super.addComponent(((SMS_Main) UI.getCurrent()).getMenuBarComponent());

        tabSheet = new TabSheet();

//        HashMapWithListeners<String, Host> hostMap = new HashMapWithListeners<>();
//        for (int i = 1; i <= 10; i++) {
//            Host h = new Host(0, "Host " + i, (Host.Hoststatus) getRandomEnum(Host.Hoststatus.values()), LocalDateTime.now(), Duration.ZERO, "Test informationTest informationTest tionTest informationTest informationonTest informationTest information");
//            hostMap.put(h.getHostname(), h);
//        }
        MapReferenceContainer<Host> hostItemContainer = null;
//        PanelContainer hostItemContainer = null;
        hostItemContainer = ContainerFactory.createHostContainer();

        MapReferenceContainer<Service> serviceItemContainer = null;
        serviceItemContainer = ContainerFactory.createServiceContainer();

        BeanItemContainer<Hostgroup> hostgroupItemContainer = ContainerFactory.createHostgroupContainer();

        tabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                // Find the tabsheet
                TabSheet tabsheet = event.getTabSheet();

//                Test for MapContainer
//                for (Map.Entry<String, Host> entry : hostMap.entrySet()) {
//                    entry.getValue().setDuration(entry.getValue().getDuration().plusSeconds(10));
//                    hostMap.put(entry.getKey(), entry.getValue());
//                }
                // Find the tab and cast it
                OverviewTabPanel tab = (OverviewTabPanel) tabsheet.getSelectedTab();
                tab.refreshLayout();

            }
        });

        hostGroupsTab = tabSheet.addTab(new HostGroupOverviewTabPanel(hostgroupItemContainer, hostItemContainer), "Host Groups");

        HostOverviewTabPanel hostOverviewTabPanel = new HostOverviewTabPanel(hostItemContainer);
        UI.getCurrent().addDetachListener(hostOverviewTabPanel);
        hostsTab = tabSheet.addTab(hostOverviewTabPanel, "Hosts");

        ServiceOverviewTabPanel serviceOverviewTabPanel = new ServiceOverviewTabPanel(serviceItemContainer);
        UI.getCurrent().addDetachListener(serviceOverviewTabPanel);
        servicesTab = tabSheet.addTab(serviceOverviewTabPanel, "Services");

        super.addComponent(tabSheet);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        ((SMS_Main) UI.getCurrent()).getMenuBarComponent().switchStyle();
        super.addComponentAsFirst(((SMS_Main) UI.getCurrent()).getMenuBarComponent());
        tabSheet.setSelectedTab(0);
    }

    /**
     * Convenience Method to create a {@link Container} for testing purpose.
     *
     * @param containerClass test-Container-Class
     * @return test-Container
     */
    static protected BeanItemContainer createIndexedContainer(Class containerClass) {
        BeanItemContainer container;

        container = new BeanItemContainer<>(containerClass);

        return container;
    }

    /**
     * Testing Function
     *
     * @param e values of an Enum
     * @return one of the Enum values
     */
    public static Enum getRandomEnum(Enum... e) {
        Random r = new Random();
        return e[r.nextInt(e.length)];
    }

}
