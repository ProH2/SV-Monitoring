/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.gui.AutoResizingGridLayout;
import at.htlpinkafeld.sms.gui.OverviewView;
import at.htlpinkafeld.sms.gui.container.MapReferenceContainer;
import at.htlpinkafeld.sms.pojos.Host;
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
    private final MapReferenceContainer<Host> container;

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
        this.container = container;
//        this.container.sort(new String[]{"status"}, new boolean[]{false});

        this.container.addItemSetChangeListener(new Container.ItemSetChangeListener() {
            @Override
            public void containerItemSetChange(Container.ItemSetChangeEvent event) {
                refreshLayout();
            }
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
        Collection<String> itemList = container.getItemIds();
        if (gridLayout != null) {
            gridLayout.removeAllComponents();

            for (String s : itemList) {
                Map.Entry<String, Host> mapEntry = ((BeanItem<Map.Entry<String, Host>>) container.getItem(s)).getBean();
                HostPanel hostPanel = new HostPanel(mapEntry);

                this.container.addMapChangeListener(hostPanel);

//                HostPanel hostPanel = ((BeanItem<HostPanel>) container.getItem(o)).getBean();
                hostPanel.addClickListener(new MouseEvents.ClickListener() {
                    @Override
                    public void click(MouseEvents.ClickEvent event) {
                        UI.getCurrent().addWindow(new HostDetailWindow(((BeanItem<Map.Entry<String, Host>>) container.getItem(s)).getBean().getValue()));
                    }
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
            container.removeMapChangeListener((HostPanel) c);
        }
    }

}