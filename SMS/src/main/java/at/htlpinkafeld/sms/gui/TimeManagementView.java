/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.gui.util.LocalDateTimeEvent;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents;
import com.vaadin.addon.calendar.ui.handler.BasicWeekClickHandler;
import java.time.LocalDate;
import java.time.Month;

/**
 * View for the TimeManagement Page
 *
 * @author Martin Six
 */
public class TimeManagementView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "timemanagementview";

    public TimeManagementView() {
        super.addComponent(new MenuBarComponent());

        Calendar calendar = new Calendar(new BasicEventProvider());

        calendar.addEvent(new LocalDateTimeEvent("Test Caption", "Test Desc", LocalDate.of(2016, Month.DECEMBER, 17)));
        calendar.addEvent(new LocalDateTimeEvent("Test Caption", "Test Desc", LocalDate.of(2016, Month.DECEMBER, 18).atStartOfDay(), LocalDate.of(2016, Month.DECEMBER, 18).atTime(8, 0)));

        calendar.setWeeklyCaptionFormat("dddd");

        calendar.setHandler(new BasicWeekClickHandler() {
            @Override
            public void weekClick(CalendarComponentEvents.WeekClick event) {
                super.weekClick(event);
            }
        });

        super.addComponent(calendar);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
