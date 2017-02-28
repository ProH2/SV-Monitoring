/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.container;

import at.htlpinkafeld.sms.pojos.Host;
import at.htlpinkafeld.sms.pojos.Service;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Container which implements {@link Container.Hierarchical} and represents a
 * Hierarchy with {@link Host} and {@link Service}.
 *
 * <p>
 * Hierarchy is like this: null-root - Host - Service.</p>
 *
 * <p>
 * Hosts have their {@code hostname} as itemId and Services have
 * {@code hostname+"/"+servicename} as their itemId. The null-root item is null
 * and it's itemId is ""</p>
 *
 * @author Martin Six
 */
public class HostServiceHierarchical_Container extends AbstractContainer implements Container.Hierarchical {

    private final Map<String, BeanItem> hostServiceMap;

    /**
     * Collection which contains the containerPropertyIds
     */
    private static final Collection containerPropertyIds = Arrays.asList(new String[]{"Name", "Host-IP", "Status"});

    /**
     * Constructor for the {@link HostServiceHierarchical_Container}, which uses
     * two Collections
     *
     * @param hosts Collection of {@link Host Hosts} which should be used to
     * build the Hierarchy
     * @param services Collection of {@link Service Services} which should be
     * used to build the Hierarchy
     */
    public HostServiceHierarchical_Container(Collection<Host> hosts, Collection<Service> services) {
        hostServiceMap = new HashMap<>();

        hostServiceMap.put("", null);

        for (Host h : hosts) {
            BeanItem beanItem = new BeanItem(h);

            hostServiceMap.put(h.getHostname(), beanItem);
        }
        for (Service s : services) {
            hostServiceMap.put(s.getHostname() + "/" + s.getName(), new BeanItem(s));
        }
    }

    @Override
    public Item getItem(Object itemId) {
        return hostServiceMap.get(String.valueOf(itemId));
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return containerPropertyIds;
    }

    @Override
    public Collection<?> getItemIds() {
        return hostServiceMap.keySet();
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        if (String.valueOf(itemId).isEmpty()) {
            return null;
        }

        BeanItem beanItem = hostServiceMap.get(itemId);
        if (beanItem != null) {
            Object item = beanItem.getBean();
            if (item instanceof Host) {
                switch (String.valueOf(propertyId)) {
                    case "Name":
                        return new ObjectProperty(((Host) item).getHostname());
                    case "Host-IP":
                        //TODO change to IP/Host
                        return new ObjectProperty(((Host) item).getInformation());
                    case "Status":
                        return new ObjectProperty(((Host) item).getStatus());
                    default:
                }
            } else if (item instanceof Service) {
                switch (String.valueOf(propertyId)) {
                    case "Name":
                        return new ObjectProperty(((Service) item).getName());
                    case "Host-IP":
                        return null;
                    case "Status":
                        return new ObjectProperty(((Service) item).getStatus().name());
                    default:
                }
            }
        }
        return null;
    }

    @Override
    public Class<?> getType(Object propertyId) {
        return String.class;
    }

    @Override
    public int size() {
        return this.hostServiceMap.size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return this.hostServiceMap.containsKey(String.valueOf(itemId));
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeItem(Object itemId) {
        //TODO Connection-Point to JSON-Service
        boolean returnV = this.hostServiceMap.remove(String.valueOf(itemId)) != null;
        if (returnV) {
            fireItemSetChange();
        }
        return returnV;
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<?> getChildren(Object itemId) {

        if (String.valueOf(itemId).contains("/")) {
            return Collections.EMPTY_LIST;
        }

        List<String> children = new LinkedList<>();

        if (String.valueOf(itemId).isEmpty()) {
            for (String s : hostServiceMap.keySet()) {
                if (!s.contains("/") && !s.isEmpty()) {
                    children.add(s);
                }
            }
        } else {
            for (String s : hostServiceMap.keySet()) {
                if (!s.equals(itemId)) {
                    if (s.startsWith(itemId + "/")) {
                        children.add(s);
                    }
                }
            }
        }
        return children;
    }

    @Override
    public Object getParent(Object itemId) {
        if (String.valueOf(itemId).isEmpty()) {
            return null;
        } else if (String.valueOf(itemId).contains("/")) {
            return String.valueOf(itemId).split("/")[0];
        } else {
            return "";
        }
    }

    @Override
    public Collection<?> rootItemIds() {
        List<String> rootItemIds = new LinkedList<>();

//        for (String itemId : hostServiceMap.keySet()) {
//            if (!itemId.contains("/")) {
//                rootItemIds.add(itemId);
//            }
//        }
        rootItemIds.add("");

        return rootItemIds;
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        return !String.valueOf(itemId).contains("/") && !String.valueOf(itemId).isEmpty();
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRoot(Object itemId) {
//        return !String.valueOf(itemId).contains("/");
        return String.valueOf(itemId).isEmpty();
    }

    @Override
    public boolean hasChildren(Object itemId) {
        if (String.valueOf(itemId).isEmpty()) {
            return true;
        }

        for (String s : hostServiceMap.keySet()) {
            if (!s.equals(itemId)) {
                if (s.startsWith(itemId + "/")) {
                    return true;
                }
            }
        }
        return false;
    }

}
