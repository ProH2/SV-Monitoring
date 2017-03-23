/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.container;

import at.htlpinkafeld.sms.dao.BaseDao;
import at.htlpinkafeld.sms.pojo.IPojo;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import java.util.Collection;

/**
 * A Container which also autmoatically delegates the calls of addBean, addAll
 * and removeItem to the predefined {@link BaseDao}
 *
 * @author masix
 * @param <T> The Pojo-Type which is used by the Dao and is stored in the
 * BeanItemContainer
 */
public class DaoDelegatingContainer<T extends IPojo> extends BeanItemContainer<T> {

    private BaseDao<T> baseDao;

    /**
     * Constructor which creates a BeanItemContainer based on {@link BaseDao#findAll()
     * }
     *
     * @param type the Beantype.class for the BeanItemContainer
     * @param baseDao the DAO to which the calls are delegated to
     */
    public DaoDelegatingContainer(Class<? super T> type, BaseDao<T> baseDao) {
        super(type, baseDao.findAll());
        this.baseDao = baseDao;
    }

    /**
     * Constructor which creates a BeanItemContainer based on Collections which
     * is passed
     *
     * @param type the Beantype.class for the BeanItemContainer
     * @param baseDao the DAO to which the calls are delegated to
     * @param beans the collection used to initialize the BeanItemContainer
     */
    public DaoDelegatingContainer(Class<? super T> type, BaseDao<T> baseDao, Collection<T> beans) {
        super(type, beans);
        this.baseDao = baseDao;
    }

    @Override
    public BeanItem<T> addItemAt(int index, Object newItemId) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeanItem<T> addItemAfter(Object previousItemId, Object newItemId) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeanItem<T> addBean(T bean) {
        baseDao.insert(bean);

        return super.addBean(bean);
    }

    @Override
    public void addAll(Collection<? extends T> collection) {
        if (baseDao != null) {
            collection.stream().forEach((t) -> {
                baseDao.insert(t);
            });
        }
        super.addAll(collection);
    }

    @Override
    public boolean removeItem(Object itemId) {
        if (itemId instanceof IPojo) {
            baseDao.delete(((IPojo) itemId).getId());
            return super.removeItem(itemId);
        }
        return false;
    }

    public void updateItem(T item) {
        baseDao.update(item);
        fireItemSetChange();
    }
}
