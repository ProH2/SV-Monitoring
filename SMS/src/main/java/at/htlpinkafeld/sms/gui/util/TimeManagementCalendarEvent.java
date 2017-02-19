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
 * {@link BasicEvent}-subclass which additionally wraps an {@link User} and has
 * multiple convenient features, mainly {@link java.time}-compatibility related
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
     * @param user User to whom this event is mapped to
     * @param date The date for the event
     */
    public TimeManagementCalendarEvent(User user, LocalDate date) {
        super(user.getUsername(), date.format(dateFormatter), localToDate(date.atStartOfDay()), localToDate(date.atTime(23, 59, 59)));
        this.user = user;
    }

    /**
     * A Constructor which uses LocalDateTime for convenience. Creates an Event
     * with the specified data
     *
     * @param user User to whom this event is mapped to
     * @param startDateTime The startDateTime for the event
     * @param endDateTime The endDateTime for the event
     */
    public TimeManagementCalendarEvent(User user, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(user.getUsername(), startDateTime.format(dateTimeFormatter) + " - " + endDateTime.format(dateTimeFormatter), localToDate(startDateTime), localToDate(endDateTime));
        this.user = user;
    }

    /**
     * A Constructor which uses Date. For use when Date is already available.
     *
     * @param user User to whom this event is mapped to
     * @param date The date for the event
     */
    public TimeManagementCalendarEvent(User user, Date date) {
        super(user.getUsername(), dateFormatterOld.format(date), date);
        this.user = user;

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
     *
     * @param user User to whom this event is mapped to
     * @param startDate The startDateTime for the event
     * @param endDate The endDateTime for the event
     */
    public TimeManagementCalendarEvent(User user, Date startDate, Date endDate) {
        super(user.getUsername(), dateTimeFormatterOld.format(startDate) + " - " + dateTimeFormatterOld.format(endDate), startDate, endDate);
        this.user = user;
    }

    /**
     * Setter for the StartDate via a {@link LocalDateTime}
     *
     * @param start new startDateTime for the event
     */
    public void setStart(LocalDateTime start) {
        super.setStart(localToDate(start));
    }

    /**
     * Setter for the EndDate via a {@link LocalDateTime}
     *
     * @param end new endDateTime for the event
     */
    public void setEnd(LocalDateTime end) {
        super.setEnd(localToDate(end));
    }

    /**
     * Getter for the StartDate as a {@link LocalDateTime}
     *
     * @return startDateTime as {@link LocalDateTime}
     */
    public LocalDateTime getLocalStart() {
        return localFromDate(super.getStart());
    }

    /**
     * Getter for the EndDate as a {@link LocalDateTime}
     *
     * @return endDateTime as {@link LocalDateTime}
     */
    public LocalDateTime getLocalEnd() {
        return localFromDate(super.getEnd());
    }

    /**
     * Getter for the User
     *
     * @return the User mapped to this event
     */
    public User getUser() {
        return user;
    }

    /**
     * Setter for the User
     *
     * @param user new User
     */
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

    //converter Methods between Date and LocalDateTime
    private static LocalDateTime localFromDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.systemDefault());
    }

    private static Date localToDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneOffset.systemDefault()).toInstant());
    }

}
