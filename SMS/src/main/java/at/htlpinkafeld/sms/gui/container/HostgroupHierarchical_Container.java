/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.container;

import at.htlpinkafeld.sms.pojos.Hostgroup;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Container which implements {@link Container.Hierarchical} and represents a
 * Hierarchy with {@link Hostgroup} and its assigned hosts.
 *
 * <p>
 * Hierarchy is like this: null-root - Hostgroup - Hostname.</p>
 *
 * <p>
 * Hostgroups have their {@code name} as itemId and Hosts have
 * {@code hostgroupName+"/"+hostname} as their itemId. The null-root item is
 * null and it's itemId is ""</p>
 *
 * @author Martin Six
 */
public class HostgroupHierarchical_Container extends AbstractContainer implements Container.Hierarchical {

    private final List<Hostgroup> hostgroups;

    /**
     * Collection which contains the containerPropertyIds
     */
    private static final Collection containerPropertyIds = Arrays.asList(new String[]{"Name"});

    /**
     * Constructor for HostgroupHierarchical_Container which uses a {@link List}
     * of {@link Hostgroup Hostgroups}
     *
     * @param hostgroups the hostgroups which are used to create the hierarchy
     */
    public HostgroupHierarchical_Container(List<Hostgroup> hostgroups) {
        this.hostgroups = hostgroups;
    }

    @Override
    public Item getItem(Object itemId) {
        //null Root-Object
        if (String.valueOf(itemId).isEmpty()) {
            return null;
        }

        if (!String.valueOf(itemId).contains("/")) {
            return new BeanItem(hostgroups.stream().filter((t) -> {
                return Objects.equals(itemId, t.getName());
            }).findFirst().orElse(null));
        } else {
            return new BeanItem(String.valueOf(itemId).split("/")[1]);
        }
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return containerPropertyIds;
    }

    @Override
    public Collection<?> getItemIds() {
        LinkedList<String> itemIds = new LinkedList<>();
        for (Hostgroup hostgroup : hostgroups) {
            itemIds.add(hostgroup.getName());
            for (String h : hostgroup.getHostlist()) {
                itemIds.add(hostgroup.getName() + "/" + h);
            }
        }
        return itemIds;
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        if (!String.valueOf(itemId).contains("/")) {
            return new ObjectProperty(String.valueOf(itemId), String.class, true);
        } else {
            return new ObjectProperty(String.valueOf(itemId).split("/")[1], String.class, true);
        }
    }

    @Override
    public Class<?> getType(Object propertyId) {
        return String.class;
    }

    @Override
    public int size() {
        int size = 1;
        for (Hostgroup h : hostgroups) {
            size += h.getHostlist().size() + 1;
        }
        return size;
    }

    @Override
    public boolean containsId(Object itemId) {
        if (String.valueOf(itemId).isEmpty()) {
            return true;
        }

        if (!String.valueOf(itemId).contains("/")) {
            for (Hostgroup hostgroup : hostgroups) {
                if (hostgroup.getName().equals(itemId)) {
                    return true;
                }
            }
        }

        for (Hostgroup hostgroup : hostgroups) {
            for (String h : hostgroup.getHostlist()) {
                if (String.valueOf(itemId).equals(hostgroup.getName() + "/" + h)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Adds a new Hostgroup to the container
     *
     * @param hostgroup the Hostgroup which will be added
     */
    public void addHostgroup(Hostgroup hostgroup) {
        this.hostgroups.add(hostgroup);
        fireItemSetChange();
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeItem(Object itemId) {
        if (String.valueOf(itemId).contains("/")) {
            for (Hostgroup hostgroup : hostgroups) {
                if (String.valueOf(itemId).split("/")[0].equals(hostgroup.getName())) {
                    fireItemSetChange();
                    return hostgroup.getHostlist().remove(String.valueOf(itemId).split("/")[1]);
                }
            }
        } else {
//            Iterator<Hostgroup> hostGIterator = hostgroups.iterator();
//            while (hostGIterator.hasNext()) {
//                if (String.valueOf(itemId).equals(hostGIterator.next().getName())) {
//                    hostGIterator.remove();
//                }
//            }
            Hostgroup deleteHostgroup = null;
            for (Hostgroup hostgroup : hostgroups) {
                if (hostgroup.getName().equals(itemId)) {
                    deleteHostgroup = hostgroup;
                }
            }
            if (hostgroups.remove(deleteHostgroup)) {
                fireItemSetChange();
            }
        }
        return true;
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
        Collection<String> childIds = new LinkedList();
        if (String.valueOf(itemId).isEmpty()) {
            for (Hostgroup hostgroup : hostgroups) {
                childIds.add(hostgroup.getName());
            }
        } else if (!String.valueOf(itemId).contains("/")) {
            for (Hostgroup hostgroup : hostgroups) {
                if (String.valueOf(itemId).equals(hostgroup.getName())) {
                    for (String hostN : hostgroup.getHostlist()) {
                        childIds.add(hostgroup.getName() + "/" + hostN);
                    }
                }
            }

        } else {
            return null;
        }
        return childIds;
    }

    @Override
    public Object getParent(Object itemId) {
        if (String.valueOf(itemId).contains("/")) {
            return String.valueOf(itemId).split("/")[0];
        } else if (!String.valueOf(itemId).isEmpty()) {
            return "";
        } else {
            return null;
        }

    }

    @Override
    public Collection<?> rootItemIds() {
        return Arrays.asList("");
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        return !String.valueOf(itemId).contains("/");
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRoot(Object itemId) {
        return String.valueOf(itemId).isEmpty();
    }

    @Override
    public boolean hasChildren(Object itemId) {
        if (String.valueOf(itemId).isEmpty()) {
            return true;
        }

        if (!String.valueOf(itemId).contains("/")) {
            for (Hostgroup hostgroup : hostgroups) {
                if (String.valueOf(itemId).equals(hostgroup.getName())) {
                    return !hostgroup.getHostlist().isEmpty();
                }
            }
        }
        return false;
    }

    @Override
    public void fireItemSetChange() {
        super.fireItemSetChange();
    }

}
