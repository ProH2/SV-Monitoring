/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.container;

import at.htlpinkafeld.dao.DutyDao;
import at.htlpinkafeld.sms.gui.TimeManagementView;
import at.htlpinkafeld.sms.gui.util.TimeManagementCalendarEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.components.calendar.event.CalendarEditableEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import java.util.Date;
import java.util.List;

/**
 * CalendarEditableEventProvider for the {@link Calendar} in
 * {@link TimeManagementView}.
 *
 * @author Martin Six
 */
public class DutyEventProvider implements CalendarEditableEventProvider {

    private final DutyDao dutyDao;
    private final BeanItemContainer<CalendarEvent> eventContainer;

    /**
     * Constructor for the Eventprovider
     *
     * @param dutyDao DAO to which the calls are delegated to
     */
    public DutyEventProvider(DutyDao dutyDao) {
        this.dutyDao = dutyDao;

        this.eventContainer = new BeanItemContainer<>(CalendarEvent.class);
    }

    @Override
    public void addEvent(CalendarEvent event) {
        if (event instanceof TimeManagementCalendarEvent) {
            this.eventContainer.addBean((TimeManagementCalendarEvent) event);
        }
    }

    @Override
    public void removeEvent(CalendarEvent event) {
        this.eventContainer.removeItem(event);
    }

    @Override
    public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
        return this.eventContainer.getItemIds();
    }

}
