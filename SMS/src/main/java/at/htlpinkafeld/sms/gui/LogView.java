/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.gui.container.DaoDelegatingContainer;
import at.htlpinkafeld.sms.gui.overviewComponents.SearchComponent;
import at.htlpinkafeld.sms.pojo.Log;
import at.htlpinkafeld.sms.service.NoUserLoggedInException;
import at.htlpinkafeld.sms.service.PermissionService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author masix
 */
public class LogView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "logview";

    DaoDelegatingContainer<Log> logContainer;
    private final Grid logGrid;

    public LogView() {
        super.addComponent(((SMS_Main) UI.getCurrent()).getMenuBarComponent());

        logContainer = ContainerFactory.createLogContainer();

        logGrid = new Grid(logContainer);
        logGrid.removeColumn("id");
        logGrid.setSizeFull();
        logGrid.setHeight(UI.getCurrent().getPage().getBrowserWindowHeight() - 38 * 2 - 120, Unit.PIXELS);

        Map<String, String> filterMap = new HashMap<>();
        filterMap.put("Log Cause", "logCause");
        filterMap.put("Log Entry", "logEntry");
        filterMap.put("Timestamp", "timestamp");

        SearchComponent searchComponent = new SearchComponent(filterMap, logContainer);
        VerticalLayout innerLayout = new VerticalLayout();
        innerLayout.addComponent(searchComponent);
        innerLayout.setComponentAlignment(searchComponent, Alignment.MIDDLE_RIGHT);
        innerLayout.addComponent(logGrid);

        TabSheet mainTab = new TabSheet();
        mainTab.addTab(innerLayout, "Logs");
        super.addComponent(mainTab);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        try {
            if (PermissionService.isAdmin()) {

            } else {
                ((SMS_Main) UI.getCurrent()).navigateTo(OverviewView.VIEW_NAME);
            }
        } catch (NoUserLoggedInException ex) {
            //redirect not logged in Users to the Login-Page
            ((SMS_Main) UI.getCurrent()).navigateTo(LoginView.VIEW_NAME);
        }

        ((SMS_Main) UI.getCurrent()).getMenuBarComponent().switchStyle();
    }

}
