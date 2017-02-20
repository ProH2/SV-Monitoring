/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.container;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * An subclass of {@link LinkedHashMap} which adds listener functionality.
 * Events are only triggered via {@link #fireMapChanged()}.
 *
 * @author Martin Six
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class HashMapWithListeners<K, V> extends LinkedHashMap<K, V> {

    private final List<MapChangeListener> mapListeners = new LinkedList<>();

    /**
     * The Listener for {@link MapChangeListener}. Listener will be notified
     * when {@link #fireMapChanged()} is called.
     */
    public interface MapChangeListener {

        /**
         * Method which is called when {@link #fireMapChanged()} is fired
         */
        public void mapChanged();
    }

    /**
     * Notifies all registered {@link MapChangeListener}
     */
    public void fireMapChanged() {
        for (MapChangeListener mcl : mapListeners) {
            mcl.mapChanged();
        }
    }

    /**
     * Adds an {@link MapChangeListener} to this {@link HashMapWithListeners}
     *
     * @param listener listener which will be registered
     */
    public void addListener(MapChangeListener listener) {
        mapListeners.add(listener);
    }

    /**
     * Tries to remove the specified {@link MapChangeListener} from this
     * {@link HashMapWithListeners}
     *
     * @param listener listener which will be removed
     */
    public void removeListener(MapChangeListener listener) {
        mapListeners.remove(listener);
    }

    /**
     * Adds a Collection of {@link MapChangeListener} to this
     * {@link HashMapWithListeners}
     *
     * @param listeners listeners which will be registered
     */
    public void addAllListeners(Collection<? extends MapChangeListener> listeners) {
        mapListeners.addAll(listeners);
    }

    /**
     * Removes the Collection of {@link MapChangeListener} from this
     * {@link HashMapWithListeners}
     *
     * @param listeners listeners which will be removed
     */
    public void removeAllListeners(Collection<? extends MapChangeListener> listeners) {
        mapListeners.removeAll(listeners);
    }

}
