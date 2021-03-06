/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.pojo.Host;
import at.htlpinkafeld.sms.pojo.Service;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.VerticalLayout;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author masix
 */
public class StatusFilterComponent extends VerticalLayout {

    public StatusFilterComponent(Map<? extends Enum, Filter> filterMap, Container.Filterable containerToFilter) {
        VerticalLayout mainLayout = new VerticalLayout();

        for (Map.Entry<? extends Enum, Filter> entry : filterMap.entrySet()) {
            CheckBox statusFiterBox = new CheckBox(entry.getKey().toString(), true);

            statusFiterBox.addValueChangeListener((event) -> {
                if (statusFiterBox.getValue()) {
                    containerToFilter.removeContainerFilter(entry.getValue());
                } else {
                    containerToFilter.addContainerFilter(entry.getValue());
                }
            });
            super.addComponent(statusFiterBox);
            super.setMargin(new MarginInfo(false, true));
        }

    }

    public static Map<Service.Servicestatus, Filter> createServiceStatusFilterMap() {
        Map<Service.Servicestatus, Filter> statusFilterMap = new LinkedHashMap<>();
        statusFilterMap.put(Service.Servicestatus.OK, new Filter() {
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                return !Service.Servicestatus.OK.equals(item.getItemProperty("status").getValue());
            }

            @Override
            public boolean appliesToProperty(Object propertyId) {
                return true;
            }
        });
        statusFilterMap.put(Service.Servicestatus.WARNING, new Filter() {
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                return !Service.Servicestatus.WARNING.equals(item.getItemProperty("status").getValue());
            }

            @Override
            public boolean appliesToProperty(Object propertyId) {
                return true;
            }
        });
        statusFilterMap.put(Service.Servicestatus.CRITICAL, new Filter() {
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                return !Service.Servicestatus.CRITICAL.equals(item.getItemProperty("status").getValue());
            }

            @Override
            public boolean appliesToProperty(Object propertyId) {
                return true;
            }
        });
        statusFilterMap.put(Service.Servicestatus.UNKNOWN, new Filter() {
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                return !Service.Servicestatus.UNKNOWN.equals(item.getItemProperty("status").getValue());
            }

            @Override
            public boolean appliesToProperty(Object propertyId) {
                return true;
            }
        });
        statusFilterMap.put(Service.Servicestatus.PENDING, new Filter() {
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                return !Service.Servicestatus.PENDING.equals(item.getItemProperty("status").getValue());
            }

            @Override
            public boolean appliesToProperty(Object propertyId) {
                return true;
            }
        });
        return statusFilterMap;
    }

    public static Map<Host.Hoststatus, Filter> createHostStatusFilterMap() {
        Map<Host.Hoststatus, Filter> statusFilterMap = new LinkedHashMap<>();
        statusFilterMap.put(Host.Hoststatus.UP, new Filter() {
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                if (item != null && item.getItemPropertyIds().contains("status")) {
                    return !Host.Hoststatus.UP.equals(item.getItemProperty("status").getValue());
                }
                return false;
            }

            @Override
            public boolean appliesToProperty(Object propertyId) {
                return true;
            }
        });
        statusFilterMap.put(Host.Hoststatus.DOWN, new Filter() {
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                if (item != null && item.getItemPropertyIds().contains("status")) {
                    return !Host.Hoststatus.DOWN.equals(item.getItemProperty("status").getValue());
                }
                return false;
            }

            @Override
            public boolean appliesToProperty(Object propertyId) {
                return true;
            }
        });
        statusFilterMap.put(Host.Hoststatus.UNREACHABLE, new Filter() {
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                if (item != null && item.getItemPropertyIds().contains("status")) {
                    return !Host.Hoststatus.UNREACHABLE.equals(item.getItemProperty("status").getValue());
                }
                return false;
            }

            @Override
            public boolean appliesToProperty(Object propertyId) {
                return true;
            }
        });
        statusFilterMap.put(Host.Hoststatus.PENDING, new Filter() {
            @Override
            public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                if (item != null && item.getItemPropertyIds().contains("status")) {
                    return !Host.Hoststatus.PENDING.equals(item.getItemProperty("status").getValue());
                }
                return false;
            }

            @Override
            public boolean appliesToProperty(Object propertyId) {
                return true;
            }
        });
        return statusFilterMap;
    }
}
