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
import java.util.Random;

/**
 * View for the Overview of the Servicepoint Data
 *
 * @author Martin Six
 */
public class OverviewView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "overview";

    private Tab hostGroupsTab;
    private Tab serviceGroupsTab;
    private Tab hosts;
    private Tab services;

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

        hostGroupsTab = tabSheet.addTab(new OverviewTabPanel(hostgroupItemContainer, "Host Gruppen"), "Host Groups");
        serviceGroupsTab = tabSheet.addTab(new OverviewTabPanel(servicegroupItemContainer, "Service Gruppen"), "Service Groups");
        hosts = tabSheet.addTab(new OverviewTabPanel(hostItemContainer, "Host Gruppen", "Host"), "Hosts");
        services = tabSheet.addTab(new OverviewTabPanel(serviceItemContainer, "Host Gruppen", "Host", "Service Gruppen", "Service"), "Services");

        super.addComponent(tabSheet);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    /**
     * Convenience Method to create a {@link Container} for testing purpose.
     *
     * @return
     */
    private BeanItemContainer createIndexedContainer(Class containerClass) {
        BeanItemContainer container;

        if (containerClass == Service.class) {
            container = new BeanItemContainer(containerClass);
        } else {
            container = new BeanItemContainer<>(containerClass);
        }

        for (int i = 1; i <= 100; i++) {
            if (containerClass == Host.class) {
                container.addBean(new Host(0, "Test" + i, (Host.Hoststatus) getRandomEnum(Host.Hoststatus.values()), LocalDateTime.now(), Duration.ZERO, "Test informationTest informationTest tionTest informationTest informationonTest informationTest information"));
            } else if (containerClass == Service.class) {
                container.addBean(new Service(i % 30, 0, "Test" + i, (Service.Servicestatus) getRandomEnum(Service.Servicestatus.values()), LocalDateTime.now(), Duration.ofMinutes(10), 0, "Test informationTest informationTest tionTest informationTest informationonTest informationTest information"));
            }

        }
        return container;
    }

    // Testing Function
    private Enum getRandomEnum(Enum... e) {
        Random r = new Random();
        return e[r.nextInt(e.length)];
    }

}
