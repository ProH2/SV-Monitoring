/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.gui.window.HostDetailWindow;
import at.htlpinkafeld.sms.gui.OverviewView;
import at.htlpinkafeld.sms.gui.container.MapReferenceContainer;
import at.htlpinkafeld.sms.pojo.Host;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel which represents the {@link Host Hosts}. It is used as a
 * {@link TabSheet.Tab} in {@link OverviewView}
 *
 * @author Martin Six
 */
public class HostOverviewTabPanel extends Panel implements OverviewTabPanel, DetachListener {

    private AutoResizingGridLayout gridLayout = null;
    private final MapReferenceContainer<Host> hostReferenceContainer;

    /**
     * Constant for the width of the CustomComponent which contains the Data
     * from the Servicepoints. It is used to calculate the amount of columns on
     * the page
     *
     */
    public static final int COMPONENT_WIDTH = 300;

    /**
     * Constructor for the {@link HostOverviewTabPanel}
     *
     * @param container which contains the {@link Host Hosts}
     */
    public HostOverviewTabPanel(MapReferenceContainer<Host> container) {
        this.hostReferenceContainer = container;
//        this.container.sort(new String[]{"status"}, new boolean[]{false});

        this.hostReferenceContainer.addItemSetChangeListener((Container.ItemSetChangeEvent event) -> {
            refreshLayout();
        });

        VerticalLayout parentVerticalLayout = new VerticalLayout();

        //create Filtermapping
        Map<String, String> filterMap = new HashMap<>();
        filterMap.put("Host", "hostname");

        SearchComponent searchComponent = new SearchComponent(filterMap, container);
        parentVerticalLayout.addComponent(searchComponent);
        parentVerticalLayout.setComponentAlignment(searchComponent, Alignment.MIDDLE_RIGHT);

        this.gridLayout = new AutoResizingGridLayout(COMPONENT_WIDTH);
        parentVerticalLayout.addComponent(gridLayout);

        super.setContent(parentVerticalLayout);

    }

    @Override
    final public void refreshLayout() {
        Collection<String> itemList = hostReferenceContainer.getItemIds();
        if (gridLayout != null) {
            gridLayout.removeAllComponents();

            for (String s : itemList) {
                Map.Entry<String, Host> hostMapEntry = ((BeanItem<Map.Entry<String, Host>>) hostReferenceContainer.getItem(s)).getBean();
                HostPanel hostPanel = new HostPanel(hostMapEntry, hostReferenceContainer);

                this.hostReferenceContainer.addMapChangeListener(hostPanel);

                hostPanel.addClickListener((MouseEvents.ClickEvent event) -> {
                    HostDetailWindow hostDetailWindow = new HostDetailWindow(hostMapEntry, hostReferenceContainer);
                    hostReferenceContainer.addMapChangeListener(hostDetailWindow);
                    UI.getCurrent().addWindow(hostDetailWindow);
                });

                hostPanel.setWidth(COMPONENT_WIDTH, Unit.PIXELS);
                gridLayout.addComponent(hostPanel);
            }

            gridLayout.markAsDirty();
        }
    }

    @Override
    public void detach(DetachEvent event) {
        for (Component c : gridLayout) {
            hostReferenceContainer.removeMapChangeListener((HostPanel) c);
        }
    }

}
