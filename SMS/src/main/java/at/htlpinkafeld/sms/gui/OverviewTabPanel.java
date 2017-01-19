/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.gui.overviewComponents.HostPanel;
import at.htlpinkafeld.sms.gui.overviewComponents.ServicePanel;
import at.htlpinkafeld.sms.pojos.Host;
import at.htlpinkafeld.sms.pojos.Service;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.vaadin.addons.stackpanel.StackPanel;

/**
 *
 * @author Martin Six
 */
//TODO: Filter implementieren, LazyQueryContainer statt BeanItemContainer verwenden
public final class OverviewTabPanel extends Panel {

    private AutoResizingGridLayout gridLayout = null;
    private VerticalLayout serviceVerticalLayout = null;

    private BeanItemContainer container;

    private TextField searchTermField;
    private NativeSelect filterSelect;
    private List<String> searchFilterList;

    /**
     * Constant for the width of the CustomComponent which contains the Data
     * from the Servicepoints. It is used to calculate the amount of columns on
     * the page
     *
     */
    public static final int COMPONENT_WIDTH = 300;

    public OverviewTabPanel(BeanItemContainer container, String... filterTypes) {
        if (container.getBeanType() != Service.class) {
            this.gridLayout = new AutoResizingGridLayout(COMPONENT_WIDTH);
        }
        this.container = container;
        this.container.sort(new String[]{"status"}, new boolean[]{false});

        searchFilterList = new LinkedList<>();
        for (String filterType : filterTypes) {
            searchFilterList.add(filterType);
        }

        filterSelect = new NativeSelect();

        for (String filterId : searchFilterList) {
            filterSelect.addItem(filterId);
        }

        filterSelect.select(filterSelect.getItemIds().toArray()[0]);
        filterSelect.setNullSelectionAllowed(false);

        searchTermField = new TextField();
        searchTermField.setSizeFull();

        Button searchButton = new Button("Suchen", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                container.removeAllContainerFilters();

                String searchTerm = searchTermField.getValue();
                if (!searchTerm.isEmpty()) {
                    String filterType = filterSelect.getValue().toString();
                    //TODO get it working
                    if (searchFilterList.contains(filterType)) {
                        Filter f = null;
                        if (container.getBeanType() == Host.class) {
                            f = new SimpleStringFilter("hostname", searchTerm, true, false);
                        } else if (container.getBeanType() == Service.class) {
                            f = new SimpleStringFilter("name", searchTerm, true, false);
                        }

                        container.addContainerFilter(f);
                    } else {
                        Notification.show("Some Error occured during searching", Notification.Type.ERROR_MESSAGE);
                    }
                }
                refreshLayout();
            }
        });

        VerticalLayout parentVerticalLayout = new VerticalLayout();

        HorizontalLayout searchLayout = new HorizontalLayout(filterSelect, searchTermField, searchButton);
        searchLayout.setMargin(new MarginInfo(true, true, false, false));

        parentVerticalLayout.addComponent(searchLayout);
        parentVerticalLayout.setComponentAlignment(searchLayout, Alignment.MIDDLE_RIGHT);

        if (container.getBeanType() != Service.class) {
            this.gridLayout = new AutoResizingGridLayout(COMPONENT_WIDTH);
            parentVerticalLayout.addComponent(gridLayout);
        } else {
            serviceVerticalLayout = new VerticalLayout();
            serviceVerticalLayout.setSizeFull();

            parentVerticalLayout.addComponent(serviceVerticalLayout);
        }

        super.setContent(parentVerticalLayout);
    }

    /**
     * Recreates the main Content of the Tab
     */
    public void refreshLayout() {
        Collection itemList = container.getItemIds();
        if (gridLayout != null) {
            gridLayout.removeAllComponents();

            //TODO: Richtige Komponenten
            for (Object o : itemList) {
                Panel compPanel;
                //create Panel depending on Container-Type (Service currently not in use)
                if (container.getBeanType() == Host.class) {
                    compPanel = new HostPanel((Host) o);
                } else if (container.getBeanType() == Service.class) {
                    compPanel = new ServicePanel((Service) o);
                } else {
                    compPanel = new HostPanel(new Host(0, o.toString(), (Host.Hoststatus) Host.Hoststatus.DOWN, LocalDateTime.now(), Duration.ZERO, "Test informationTest informationTest tionTest informationTest informationonTest informationTest information"));
                }

                compPanel.setWidth(COMPONENT_WIDTH, Unit.PIXELS);
                gridLayout.addComponent(compPanel);
            }

            gridLayout.markAsDirty();
        } else if (serviceVerticalLayout != null) {
            serviceVerticalLayout.removeAllComponents();

            Set<Integer> hostNrSet = new HashSet<>();
            for (Object o : itemList) {
                hostNrSet.add(((Service) o).getHostnr());
            }

            for (Integer hostNr : hostNrSet) {
                AutoResizingGridLayout argl = new AutoResizingGridLayout(COMPONENT_WIDTH);
                for (Object o : itemList) {
                    if (((Service) o).getHostnr().equals(hostNr)) {
                        argl.addComponent(new ServicePanel((Service) o));
                    }
                }
                argl.setSizeFull();
                argl.setMargin(false);

                AbsoluteLayout absoluteLayout = new AbsoluteLayout();
                absoluteLayout.setHeight(37, Unit.PIXELS);

                Panel servicesWrapperPanel = new Panel(hostNr.toString() + "Test", argl);

                StackPanel sp = StackPanel.extend(servicesWrapperPanel);
                sp.addToggleListener(new StackPanel.ToggleListener() {
                    @Override
                    public void toggleClick(StackPanel source) {
                        if (source.isOpen()) {
                            absoluteLayout.setHeight(218, Unit.PIXELS);
                        } else {
                            absoluteLayout.setHeight(37, Unit.PIXELS);
                        }
                    }
                });
                sp.close();

                absoluteLayout.addComponent(servicesWrapperPanel);

                TextField l = new TextField(null, "OK: 1   PASS : 3");
                l.setReadOnly(true);
                l.setSizeUndefined();
                l.addStyleName("passclick");
                absoluteLayout.addComponent(l, "right: 50%;");

                serviceVerticalLayout.addComponent(absoluteLayout);
            }
        }
    }

    /**
     * used to expose the {@link AutoResizingGridLayout } as a
     * {@link  Page.BrowserWindowResizeListener} to {@link OverviewView}
     *
     * @return
     */
    public AutoResizingGridLayout getGridLayout() {
        return gridLayout;
    }

}
