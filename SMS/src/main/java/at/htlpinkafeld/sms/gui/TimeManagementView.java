/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.gui.window.CalendarEventWindow;
import static at.htlpinkafeld.sms.gui.UserManagementView.NAME_PROPERTY;
import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.gui.container.DutyEventProvider;
import at.htlpinkafeld.sms.gui.util.TimeManagementCalendarEvent;
import at.htlpinkafeld.sms.pojo.User;
import at.htlpinkafeld.sms.service.NoUserLoggedInException;
import at.htlpinkafeld.sms.service.PermissionService;
import com.vaadin.addon.calendar.gwt.client.ui.VCalendar;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Calendar;
import com.vaadin.data.Container;
import com.vaadin.event.Action;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableTransferable;
import com.vaadin.ui.UI;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.CalendarDateRange;
import com.vaadin.ui.components.calendar.CalendarTargetDetails;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import java.io.File;
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

    private final Table table;

    private final Container.Indexed userContainer;

    private final DutyEventProvider dutyProvider;

    private final Calendar calendar;

    /**
     * Constructor for TimeManagementView
     */
    public TimeManagementView() {
        super.addComponent(((SMS_Main) UI.getCurrent()).getMenuBarComponent());

        boolean isAdmin = false;
        try {
            isAdmin = PermissionService.isAdmin();
        } catch (NoUserLoggedInException ex) {
            //redirect not logged in Users to the Login-Page
            ((SMS_Main) UI.getCurrent()).navigateTo(LoginView.VIEW_NAME);
        }

        //calc calendarHeight based on Browserwindow minus 38 because of menu
        int layoutSize = UI.getCurrent().getPage().getBrowserWindowHeight() - (38);

        userContainer = ContainerFactory.createIndexedUserContainer();
        if (isAdmin) {
            table = new Table("Available Users");
            table.setWidth(100, Unit.PERCENTAGE);
            table.setHeight(layoutSize, Unit.PIXELS);

            table.setContainerDataSource(userContainer, Collections.singletonList(NAME_PROPERTY));
            table.setColumnHeader(NAME_PROPERTY, "Available Users");
            table.setDragMode(Table.TableDragMode.ROW);
            table.setSelectable(true);
        } else {
            table = null;
        }

        dutyProvider = ContainerFactory.createDutyEventProvider();

        calendar = new Calendar(dutyProvider);
        calendar.setWidth((float) 100, Unit.PERCENTAGE);
//        
//        calendar.setContainerDataSource(userContainer, NAME_PROPERTY, NAME_PROPERTY, NAME_PROPERTY, NAME_PROPERTY, NAME_PROPERTY);
        //Reduce the height further because of visual glitches
        calendar.setHeight(layoutSize - 190, Unit.PIXELS);

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
                    calendar.set(java.util.Calendar.DAY_OF_WEEK, 1);
                    cal.setStartDate(calendar.getTime());
                    calendar.add(java.util.Calendar.DAY_OF_YEAR, 7 * 5);
                    cal.setEndDate(calendar.getTime());

                } else {
                    // Default behaviour, change date range to the week
                    super.dateClick(event);
                }
            }
        });

        calendar.setHandler((CalendarComponentEvents.EventClick event) -> {
            UI.getCurrent().addWindow(new CalendarEventWindow((TimeManagementCalendarEvent) event.getCalendarEvent(), userContainer, calendar));
        });

        if (isAdmin) {

            calendar.addActionHandler(createCalendarActionHandler());

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

            calendar.setHandler((CalendarComponentEvents.RangeSelectEvent event) -> {
                if (!event.getStart().equals(event.getEnd())) {
                    TimeManagementCalendarEvent tmce = new TimeManagementCalendarEvent(new User(), event.getStart(), event.getEnd());
                    UI.getCurrent().addWindow(new CalendarEventWindow(tmce, userContainer, event.getComponent()));
                }
            });

            calendar.setHandler(new BasicEventMoveHandler() {
                @Override
                public void eventMove(CalendarComponentEvents.MoveEvent event) {
                    if (event.getCalendarEvent() instanceof TimeManagementCalendarEvent) {
                        super.eventMove(event);
                        TimeManagementCalendarEvent tmce = (TimeManagementCalendarEvent) event.getCalendarEvent();
                        dutyProvider.updateEvent(tmce);
                    }
                    calendar.markAsDirty();
                }
            });
            calendar.setHandler(new BasicEventResizeHandler() {
                @Override
                public void eventResize(CalendarComponentEvents.EventResize event) {
                    if (event.getCalendarEvent() instanceof TimeManagementCalendarEvent) {
                        super.eventResize(event);
                        TimeManagementCalendarEvent tmce = (TimeManagementCalendarEvent) event.getCalendarEvent();
                        dutyProvider.updateEvent(tmce);
                    }
                    calendar.markAsDirty();
                }
            });
        } else {
            //Disable Interactive calendar for normal Users
            calendar.setHandler((CalendarComponentEvents.EventResizeHandler) null);
            calendar.setHandler((CalendarComponentEvents.EventMoveHandler) null);
        }

//        DragAndDropWrapper dropWrapper = new DragAndDropWrapper(calendar);
//        dropWrapper.setDropHandler(dh);
        // Find the application directory
        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

        // Image as a file resource
        FileResource arrowLeftResource = new FileResource(new File(basepath + "/WEB-INF/images/chevron-left.svg"));

        Button calendarLeftButton = new Button(arrowLeftResource);
        calendarLeftButton.setIconAlternateText("Calendar Backward");
        calendarLeftButton.addClickListener((event) -> {

            java.util.Calendar timeCalendar = calendar.getInternalCalendar();

            //Checks if the calendar is in MonthlyMode
            if (calendar.isMonthlyMode()) {

                timeCalendar.setTime(calendar.getStartDate());
                timeCalendar.add(java.util.Calendar.DAY_OF_YEAR, -28);
                Date startTime = timeCalendar.getTime();

                timeCalendar.setTime(calendar.getEndDate());
                timeCalendar.add(java.util.Calendar.DAY_OF_YEAR, -28);
                Date endTime = timeCalendar.getTime();

                //Moves the calendar 28 Days back
                calendar.setStartDate(startTime);
                calendar.setEndDate(endTime);
            } else {
                timeCalendar.setTime(calendar.getStartDate());
                timeCalendar.add(java.util.Calendar.DAY_OF_YEAR, -7);
                Date startTime = timeCalendar.getTime();

                timeCalendar.setTime(calendar.getEndDate());
                timeCalendar.add(java.util.Calendar.DAY_OF_YEAR, -7);
                Date endTime = timeCalendar.getTime();

                //Moves the calendar 7 Days back
                calendar.setStartDate(startTime);
                calendar.setEndDate(endTime);
            }
        });

        FileResource arrowRigthResource = new FileResource(new File(basepath + "/WEB-INF/images/chevron-right.svg"));

        Button calendarRightButton = new Button(arrowRigthResource);
        calendarRightButton.setIconAlternateText("Calendar Forward");
        calendarRightButton.addClickListener((event) -> {
            java.util.Calendar timeCalendar = calendar.getInternalCalendar();

            //Checks if the calendar is in MonthlyMode
            if (calendar.isMonthlyMode()) {

                timeCalendar.setTime(calendar.getStartDate());
                timeCalendar.add(java.util.Calendar.DAY_OF_YEAR, 28);
                Date startTime = timeCalendar.getTime();

                timeCalendar.setTime(calendar.getEndDate());
                timeCalendar.add(java.util.Calendar.DAY_OF_YEAR, 28);
                Date endTime = timeCalendar.getTime();

                //Moves the calendar 28 Days forward
                calendar.setStartDate(startTime);
                calendar.setEndDate(endTime);
            } else {
                timeCalendar.setTime(calendar.getStartDate());
                timeCalendar.add(java.util.Calendar.DAY_OF_YEAR, 7);
                Date startTime = timeCalendar.getTime();

                timeCalendar.setTime(calendar.getEndDate());
                timeCalendar.add(java.util.Calendar.DAY_OF_YEAR, 7);
                Date endTime = timeCalendar.getTime();

                //Moves the calendar 7 Days forward
                calendar.setStartDate(startTime);
                calendar.setEndDate(endTime);
            }
        });

        FileResource exchangeViewsResource = new FileResource(new File(basepath + "/WEB-INF/images/exchange.svg"));

        Button calendarToggleViewButton = new Button("Toggle Calendar View", exchangeViewsResource);
        calendarToggleViewButton.addClickListener((event) -> {
            long currentCalDateRange = calendar.getEndDate().getTime() - calendar.getStartDate().getTime();
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(calendar.getStartDate());

            if (currentCalDateRange <= VCalendar.WEEKINMILLIS) {
                // Change the date range to the current month
                gregorianCalendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
                calendar.setStartDate(gregorianCalendar.getTime());
                gregorianCalendar.set(java.util.Calendar.DAY_OF_MONTH, gregorianCalendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));

                calendar.setEndDate(gregorianCalendar.getTime());

            } else {
                //change date range to the week
                gregorianCalendar.set(java.util.Calendar.DAY_OF_WEEK, 2);
                calendar.setStartDate(gregorianCalendar.getTime());
                gregorianCalendar.add(java.util.Calendar.DAY_OF_MONTH, 7);
                gregorianCalendar.add(java.util.Calendar.SECOND, -1);

                calendar.setEndDate(gregorianCalendar.getTime());
            }

        });
        calendarToggleViewButton.setSizeFull();

        calendarLeftButton.setSizeFull();
        calendarRightButton.setSizeFull();

        calendar.setHandler((CalendarComponentEvents.BackwardHandler) null);
        calendar.setHandler((CalendarComponentEvents.ForwardHandler) null);

        HorizontalLayout calendarLeftRightLayout = new HorizontalLayout(calendarLeftButton, calendar, calendarRightButton);

        calendarLeftRightLayout.setExpandRatio(calendarLeftButton, 3);
        calendarLeftRightLayout.setExpandRatio(calendar, 94);
        calendarLeftRightLayout.setExpandRatio(calendarRightButton, 3);

//        calendarLeftRightLayout.setWidth((float) 100, Unit.PERCENTAGE);
        calendarLeftRightLayout.setSizeFull();

        VerticalLayout calendarLayout = new VerticalLayout(calendarToggleViewButton, calendarLeftRightLayout);
        calendarLayout.setExpandRatio(calendarToggleViewButton, 5);
        calendarLayout.setExpandRatio(calendarLeftRightLayout, 95);
        calendarLayout.setSizeFull();

        if (isAdmin && table != null) {
            HorizontalSplitPanel parentSplitPanel = new HorizontalSplitPanel(calendarLayout, table);
//            parentSplitPanel.setLocked(true);
            parentSplitPanel.setSplitPosition(90, Unit.PERCENTAGE);
            parentSplitPanel.setMaxSplitPosition(95, Unit.PERCENTAGE);
            parentSplitPanel.setMinSplitPosition(60, Unit.PERCENTAGE);

            parentSplitPanel.setWidth(100, Unit.PERCENTAGE);
            parentSplitPanel.setHeight(layoutSize, Unit.PIXELS);

            super.addComponent(parentSplitPanel);
        } else {
            calendarLayout.setHeight(layoutSize, Unit.PIXELS);
            super.addComponent(calendarLayout);
        }
    }

    @Override

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        try {
            PermissionService.isAdmin();
        } catch (NoUserLoggedInException ex) {
            //redirect not logged in Users to the Login-Page
            ((SMS_Main) UI.getCurrent()).navigateTo(LoginView.VIEW_NAME);
        }

        ((SMS_Main) UI.getCurrent()).getMenuBarComponent().switchStyle();
    }

    private Action.Handler createCalendarActionHandler() {
        Action.Handler actionHandler = new Action.Handler() {

            Action createEventAction = new Action("Create Event");
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
                        return new Action[]{createEventAction, editEventAction, deleteEventAction};
                    }
                }
                return new Action[]{createEventAction};

            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                // The sender is the Calendar object
                Calendar calendar = (Calendar) sender;

                if (action == createEventAction) {
                    TimeManagementCalendarEvent tmce = null;
                    if (target instanceof TimeManagementCalendarEvent) {
                        TimeManagementCalendarEvent tarTmce = (TimeManagementCalendarEvent) target;
                        tmce = new TimeManagementCalendarEvent(new User(), tarTmce.getLocalStart(), tarTmce.getLocalStart().plusHours(2));
                    } else if (target instanceof Date) {
                        java.util.Calendar timeCalendar = calendar.getInternalCalendar();
                        timeCalendar.setTime((Date) target);
                        timeCalendar.add(java.util.Calendar.MINUTE, 120);
                        Date endTime = timeCalendar.getTime();

                        tmce = new TimeManagementCalendarEvent(new User(), (Date) target, endTime);
                    } else {
                        Notification.show("No event to edit");
                    }
                    UI.getCurrent().addWindow(new CalendarEventWindow(tmce, userContainer, calendar));
                } else if (action == editEventAction) {
                    // Check that the click was done on an event
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
