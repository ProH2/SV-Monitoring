/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.service.NoUserLoggedInException;
import at.htlpinkafeld.sms.service.PermissionService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author masix
 */
public class LogView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "log view";

    private final Grid logGrid;

    public LogView() {
        logGrid = new Grid(ContainerFactory.createLogContainer());
        logGrid.removeColumn("id");
        logGrid.setSizeFull();
        logGrid.setHeight(UI.getCurrent().getPage().getBrowserWindowHeight()-38, Unit.PIXELS);
        super.addComponent(((SMS_Main) UI.getCurrent()).getMenuBarComponent());
        super.addComponent(logGrid);

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