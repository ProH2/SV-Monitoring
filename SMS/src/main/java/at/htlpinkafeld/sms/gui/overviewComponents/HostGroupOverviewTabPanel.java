/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.gui.OverviewView;
import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.gui.container.MapReferenceContainer;
import at.htlpinkafeld.sms.pojos.Host;
import at.htlpinkafeld.sms.pojos.Hostgroup;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
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
import java.util.Map;
import org.vaadin.addons.stackpanel.StackPanel;

/**
 * Panel which represents the {@link Hostgroup Hostgroups}. It is used as a
 * {@link TabSheet.Tab} in {@link OverviewView}
 *
 * @author Martin Six
 */
public class HostGroupOverviewTabPanel extends Panel implements OverviewTabPanel, DetachListener {

    private VerticalLayout hostGroupVerticalLayout = null;
    private BeanItemContainer<Hostgroup> hostgroupContainer;
    private final MapReferenceContainer<Host> hostReferenceContainer;

    /**
     * Constant for the width of the CustomComponent which contains the Data
     * from the Servicepoints. It is used to calculate the amount of columns on
     * the page
     *
     */
    public static final int COMPONENT_WIDTH = 300;

    /**
     * Constructor for the {@link HostGroupOverviewTabPanel}
     *
     * @param hostgroupContainer which contains the {@link Hostgroup Hostgroups}
     * @param hostReferenceContainer which contains the {@link Host Hosts}
     */
    public HostGroupOverviewTabPanel(BeanItemContainer<Hostgroup> hostgroupContainer, MapReferenceContainer<Host> hostReferenceContainer) {
        this.hostgroupContainer = hostgroupContainer;

        this.hostReferenceContainer = hostReferenceContainer;

        //Todo default sorting
        this.hostgroupContainer.addItemSetChangeListener((Container.ItemSetChangeEvent event) -> {
            refreshLayout();
        });

        VerticalLayout parentVerticalLayout = new VerticalLayout();

        //create Filtermapping
        Map<String, String> filterMap = new HashMap<>();
        filterMap.put("Host Gruppe", "name");

        SearchComponent searchComponent = new SearchComponent(filterMap, hostgroupContainer);
        parentVerticalLayout.addComponent(searchComponent);
        parentVerticalLayout.setComponentAlignment(searchComponent, Alignment.MIDDLE_RIGHT);

        hostGroupVerticalLayout = new VerticalLayout();
        hostGroupVerticalLayout.setSizeFull();

        parentVerticalLayout.addComponent(hostGroupVerticalLayout);

        super.setContent(parentVerticalLayout);

    }

    @Override
    public void attach() {
        super.attach();
        hostgroupContainer = ContainerFactory.createHostgroupContainer();
    }

    @Override
    final public void refreshLayout() {
        Collection<Hostgroup> itemList = hostgroupContainer.getItemIds();

        if (hostGroupVerticalLayout != null) {
            hostGroupVerticalLayout.removeAllComponents();

            for (Hostgroup hostgroup : itemList) {

                //TODO Use Container instead of this
                Map<Host.Hoststatus, Integer> statusCountMap = new HashMap<>();
                for (Host.Hoststatus status : Host.Hoststatus.values()) {
                    statusCountMap.put(status, 0);
                }

                GridLayout hostgroupGridLayout;
                hostgroupGridLayout = new GridLayout((UI.getCurrent().getPage().getBrowserWindowWidth() - 37 - 10) / (COMPONENT_WIDTH + 12), 1);
//                hostgroupGridLayout = new AutoResizingGridLayout(COMPONENT_WIDTH);
                hostgroupGridLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

                for (String hostname : hostgroup.getHostlist()) {
                    BeanItem<Map.Entry<String, Host>> hostMapBeanItem = (BeanItem<Map.Entry<String, Host>>) hostReferenceContainer.getItem(hostname);
                    if (hostMapBeanItem != null) {
                        Map.Entry<String, Host> hostMapEntry = hostMapBeanItem.getBean();
                        HostPanel hostPanel = new HostPanel(hostMapEntry);

                        this.hostReferenceContainer.addMapChangeListener(hostPanel);

                        hostPanel.addClickListener(new MouseEvents.ClickListener() {
                            @Override
                            public void click(MouseEvents.ClickEvent event) {
                                UI.getCurrent().addWindow(new HostDetailWindow(hostMapEntry.getValue()));
                            }
                        });
                        statusCountMap.put(hostMapEntry.getValue().getStatus(), statusCountMap.get(hostMapEntry.getValue().getStatus()) + 1);
                        hostgroupGridLayout.addComponent(hostPanel);
                        hostgroupGridLayout.addDetachListener(this);
                    }
                }

                hostgroupGridLayout.setSizeFull();
                hostgroupGridLayout.setMargin(false);

                //AbsoluteLayout to show Data in the top-Span
                AbsoluteLayout absoluteLayout = new AbsoluteLayout();
                absoluteLayout.setHeight(38 + (hostgroupGridLayout.getRows() * 180), Unit.PIXELS);

                Panel hostsWrapperPanel = new Panel(hostgroup.getName(), hostgroupGridLayout);

                StackPanel sp = StackPanel.extend(hostsWrapperPanel);
                sp.addToggleListener(new StackPanel.ToggleListener() {
                    @Override
                    public void toggleClick(StackPanel source) {
                        if (source.isOpen()) {
                            absoluteLayout.setHeight(38 + (hostgroupGridLayout.getRows() * 180), Unit.PIXELS);
                        } else {
                            absoluteLayout.setHeight(37, Unit.PIXELS);
                        }
                    }
                });

                if (hostgroup.getHostlist().isEmpty()) {
                    sp.close();
                    absoluteLayout.setHeight(37, Unit.PIXELS);
                }

                absoluteLayout.addComponent(hostsWrapperPanel);

                HorizontalLayout statusesLayout = new HorizontalLayout();

                for (Map.Entry entry : statusCountMap.entrySet()) {
                    TextField statusField = new TextField(null, entry.getKey() + ": " + entry.getValue());
                    statusField.setReadOnly(true);
                    statusField.setWidth(150, Unit.PIXELS);

                    statusField.addStyleName("passclick");
                    statusesLayout.addComponent(statusField);
                }
                statusesLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                    @Override
                    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                        for (Extension e : hostsWrapperPanel.getExtensions()) {
                            if (e instanceof StackPanel) {
                                StackPanel sp = (StackPanel) e;
                                //TODO find better solution (not 2 setHeight)
                                if (sp.isOpen()) {
                                    sp.close();
                                    absoluteLayout.setHeight(37, Unit.PIXELS);
                                } else {
                                    absoluteLayout.setHeight(38 + (hostgroupGridLayout.getRows() * 180), Unit.PIXELS);
                                    sp.open();
                                }
                            }
                        }
                    }
                });

                absoluteLayout.addComponent(statusesLayout, "left: 30%;");

                hostGroupVerticalLayout.addComponent(absoluteLayout);
            }
        }
    }

    @Override
    public void detach(DetachEvent event) {
        if (event.getSource().equals(this)) {
            GridLayout gridLayout = (GridLayout) event.getSource();
            for (Component c : gridLayout) {
                this.hostReferenceContainer.removeMapChangeListener((HostPanel) c);
            }
        }

    }

}
