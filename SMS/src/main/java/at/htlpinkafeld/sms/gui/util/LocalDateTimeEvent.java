/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.util;

import com.vaadin.addon.calendar.event.BasicEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 *
 * @author Martin Six
 */
public class LocalDateTimeEvent extends BasicEvent {

    /**
     * A Constructor which uses LocalDate for convenience. allDay ist set to
     * true automatically
     *
     * @param caption
     * @param description
     * @param date
     */
    public LocalDateTimeEvent(String caption, String description, LocalDate date) {
        super(caption, description, localToDate(date.atStartOfDay()));
        super.setAllDay(true);
    }

    /**
     * A Constructor which uses LocalDateTime for convenience.
     *
     * @param caption
     * @param description
     * @param startDate
     * @param endDate
     */
    public LocalDateTimeEvent(String caption, String description, LocalDateTime startDate, LocalDateTime endDate) {
        super(caption, description, localToDate(startDate), localToDate(endDate));
    }

    public void setStart(LocalDateTime start) {
        super.setStart(localToDate(start));
    }

    public void setEnd(LocalDateTime end) {
        super.setEnd(localToDate(end));
    }

    public LocalDateTime getLocalStart() {
        return localFromDate(super.getStart());
    }

    public LocalDateTime getLocalEnd() {
        return localFromDate(super.getEnd());
    }

    private static LocalDateTime localFromDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.systemDefault());
    }

    private static Date localToDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneOffset.systemDefault()).toInstant());
    }

}
