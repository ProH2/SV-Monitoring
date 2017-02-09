/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.gui.util.HostServiceHierarchical_Container;
import at.htlpinkafeld.sms.pojos.Host;
import at.htlpinkafeld.sms.pojos.Service;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.Arrays;

/**
 *
 * @author Martin Six
 */
public class Host_Service_ManagementView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "host_service_managementview";

    public Host_Service_ManagementView() {
        super.addComponent(new MenuBarComponent());

        TreeTable treeTable = new TreeTable();

        HostServiceHierarchical_Container hostServiceHierarchical_Container = new HostServiceHierarchical_Container(OverviewView.createIndexedContainer(Host.class).getItemIds(), OverviewView.createIndexedContainer(Service.class).getItemIds());

        treeTable.setContainerDataSource(hostServiceHierarchical_Container);
        treeTable.setSizeFull();

        treeTable.addGeneratedColumn("new Service", new Table.ColumnGenerator() {
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

        treeTable.addGeneratedColumn("remove", new Table.ColumnGenerator() {
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

        
        //TODO Hide first row
//        treeTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
//            @Override
//            public String getStyle(Table source, Object itemId, Object propertyId) {
//                if (propertyId == null) {
//                    if (String.valueOf(itemId).isEmpty()) {
//                        return "hiderow";
//                    } else {
//                        return null;
//                    }
//                } else {
//                    return null;
//                }
//            }
//        });

        treeTable.setCollapsed("", false);

        super.addComponent(treeTable);

        Button createHostButton = new Button("Create Host", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().addWindow(new NewHostServiceWindow(null));
            }
        });

        super.addComponent(createHostButton);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
