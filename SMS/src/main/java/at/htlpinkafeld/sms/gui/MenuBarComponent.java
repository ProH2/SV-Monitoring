/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.service.NoUserLoggedInException;
import at.htlpinkafeld.sms.service.PermissionService;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import java.io.File;
import java.security.Permission;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A CustomComponent for the Dashboard-type-Menubar on the top of the screen
 * after login.
 *
 * @author Martin Six
 */
public final class MenuBarComponent extends CustomComponent {

    private final MenuBar menuBar;

    private final MenuBar.MenuItem overviewMItem;
    private final MenuBar.MenuItem host_service_ManagementMItem;
    private final MenuBar.MenuItem userManagementMItem;
    private final MenuBar.MenuItem timeManagementMItem;

    /**
     * Constructor for MenuBarComponent
     */
    public MenuBarComponent() {
        super.setHeight(37, Unit.PIXELS);

        menuBar = new MenuBar();
        menuBar.setWidth(100, Unit.PERCENTAGE);

        overviewMItem = menuBar.addItem("Overview", (MenuBar.MenuItem selectedItem) -> {
            UI ui = UI.getCurrent();
            if (ui instanceof SMS_Main) {
                ((SMS_Main) ui).navigateTo(OverviewView.VIEW_NAME);
            }
        });

        host_service_ManagementMItem = menuBar.addItem("Host-Service Management", (MenuBar.MenuItem selectedItem) -> {
            UI ui = UI.getCurrent();
            if (ui instanceof SMS_Main) {
                ((SMS_Main) ui).navigateTo(Host_Service_ManagementView.VIEW_NAME);
            }
        });

        userManagementMItem = menuBar.addItem("User Management", (MenuBar.MenuItem selectedItem) -> {
            UI ui = UI.getCurrent();
            if (ui instanceof SMS_Main) {
                ((SMS_Main) ui).navigateTo(UserManagementView.VIEW_NAME);
            }
        });

        timeManagementMItem = menuBar.addItem("Time Management", (MenuBar.MenuItem selectedItem) -> {
            UI ui = UI.getCurrent();
            if (ui instanceof SMS_Main) {
                ((SMS_Main) ui).navigateTo(TimeManagementView.VIEW_NAME);
            }
        });

        MenuBar.MenuItem logoutMItem = menuBar.addItem("Logout", (MenuBar.MenuItem selectedItem) -> {
            for (UI ui : VaadinSession.getCurrent().getUIs()) {
                ui.access(() -> {
                    // Redirect from the page
                    ui.getPage().setLocation("/SMS/");
                });
            }

            getSession().close();
        });

        logoutMItem.setIcon(new FileResource(new File(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/WEB-INF/images/sign-out.svg")));
        logoutMItem.setStyleName("right");

        switchStyle();

        super.setCompositionRoot(menuBar);
    }

    /**
     * Sets the style of the {@link MenuBar.MenuItem} for the current page as
     * current according to the url
     */
    public void switchStyle() {

        try {
            if (!PermissionService.isAdmin()) {
                menuBar.removeItem(host_service_ManagementMItem);
                menuBar.removeItem(userManagementMItem);
            }
        } catch (NoUserLoggedInException ex) {

        }

        overviewMItem.setStyleName(null);
        host_service_ManagementMItem.setStyleName(null);
        userManagementMItem.setStyleName(null);
        timeManagementMItem.setStyleName(null);

        String uriFragment = UI.getCurrent().getPage().getUriFragment();
        if (uriFragment != null) {
            switch (uriFragment) {
                case '!' + OverviewView.VIEW_NAME:
                    overviewMItem.setStyleName("current");
                    break;
                case '!' + Host_Service_ManagementView.VIEW_NAME:
                    host_service_ManagementMItem.setStyleName("current");
                    break;
                case '!' + UserManagementView.VIEW_NAME:
                    userManagementMItem.setStyleName("current");
                    break;
                case '!' + TimeManagementView.VIEW_NAME:
                    timeManagementMItem.setStyleName("current");
                    break;
                default:
                    overviewMItem.setStyleName("current");
            }
        }
    }

}
