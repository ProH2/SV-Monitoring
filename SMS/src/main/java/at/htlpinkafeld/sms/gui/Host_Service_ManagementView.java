/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.gui.window.EditHostgroupWindow;
import at.htlpinkafeld.sms.gui.window.NewHostServiceWindow;
import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.gui.container.HostServiceHierarchical_Container;
import at.htlpinkafeld.sms.gui.container.HostgroupHierarchical_Container;
import at.htlpinkafeld.sms.pojo.Host;
import at.htlpinkafeld.sms.pojo.Service;
import at.htlpinkafeld.sms.service.AddHostsAndServices_Service;
import at.htlpinkafeld.sms.service.NoUserLoggedInException;
import at.htlpinkafeld.sms.service.PermissionService;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.AbsoluteLayout;
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
        try {
            if (PermissionService.isAdmin()) {

            } else {
                ((SMS_Main) UI.getCurrent()).navigateTo(OverviewView.VIEW_NAME);
            }
        } catch (NoUserLoggedInException ex) {
            //redirect not logged in Users to the Login-Page
            ((SMS_Main) UI.getCurrent()).navigateTo(LoginView.VIEW_NAME);
        }

        ((SMS_Main) UI.getCurrent()).getMenuBarComponent().switchStyle();
    }

    /**
     * Factory Method to create the Panel for the Host/Service-Management Tab
     *
     * @return The Panel for the Host/Service-Management Tab
     */
    private Panel createHostServiceManagementPanel() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setHeight(UI.getCurrent().getPage().getBrowserWindowHeight() - 38 * 2, Unit.PIXELS);

        TreeTable hostService_treeTable = new TreeTable();

        HostServiceHierarchical_Container hostServiceHierarchical_Container = ContainerFactory.createHostServiceHierarchical_Container();

        hostService_treeTable.setContainerDataSource(hostServiceHierarchical_Container);
        hostService_treeTable.setSizeFull();

        hostService_treeTable.addGeneratedColumn("Create Service", (Table source, Object itemId, Object columnId) -> {
            if (!String.valueOf(itemId).isEmpty()) {
                BeanItem beanItem = (BeanItem) source.getItem(itemId);
                if (beanItem != null) {
                    Object bean = beanItem.getBean();
                    if (bean instanceof Host) {
                        return new Button("Create Service", (Button.ClickEvent event) -> {
                            UI.getCurrent().addWindow(new NewHostServiceWindow((Host) bean, hostServiceHierarchical_Container));
                        });
                    }
                }
            }
            return "";
        });

        hostService_treeTable.addGeneratedColumn("Remove", (Table source, Object itemId, Object columnId) -> {
            if (!String.valueOf(itemId).isEmpty()) {
                BeanItem beanItem = (BeanItem) source.getItem(itemId);
                if (beanItem != null) {
                    Object bean = beanItem.getBean();
                    if (bean instanceof Host) {
                        return new Button("Remove Host", (Button.ClickEvent event) -> {
                            source.removeItem(itemId);
                            Page.getCurrent().reload();
                        });
                    } else if (bean instanceof Service) {
                        return new Button("Remove Service", (Button.ClickEvent event) -> {
                            source.removeItem(itemId);
                            Page.getCurrent().reload();
                        });
                    }
                }
            }
            return "";
        });

        hostService_treeTable.setCellStyleGenerator((Table source, Object itemId, Object propertyId) -> {
            if (String.valueOf(itemId).isEmpty()) {
                return "hiderow";
            } else {
                return null;
            }
        });
        hostService_treeTable.setCollapsed("", false);

        mainLayout.addComponent(hostService_treeTable);
        mainLayout.setExpandRatio(hostService_treeTable, 90);

        AbsoluteLayout buttonLayout = new AbsoluteLayout();
        buttonLayout.setSizeFull();

        buttonLayout.addComponent(new Button("Create Host", (Button.ClickEvent event) -> {
            UI.getCurrent().addWindow(new NewHostServiceWindow(null, hostServiceHierarchical_Container));
        }));

        buttonLayout.addComponent(new Button("Restart Nagios", (event) -> {
            AddHostsAndServices_Service.restartNagios();
        }), "right:0px");

        mainLayout.addComponent(buttonLayout);
        mainLayout.setExpandRatio(buttonLayout, 10);

        return new Panel(mainLayout);
    }

    /**
     * Factory Method to create the Panel for the Hostgroup-Management Tab
     *
     * @return The Panel for the Hostgroup-Management Tab
     */
    private Panel createHostgroupManagementPanel() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setHeight(UI.getCurrent().getPage().getBrowserWindowHeight() - 38 * 2, Unit.PIXELS);

        TreeTable hostgroup_treeTable = new TreeTable();

        HostgroupHierarchical_Container hostgroupHierarchical_Container = ContainerFactory.createHostgroupHierarchical_Container();
        //TODO auto Sorting (with Hosts inside)

        hostgroup_treeTable.setContainerDataSource(hostgroupHierarchical_Container);
        hostgroup_treeTable.setSizeFull();
        hostgroup_treeTable.setBuffered(false);

        hostgroup_treeTable.addGeneratedColumn("Edit Hostgroup", (Table source, Object itemId, Object columnId) -> {
            if (!String.valueOf(itemId).isEmpty() && !String.valueOf(itemId).contains("/")) {
                return new Button("Edit Hostgroup", (Button.ClickEvent event) -> {
                    UI.getCurrent().addWindow(new EditHostgroupWindow(hostgroupHierarchical_Container, String.valueOf(itemId)));
                });
            }
            return "";
        });

        hostgroup_treeTable.addGeneratedColumn("Remove", (Table source, Object itemId, Object columnId) -> {
            if (!String.valueOf(itemId).isEmpty()) {
                if (!String.valueOf(itemId).contains("/")) {
                    return new Button("Delete Hostgroup", (Button.ClickEvent event) -> {
                        source.removeItem(itemId);
                        Page.getCurrent().reload();
                    });
                } else if (String.valueOf(itemId).contains("/")) {
                    return new Button("Remove Host", (Button.ClickEvent event) -> {
                        source.removeItem(itemId);
                        Page.getCurrent().reload();
                    });
                }
            }
            return "";
        });

        hostgroup_treeTable.setCellStyleGenerator((Table source, Object itemId, Object propertyId) -> {
            if (String.valueOf(itemId).isEmpty()) {
                return "hiderow";
            } else {
                return null;
            }
        });
        hostgroup_treeTable.setCollapsed("", false);

        mainLayout.addComponent(hostgroup_treeTable);
        mainLayout.setExpandRatio(hostgroup_treeTable, 90);

        Button createHostButton = new Button("Create Hostgroup", (Button.ClickEvent event) -> {
            UI.getCurrent().addWindow(new EditHostgroupWindow(hostgroupHierarchical_Container, null));
        });

        mainLayout.addComponent(createHostButton);
        mainLayout.setExpandRatio(createHostButton, 10);

        return new Panel(mainLayout);
    }

}
