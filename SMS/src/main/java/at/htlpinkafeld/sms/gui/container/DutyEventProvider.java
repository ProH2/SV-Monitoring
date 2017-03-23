/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.container;

import at.htlpinkafeld.sms.dao.DutyDao;
import at.htlpinkafeld.sms.dao.DutyDaoImpl;
import at.htlpinkafeld.sms.gui.TimeManagementView;
import at.htlpinkafeld.sms.gui.util.TimeManagementCalendarEvent;
import at.htlpinkafeld.sms.pojo.Duty;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.components.calendar.event.CalendarEditableEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CalendarEditableEventProvider for the {@link Calendar} in
 * {@link TimeManagementView}.
 *
 * @author Martin Six
 */
public class DutyEventProvider implements CalendarEditableEventProvider {

    private final DutyDaoImpl dutyDao;

    /**
     * Constructor for the Eventprovider
     *
     * @param dutyDao DAO to which the calls are delegated to
     */
    public DutyEventProvider(DutyDaoImpl dutyDao) {
        this.dutyDao = dutyDao;

    }

    @Override
    public void addEvent(CalendarEvent event) {
        if (event instanceof TimeManagementCalendarEvent) {
            dutyDao.insert(new Duty(null, ((TimeManagementCalendarEvent) event).getUser(),
                    new java.sql.Date(event.getStart().getTime()), new java.sql.Date(event.getEnd().getTime())));
        }
    }

    @Override
    public void removeEvent(CalendarEvent event) {
        if (event instanceof TimeManagementCalendarEvent) {
            dutyDao.delete(((TimeManagementCalendarEvent) event).getDutyId());
        }

    }

    public void updateEvent(TimeManagementCalendarEvent event) {
        dutyDao.update(new Duty(event.getDutyId(), event.getUser(), event.getStart(), event.getEnd()));
    }

    @Override
    public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
        return dutyDao.getDutiesByRange(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime())).stream().map((duty) -> {
            return new TimeManagementCalendarEvent(duty);
        }).collect(Collectors.toList());
    }

}
