/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.gui.window.ServiceDetailWindow;
import at.htlpinkafeld.sms.gui.OverviewView;
import at.htlpinkafeld.sms.gui.container.MapReferenceContainer;
import at.htlpinkafeld.sms.pojos.Service;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.server.Extension;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.vaadin.addons.stackpanel.StackPanel;

/**
 * Panel which represents the {@link Service Services}. It is used as a
 * {@link TabSheet.Tab} in {@link OverviewView}
 *
 * @author Martin Six
 */
public class ServiceOverviewTabPanel extends Panel implements OverviewTabPanel, DetachListener {

    /**
     * Layout which contains the {@link StackPanel Service-StackPanels}
     */
    private VerticalLayout serviceVerticalLayout = null;

    private final MapReferenceContainer<Service> serviceReferenceContainer;

    /**
     * Constant for the width of the CustomComponent which contains the Data
     * from the Servicepoints. It is used to calculate the amount of columns on
     * the page
     *
     */
    public static final int COMPONENT_WIDTH = 300;

    /**
     * Constructor for the {@link ServiceOverviewTabPanel}
     *
     * @param container which contains the {@link Service Services}
     */
    public ServiceOverviewTabPanel(MapReferenceContainer<Service> container) {

        this.serviceReferenceContainer = container;
//        this.container.sort(new String[]{"status"}, new boolean[]{false});

        this.serviceReferenceContainer.addItemSetChangeListener((Container.ItemSetChangeEvent event) -> {
            refreshLayout();
        });

        VerticalLayout parentVerticalLayout = new VerticalLayout();

        Map<String, String> filterMap = new HashMap<>();
        filterMap.put("Host", "hostname");
        filterMap.put("Service", "name");

        SearchComponent searchComponent = new SearchComponent(filterMap, container);
        parentVerticalLayout.addComponent(searchComponent);
        parentVerticalLayout.setComponentAlignment(searchComponent, Alignment.MIDDLE_RIGHT);

        serviceVerticalLayout = new VerticalLayout();
        serviceVerticalLayout.setSizeFull();

        parentVerticalLayout.addComponent(serviceVerticalLayout);

        super.setContent(parentVerticalLayout);
    }

    @Override
    final public void refreshLayout() {
        Collection<String> itemIdList = serviceReferenceContainer.getItemIds();
        if (serviceVerticalLayout != null) {
            serviceVerticalLayout.removeAllComponents();

            Set<String> hostNameSet = new HashSet<>();
            for (String s : itemIdList) {
                if (s != null) {
                    hostNameSet.add(s.split("/")[0]);
                }
            }

            for (String hostname : hostNameSet) {

                Map<Service.Servicestatus, Integer> statusCountMap = new HashMap<>();
                for (Service.Servicestatus status : Service.Servicestatus.values()) {
                    statusCountMap.put(status, 0);
                }

                GridLayout serviceGridLayout = new GridLayout((UI.getCurrent().getPage().getBrowserWindowWidth() - 37 - 10) / (COMPONENT_WIDTH + 12), 1);
                serviceGridLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

                for (String o : itemIdList) {
                    Map.Entry<String, Service> entry = ((BeanItem<Map.Entry<String, Service>>) serviceReferenceContainer.getItem(o)).getBean();
                    if ((entry).getValue().getHostname().equals(hostname)) {

                        ServicePanel servicePanel = new ServicePanel(entry, serviceReferenceContainer);

                        this.serviceReferenceContainer.addMapChangeListener(servicePanel);

                        servicePanel.addClickListener((MouseEvents.ClickEvent event) -> {
                            ServiceDetailWindow serviceDetailWindow = new ServiceDetailWindow(entry, serviceReferenceContainer);
                            serviceReferenceContainer.addMapChangeListener(serviceDetailWindow);
                            UI.getCurrent().addWindow(serviceDetailWindow);
                        });
                        statusCountMap.put(entry.getValue().getStatus(), statusCountMap.get(entry.getValue().getStatus()) + 1);
                        serviceGridLayout.addComponent(servicePanel);
                        serviceGridLayout.addDetachListener(this);
                    }
                }
                serviceGridLayout.setSizeFull();
                serviceGridLayout.setMargin(false);

                //AbsoluteLayout to show Data in the top-Span
                AbsoluteLayout absoluteLayout = new AbsoluteLayout();
                absoluteLayout.setHeight(37, Unit.PIXELS);

                Panel servicesWrapperPanel = new Panel(hostname, serviceGridLayout);

                StackPanel sp = StackPanel.extend(servicesWrapperPanel);
                sp.addToggleListener((StackPanel source) -> {
                    if (source.isOpen()) {
                        absoluteLayout.setHeight(38 + (serviceGridLayout.getRows() * 180), Unit.PIXELS);
                    } else {
                        absoluteLayout.setHeight(37, Unit.PIXELS);
                    }
                });
                sp.close();

                absoluteLayout.addComponent(servicesWrapperPanel);

                HorizontalLayout statusesLayout = new HorizontalLayout();

                for (Map.Entry entry : statusCountMap.entrySet()) {
                    TextField statusField = new TextField(null, entry.getKey() + ": " + entry.getValue());
                    statusField.setReadOnly(true);
                    statusField.setWidth(140, Unit.PIXELS);

                    statusField.addStyleName("passclick");
                    statusesLayout.addComponent(statusField);
                }
                statusesLayout.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
                    for (Extension e : servicesWrapperPanel.getExtensions()) {
                        if (e instanceof StackPanel) {
                            StackPanel sp1 = (StackPanel) e;
                            //TODO find better solution (not 2 setHeight)
                            if (sp1.isOpen()) {
                                sp1.close();
                                absoluteLayout.setHeight(37, Unit.PIXELS);
                            } else {
                                absoluteLayout.setHeight(38 + (serviceGridLayout.getRows() * 180), Unit.PIXELS);
                                sp1.open();
                            }
                        }
                    }
                });

                absoluteLayout.addComponent(statusesLayout, "left: 30%;");

                serviceVerticalLayout.addComponent(absoluteLayout);
            }
        }
    }

    @Override
    public void detach(DetachEvent event) {
        if (event.getSource().equals(this)) {
            GridLayout gridLayout = (GridLayout) event.getSource();
            for (Component c : gridLayout) {
                serviceReferenceContainer.removeMapChangeListener((ServicePanel) c);
            }
        }

    }

}
