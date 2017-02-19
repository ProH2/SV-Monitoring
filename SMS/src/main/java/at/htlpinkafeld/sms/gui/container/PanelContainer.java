/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.container;

import at.htlpinkafeld.sms.gui.overviewComponents.ComponentPanelInterface;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Not currently used - to be deleted
 *
 * @author Martin Six
 * @param <V> Type of Map Value
 */
@Deprecated
public class PanelContainer<V> extends AbstractContainer implements Container.Filterable, Container.ItemSetChangeNotifier {

    private final List<? extends ComponentPanelInterface<V>> panels;

    private final Set itemProperties;
    private final Class valueClass;

    private final Set<Filter> filters;

    public PanelContainer(List<? extends ComponentPanelInterface<V>> panels, Class<V> valueClass) throws IllegalAccessException, InstantiationException {
        this.panels = panels;
        this.valueClass = valueClass;
        itemProperties = new HashSet(new BeanItem(valueClass.newInstance()).getItemPropertyIds());
        filters = new HashSet<>();
    }

    @Override
    public Item getItem(Object itemId) {
        Item item = null;
        Item filterItem = null;
        for (ComponentPanelInterface<V> panel : panels) {
            if (Objects.equals(panel.getValue(), itemId)) {
                item = new BeanItem<>(panel);
                filterItem = new BeanItem(panel.getValue());
            }
        }
        for (Filter f : filters) {
            if (!f.passesFilter(itemId, filterItem)) {
                return null;
            }
        }
        return item;
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return itemProperties;
    }

    @Override
    public List getItemIds() {
        List filteredSet = new ArrayList();
        boolean filterSuccess = true;
        for (ComponentPanelInterface<V> panel : panels) {
            for (Filter f : filters) {
                if (!f.passesFilter(panel.getValue(), new BeanItem(panel.getValue()))) {
                    filterSuccess = false;
                    break;
                }
            }
            if (filterSuccess) {
                filteredSet.add(panel.getValue());
            }
            filterSuccess = true;
        }
        return filteredSet;
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        for (ComponentPanelInterface<V> panel : panels) {
            if (Objects.equals(panel.getValue(), itemId)) {
                return new BeanItem<>(panel.getValue()).getItemProperty(propertyId);
            }
        }
        return null;
    }

    @Override
    public Class<?> getType(Object propertyId) {
        return itemProperties.contains(propertyId) ? propertyId.getClass() : null;
    }

    @Override
    public int size() {
        return this.panels.size();
    }

    @Override
    public boolean containsId(Object itemId) {
        for (ComponentPanelInterface<V> panel : panels) {
            if (Objects.equals(panel.getValue(), itemId)) {
                return true;
            }
        }
        return false;
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
    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void addContainerFilter(Filter filter) throws UnsupportedFilterException {
        filters.add(filter);
        fireItemSetChange();
    }

    @Override
    public void removeContainerFilter(Filter filter) {
        filters.remove(filter);
        fireItemSetChange();
    }

    @Override
    public void removeAllContainerFilters() {
        if (!filters.isEmpty()) {
            filters.clear();
            fireItemSetChange();
        }
    }

    @Override
    public Collection<Filter> getContainerFilters() {
        return Collections.unmodifiableCollection(filters);
    }

    @Override
    public Collection<ItemSetChangeListener> getItemSetChangeListeners() {
        return super.getItemSetChangeListeners(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeItemSetChangeListener(ItemSetChangeListener listener) {
        super.removeItemSetChangeListener(listener); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addItemSetChangeListener(ItemSetChangeListener listener) {
        super.addItemSetChangeListener(listener); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeListener(ItemSetChangeListener listener) {
        super.removeListener(listener); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addListener(ItemSetChangeListener listener) {
        super.addListener(listener); //To change body of generated methods, choose Tools | Templates.
    }

}
