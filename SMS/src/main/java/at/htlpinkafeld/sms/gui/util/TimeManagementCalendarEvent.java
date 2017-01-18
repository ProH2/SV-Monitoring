/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.util;

import at.htlpinkafeld.sms.pojo.User;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Martin Six
 */
public class TimeManagementCalendarEvent extends BasicEvent {

    private User user;

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private static SimpleDateFormat dateFormatterOld = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat dateTimeFormatterOld = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    /**
     * A Constructor which uses LocalDate for convenience. Creates an Event for
     * the whole day without the allDay flag set
     *
     * @param user
     * @param date
     */
    public TimeManagementCalendarEvent(User user, LocalDate date) {
        super(user.getUsername(), date.format(dateFormatter), localToDate(date.atStartOfDay()), localToDate(date.atTime(23, 59, 59)));
        this.user=user;
    }

    /**
     * A Constructor which uses LocalDateTime for convenience. Creates an Event
     * with the specified data
     *
     * @param user
     * @param startDate
     * @param endDate
     */
    public TimeManagementCalendarEvent(User user, LocalDateTime startDate, LocalDateTime endDate) {
        super(user.getUsername(), startDate.format(dateTimeFormatter) + " - " + endDate.format(dateTimeFormatter), localToDate(startDate), localToDate(endDate));
        this.user=user;
    }

    /**
     * A Constructor which uses Date. For use when Date is already available.
     * Same function as
     * {@link #LocalDateTimeEvent(String caption, String description, LocalDate date) LocalDateTimeEvent}
     *
     * @param user
     * @param date
     */
    public TimeManagementCalendarEvent(User user, Date date) {
        super(user.getUsername(), dateFormatterOld.format(date), date);
        this.user=user;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date startTime = cal.getTime();
        super.setStart(startTime);

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endTime = cal.getTime();
        super.setEnd(endTime);
    }

    /**
     * A Constructor which uses Date. For use when Date is already available.
     * Same function as
     * {@link #LocalDateTimeEvent(String caption, String description, LocalDateTime startDate, LocalDateTime endDate) LocalDateTimeEvent}
     *
     * @param user
     * @param startDate
     * @param endDate
     */
    public TimeManagementCalendarEvent(User user, Date startDate, Date endDate) {
        super(user.getUsername(), dateTimeFormatterOld.format(startDate) + " - " + dateTimeFormatterOld.format(endDate), startDate, endDate);
        this.user=user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        super.setCaption(user.getUsername());
    }

    @Override
    protected void fireEventChange() {
        refreshDescription();

        super.fireEventChange();
    }

    private void refreshDescription() {
        LocalDateTime startTime = getLocalStart();
        LocalDateTime endTime = getLocalEnd();

        String newDescription;

        //Check if it is for the whole day/multiple days
        if (startTime.toLocalTime().equals(LocalTime.MIDNIGHT) && endTime.toLocalTime().equals(LocalTime.of(23, 59, 59))) {
            //check if it is one day
            if (startTime.toLocalDate().isEqual(endTime.toLocalDate())) {
                newDescription = startTime.format(dateFormatter);
            } else {
                newDescription = startTime.format(dateFormatter) + " - " + endTime.format(dateFormatter);
            }
        } else {
            newDescription = startTime.format(dateTimeFormatter) + " - " + endTime.format(dateTimeFormatter);
        }

        if (newDescription != null) {
            if (!newDescription.equals(super.getDescription())) {
                super.setDescription(newDescription);
            }
        }
    }

    private static LocalDateTime localFromDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.systemDefault());
    }

    private static Date localToDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneOffset.systemDefault()).toInstant());
    }

}
