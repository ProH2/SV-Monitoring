/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import static at.htlpinkafeld.sms.gui.UserManagementView.USERNAME_PROPERTY;
import at.htlpinkafeld.sms.gui.util.TimeManagementCalendarEvent;
import at.htlpinkafeld.sms.pojo.User;
import com.vaadin.addon.calendar.gwt.client.ui.VCalendar;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Calendar;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableTransferable;
import com.vaadin.ui.UI;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.CalendarDateRange;
import com.vaadin.ui.components.calendar.CalendarTargetDetails;
import com.vaadin.ui.components.calendar.event.BasicEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * View for the TimeManagement Page
 *
 * @author Martin Six
 */
public class TimeManagementView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "timemanagementview";

    Table table;

    public TimeManagementView() {
        super.addComponent(new MenuBarComponent());

//        super.addComponent(new Label("Dienstkalendar"));

        Container.Indexed indexed = createIndexedContainer();
        table = new Table("Available Users");
        table.setWidth(100, Unit.PERCENTAGE);
        table.setHeight(800, Unit.PIXELS);

        table.setContainerDataSource(indexed, Collections.singletonList(USERNAME_PROPERTY));
        table.setDragMode(Table.TableDragMode.ROW);
        table.setSelectable(true);

        Calendar calendar = new Calendar(new BasicEventProvider());
        calendar.setWidth((float) 99.5, Unit.PERCENTAGE);
        calendar.setHeight(800, Unit.PIXELS);

        calendar.addActionHandler(createCalendarActionHandler());

        calendar.setHandler(new BasicDateClickHandler() {
            @Override
            public void dateClick(CalendarComponentEvents.DateClickEvent event) {
                Calendar cal = event.getComponent();
                long currentCalDateRange = cal.getEndDate().getTime() - cal.getStartDate().getTime();

                if (currentCalDateRange >= VCalendar.DAYINMILLIS) {
                    // Change the date range to the current month
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTime(event.getDate());
                    calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
                    cal.setStartDate(calendar.getTime());
                    calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
                    cal.setEndDate(calendar.getTime());

                } else {
                    // Default behaviour, change date range to the week
                    super.dateClick(event);
                }
            }
        });

        calendar.setHandler(new CalendarComponentEvents.EventClickHandler() {
            @Override
            public void eventClick(CalendarComponentEvents.EventClick event) {
                UI.getCurrent().addWindow(new CalendarEventWindow((TimeManagementCalendarEvent) event.getCalendarEvent(), table.getContainerDataSource(), calendar));
            }
        });

        //DropHandler for Dropping Users from List to Calendar to create Events
        calendar.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent event) {
                CalendarTargetDetails details = (CalendarTargetDetails) event.getTargetDetails();

                TableTransferable transferable = (TableTransferable) event.getTransferable();

                Calendar cal = details.getTargetCalendar();
                long currentCalDateRange = cal.getEndDate().getTime() - cal.getStartDate().getTime();

                Date dropTime = details.getDropTime();

                User draggedUser = (User) transferable.getItemId();

                //Checks if the calendar is in WeekView( or DayView)
                if (currentCalDateRange < VCalendar.WEEKINMILLIS) {

                    java.util.Calendar timeCalendar = details.getTargetCalendar().getInternalCalendar();
                    timeCalendar.setTime(dropTime);
                    timeCalendar.add(java.util.Calendar.MINUTE, 120);
                    Date endTime = timeCalendar.getTime();

                    calendar.addEvent(new TimeManagementCalendarEvent(draggedUser, dropTime, endTime));
                } else {
                    calendar.addEvent(new TimeManagementCalendarEvent(draggedUser, dropTime));
                }
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
//                return new SourceIs(table);
                return AcceptAll.get();
            }
        });

//        DragAndDropWrapper dropWrapper = new DragAndDropWrapper(calendar);
//        dropWrapper.setDropHandler(dh);
        VerticalLayout calendarLayout = new VerticalLayout(calendar);
        calendarLayout.setWidth(100, Unit.PERCENTAGE);

        HorizontalSplitPanel parentSplitPanel = new HorizontalSplitPanel(calendarLayout, table);
        parentSplitPanel.setLocked(true);
        parentSplitPanel.setSplitPosition(90, Unit.PERCENTAGE);
        parentSplitPanel.setSizeFull();

        super.addComponent(parentSplitPanel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    /**
     * Convenience Method to create a {@link Container} for testing purpose.
     *
     * @return
     */
    private BeanItemContainer createIndexedContainer() {
        BeanItemContainer<User> container = new BeanItemContainer<>(User.class);

        container.addBean(new User(1, "Dogist", "1234", "Martin", "noplan@gmc.at", "5421575"));
        container.addBean(new User(2, "Irish", "4321", "Sebastian", "noplan@qmail.com", "5788775"));

        return container;
    }

    private Action.Handler createCalendarActionHandler() {
        Action.Handler actionHandler = new Action.Handler() {

            Action editEventAction = new Action("Edit Event");
            Action deleteEventAction = new Action("Delete Event");

            @Override
            public Action[] getActions(Object target, Object sender) {

                // The sender is the Calendar object
                if (!(sender instanceof Calendar)) {
                    return null;
                }

                // The target should be a CalendarDateRage for the
                // entire day from midnight to midnight.
                if (!(target instanceof CalendarDateRange)) {
                    return null;
                }

                CalendarDateRange dateRange = (CalendarDateRange) target;

                Calendar calendar = (Calendar) sender;

                // List all the events on the requested day
                List<CalendarEvent> events = calendar.getEvents(dateRange.getStart(), dateRange.getEnd());

                // You can have some logic here, using the date
                // information.
                for (CalendarEvent e : events) {
                    if (!e.isAllDay()) {
                        return new Action[]{editEventAction, deleteEventAction};
                    }
                }
                return new Action[]{};

            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                // The sender is the Calendar object
                Calendar calendar = (Calendar) sender;

                if (action == editEventAction) {
                    // Check that the click was not done on an event
                    if (target instanceof TimeManagementCalendarEvent) {
                        UI.getCurrent().addWindow(new CalendarEventWindow((TimeManagementCalendarEvent) target, table.getContainerDataSource(), calendar));
                    } else {
                        Notification.show("No event to edit");
                    }
                } else if (action == deleteEventAction) {
                    // Check if the action was clicked on top of an event
                    if (target instanceof TimeManagementCalendarEvent) {
                        TimeManagementCalendarEvent event = (TimeManagementCalendarEvent) target;
                        calendar.removeEvent(event);
                    } else {
                        Notification.show("No event to delete");
                    }
                }
            }
        };
        return actionHandler;
    }

}
