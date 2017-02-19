/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.container;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link  AbstractContainer} which internaly uses a Map which may be updated.
 * Updates via put or replace will transfer to the item.
 *
 * @author Martin Six
 * @param <V> Type of Map Value
 */
public class MapReferenceContainer<V> extends AbstractContainer implements Container.Filterable, Container.ItemSetChangeNotifier {

    private final HashMapWithListeners<String, V> internalContainerMap;

    private final Set itemProperties;
    private final Class valueClass;

    /**
     * Set of {@link Filter}
     */
    private final Set<Filter> filters;

    /**
     * Constructor for {@link MapReferenceContainer}
     *
     * @param internalMap The map which will be contained inside the container.
     * @param valueClass Class-Object of the stored object type. Used to create
     * the itemPropertyIds.
     * @throws IllegalAccessException if the class {@link valueClass} cannot be
     * instantiated via default-constructor
     * @throws InstantiationException if the class {@link valueClass} cannot be
     * instantiated via default-constructor
     */
    public MapReferenceContainer(HashMapWithListeners<String, V> internalMap, Class<V> valueClass) throws IllegalAccessException, InstantiationException {
        this.internalContainerMap = internalMap;
        this.valueClass = valueClass;
        itemProperties = new HashSet(new BeanItem(valueClass.newInstance()).getItemPropertyIds());
        filters = new HashSet<>();
    }

    /**
     * Gets the Item with the given Item ID from the Container. The Item is a
     * {@link BeanItem} which encapsulates a {@link AbstractMap.SimpleEntry} of
     * the types String, V. If the Container does not contain the requested Item
     * or it is filtered out, null is returned.
     */
    @Override
    public Item getItem(Object itemId) {
        Item item = null;
        Item filterItem = null;
        for (Map.Entry entry : internalContainerMap.entrySet()) {
            if (Objects.equals(entry.getKey(), itemId)) {
                item = new BeanItem(new AbstractMap.SimpleEntry<>(entry));
                filterItem = new BeanItem(entry.getValue());
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
        List<String> filteredSet = new ArrayList();
        boolean filterSuccess = true;
        for (Map.Entry<String, V> o : internalContainerMap.entrySet()) {
            for (Filter f : filters) {
                if (!f.passesFilter(o.getKey(), new BeanItem(o.getValue()))) {
                    filterSuccess = false;
                    break;
                }
            }
            if (filterSuccess) {
                filteredSet.add(o.getKey());
            }
            filterSuccess = true;
        }
        return filteredSet;
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        return new BeanItem<>(this.internalContainerMap.get(String.valueOf(itemId))).getItemProperty(propertyId);
    }

    @Override
    public Class<?> getType(Object propertyId) {
        return itemProperties.contains(propertyId) ? propertyId.getClass() : null;
    }

    @Override
    public int size() {
        return this.internalContainerMap.size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return internalContainerMap.containsKey(String.valueOf(itemId));
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

    /**
     * Delegates to
     * {@link HashMapWithListeners#addListener(at.htlpinkafeld.sms.gui.container.HashMapWithListeners.MapChangeListener)}
     *
     * @see
     * HashMapWithListeners#addListener(at.htlpinkafeld.sms.gui.container.HashMapWithListeners.MapChangeListener)
     * @param listener {@link HashMapWithListeners.MapChangeListener} which will
     * be registered
     */
    public void addMapChangeListener(HashMapWithListeners.MapChangeListener listener) {
        internalContainerMap.addListener(listener);
    }

    /**
     * Delegates to
     * {@link HashMapWithListeners#removeListener(at.htlpinkafeld.sms.gui.container.HashMapWithListeners.MapChangeListener)}
     *
     * @see
     * HashMapWithListeners#removeListener(at.htlpinkafeld.sms.gui.container.HashMapWithListeners.MapChangeListener)
     * @param listener {@link HashMapWithListeners.MapChangeListener} which will
     * be removed
     */
    public void removeMapChangeListener(HashMapWithListeners.MapChangeListener listener) {
        internalContainerMap.removeListener(listener);
    }

    /**
     * Delegates to
     * {@link HashMapWithListeners#removeAllListeners(java.util.Collection) }
     *
     * @see HashMapWithListeners#removeAllListeners(java.util.Collection)
     * @param listeners Collection of
     * {@link HashMapWithListeners.MapChangeListener} which will be removed
     */
    public void addMapChangeListeners(Collection<? extends HashMapWithListeners.MapChangeListener> listeners) {
        internalContainerMap.addAllListeners(listeners);
    }

    /**
     * Delegates to
     * {@link HashMapWithListeners#addAllListeners(java.util.Collection)}
     *
     * @see HashMapWithListeners#addAllListeners(java.util.Collection)
     * @param listeners Collection of
     * {@link HashMapWithListeners.MapChangeListener} which will be added
     */
    public void removeMapChangeListeners(Collection<? extends HashMapWithListeners.MapChangeListener> listeners) {
        internalContainerMap.removeAllListeners(listeners);
    }

    /**
     * Delegates to {@link HashMapWithListeners#fireMapChanged()}
     *
     * @see HashMapWithListeners#fireMapChanged()
     */
    public void fireMapChanged() {
        internalContainerMap.fireMapChanged();
    }

}
