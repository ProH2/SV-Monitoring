/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.window;

import at.htlpinkafeld.sms.gui.LoginView;
import at.htlpinkafeld.sms.gui.SMS_Main;
import at.htlpinkafeld.sms.gui.util.TimeManagementCalendarEvent;
import at.htlpinkafeld.sms.pojo.User;
import at.htlpinkafeld.sms.service.NoUserLoggedInException;
import at.htlpinkafeld.sms.service.PermissionService;
import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.Date;

/**
 * Window used for editing the Events of the calendar in
 * {@link TimeManagementView}. Receives the to be edited
 * {@link TimeManagementCalendarEvent}.
 *
 * @author Martin Six
 */
public class CalendarEventWindow extends Window {

    /**
     * Constructor for CalendarEventWindow
     *
     * @param calendarEvent calendarEvent which will be edited
     * @param userDataSource container which contains the available
     * {@link User Users}
     * @param calendar instance of the calender from which this window was
     * opened
     */
    public CalendarEventWindow(TimeManagementCalendarEvent calendarEvent, Container userDataSource, Calendar calendar) {
        super("Edit Time Distribution");
        super.center();
        super.setModal(true);

        FormLayout formLayout = new FormLayout();

        final NativeSelect userNativeSelect = new NativeSelect("User", userDataSource);
        userNativeSelect.setRequired(true);
        userNativeSelect.setRequiredError("User is required!");
        userNativeSelect.select(calendarEvent.getUser());
        userNativeSelect.setNullSelectionAllowed(false);

        final DateField startDateField = createDateTimeField("Start date", calendarEvent.getStart(), "Start date is required!");

        final DateField endDateField = createDateTimeField("End date", calendarEvent.getEnd(), "End date is required!");

        //validate for startDate>endDate
        startDateField.addValidator((Object value) -> {
            if (((Date) value).after(endDateField.getValue())) {
                throw new Validator.InvalidValueException("End date before Start date not allowed!");
            }
        });
        endDateField.addValidator((Object value) -> {
            if (((Date) value).before(startDateField.getValue())) {
                throw new Validator.InvalidValueException("End date before Start date not allowed!");
            }
        });

        formLayout.addComponent(userNativeSelect);
        formLayout.addComponent(startDateField);
        formLayout.addComponent(endDateField);

        //create the Buttons
        Button createButton = new Button("Save", (Button.ClickEvent event) -> {
            try {
                userNativeSelect.validate();
                startDateField.validate();
                endDateField.validate();

                calendar.removeEvent(calendarEvent);

                calendar.addEvent(new TimeManagementCalendarEvent((User) userNativeSelect.getValue(), startDateField.getValue(), endDateField.getValue()));

                calendar.markAsDirty();
                close();
            } catch (Validator.InvalidValueException e) {
                Notification.show("Validation Error!", e.getLocalizedMessage(), Notification.Type.WARNING_MESSAGE);
            }
        });

        Button cancelButton = new Button("Cancel", (Button.ClickEvent event) -> {
            close();
        });

        Button deleteButton = new Button("Delete", (Button.ClickEvent event) -> {
            calendar.removeEvent(calendarEvent);
            close();
        });

        try {
            if (PermissionService.isAdmin()) {
                formLayout.addComponents(createButton, cancelButton, deleteButton);
            } else {
                userNativeSelect.setEnabled(false);
                startDateField.setEnabled(false);
                endDateField.setEnabled(false);
            }
        } catch (NoUserLoggedInException ex) {
            super.close();
            ((SMS_Main) UI.getCurrent()).navigateTo(LoginView.VIEW_NAME);
        }

        formLayout.setSizeUndefined();
        formLayout.setMargin(true);
        super.setContent(formLayout);

    }

    /**
     * Convenience Factory-Method to create {@link DateField}
     */
    private DateField createDateTimeField(String caption, Date value, String errorMsg) {
        final PopupDateField dateField = new PopupDateField(caption, value);
        dateField.setRequired(true);
        dateField.setRequiredError(errorMsg);
        dateField.setResolution(Resolution.SECOND);
        dateField.setShowISOWeekNumbers(true);
        return dateField;
    }

}
