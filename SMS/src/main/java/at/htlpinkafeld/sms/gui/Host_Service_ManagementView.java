/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.gui.container.HostgroupHierarchical_Container;
import at.htlpinkafeld.sms.pojos.Host;
import at.htlpinkafeld.sms.pojos.Service;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * View for the Management of Host and Services
 *
 * @author Martin Six
 */
public class Host_Service_ManagementView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "host_service_managementview";

    /**
     * Constructor for Host_Service_ManagementView
     */
    public Host_Service_ManagementView() {
        super.addComponent(((SMS_Main) UI.getCurrent()).getMenuBarComponent());

        TabSheet tabsheet = new TabSheet();

        tabsheet.addTab(createHostgroupManagementPanel(), "Hostgroups");
        tabsheet.addTab(createHostServiceManagementPanel(), "Host/Service");

        super.addComponent(tabsheet);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        ((SMS_Main) UI.getCurrent()).getMenuBarComponent().switchStyle();
    }

    /**
     * Factory Method to create the Panel for the Host/Service-Management Tab
     *
     * @return The Panel for the Host/Service-Management Tab
     */
    private Panel createHostServiceManagementPanel() {
        VerticalLayout mainLayout = new VerticalLayout();

        TreeTable hostService_treeTable = new TreeTable();

        Container.Hierarchical hostServiceHierarchical_Container = ContainerFactory.createHostServiceHierarchical_Container();

        hostService_treeTable.setContainerDataSource(hostServiceHierarchical_Container);
        hostService_treeTable.setSizeFull();

        hostService_treeTable.addGeneratedColumn("new Service", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                if (!String.valueOf(itemId).isEmpty()) {
                    BeanItem beanItem = (BeanItem) source.getItem(itemId);
                    if (beanItem != null) {
                        Object bean = beanItem.getBean();
                        if (bean instanceof Host) {
                            return new Button("Create Service", new Button.ClickListener() {
                                @Override
                                public void buttonClick(Button.ClickEvent event) {
                                    UI.getCurrent().addWindow(new NewHostServiceWindow((Host) bean));
                                }
                            });
                        }
                    }
                }
                return "";
            }
        });

        hostService_treeTable.addGeneratedColumn("remove", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                if (!String.valueOf(itemId).isEmpty()) {
                    BeanItem beanItem = (BeanItem) source.getItem(itemId);
                    if (beanItem != null) {
                        Object bean = beanItem.getBean();
                        if (bean instanceof Host) {
                            return new Button("Remove Host", new Button.ClickListener() {
                                @Override
                                public void buttonClick(Button.ClickEvent event) {
                                    source.removeItem(itemId);
                                    source.refreshRowCache();
                                }
                            });
                        } else if (bean instanceof Service) {
                            return new Button("Remove Service", new Button.ClickListener() {
                                @Override
                                public void buttonClick(Button.ClickEvent event) {
                                    source.removeItem(itemId);
                                    source.refreshRowCache();
                                }
                            });
                        }
                    }
                }
                return "";
            }
        });

        hostService_treeTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                if (String.valueOf(itemId).isEmpty()) {
                    return "hiderow";
                } else {
                    return null;
                }
            }
        });
        hostService_treeTable.setCollapsed("", false);

        mainLayout.addComponent(hostService_treeTable);

        Button createHostButton = new Button("Create Host", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().addWindow(new NewHostServiceWindow(null));
            }
        });

        mainLayout.addComponent(createHostButton);

        return new Panel(mainLayout);
    }

    /**
     * Factory Method to create the Panel for the Hostgroup-Management Tab
     *
     * @return The Panel for the Hostgroup-Management Tab
     */
    private Panel createHostgroupManagementPanel() {
        VerticalLayout mainLayout = new VerticalLayout();

        TreeTable hostgroup_treeTable = new TreeTable();

        HostgroupHierarchical_Container hostgroupHierarchical_Container = ContainerFactory.createHostgroupHierarchical_Container();

        hostgroup_treeTable.setContainerDataSource(hostgroupHierarchical_Container);
        hostgroup_treeTable.setSizeFull();

        hostgroup_treeTable.addGeneratedColumn("edit Hostgroup", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                if (!String.valueOf(itemId).isEmpty() && !String.valueOf(itemId).contains("/")) {
                    return new Button("edit Hostgroup", new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            UI.getCurrent().addWindow(new EditHostgroupWindow(hostgroupHierarchical_Container, String.valueOf(itemId)));
                        }
                    });
                }
                return "";
            }
        });

        hostgroup_treeTable.addGeneratedColumn("remove", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                if (!String.valueOf(itemId).isEmpty()) {
                    if (!String.valueOf(itemId).contains("/")) {
                        return new Button("Delete Hostgroup", (Button.ClickEvent event) -> {
                            source.removeItem(itemId);
                            source.refreshRowCache();
                        });
                    } else if (String.valueOf(itemId).contains("/")) {
                        return new Button("Remove Host", (Button.ClickEvent event) -> {
                            source.removeItem(itemId);
                            source.refreshRowCache();
                        });
                    }
                }
                return "";
            }
        });

        hostgroup_treeTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                if (String.valueOf(itemId).isEmpty()) {
                    return "hiderow";
                } else {
                    return null;
                }
            }
        });
        hostgroup_treeTable.setCollapsed("", false);

        mainLayout.addComponent(hostgroup_treeTable);

        Button createHostButton = new Button("Create Hostgroup", (Button.ClickEvent event) -> {
            UI.getCurrent().addWindow(new EditHostgroupWindow(hostgroupHierarchical_Container, null));
        });

        mainLayout.addComponent(createHostButton);

        return new Panel(mainLayout);
    }

}
