/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import com.vaadin.event.ContextClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;

/**
 * A CustomComponent for the Dashboard-type-Menubar on the top of the screen
 * after login.
 *
 * @author Martin Six
 */
public class MenuBarComponent extends CustomComponent {

    private final MenuBar menuBar;

    private final MenuBar.MenuItem overviewMItem;
    private final MenuBar.MenuItem userManagementMItem;
    private final MenuBar.MenuItem timeManagementMItem;

    public MenuBarComponent() {
        super.setSizeFull();

        menuBar = new MenuBar();
        menuBar.setWidth(100, Unit.PERCENTAGE);

        overviewMItem = menuBar.addItem("Overview", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                UI ui = UI.getCurrent();
                if (ui instanceof SMS_Main) {
                    ((SMS_Main) ui).navigateTo(OverviewView.VIEW_NAME);
                }
            }
        });

        userManagementMItem = menuBar.addItem("User Management", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                UI ui = UI.getCurrent();
                if (ui instanceof SMS_Main) {
                    ((SMS_Main) ui).navigateTo(UserManagementView.VIEW_NAME);
                }
            }
        });

        timeManagementMItem = menuBar.addItem("Time Management", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                UI ui = UI.getCurrent();
                if (ui instanceof SMS_Main) {
                    ((SMS_Main) ui).navigateTo(TimeManagementView.VIEW_NAME);
                }
            }
        });

        MenuBar.MenuItem logoutMItem = menuBar.addItem("Logout", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                UI ui = UI.getCurrent();
                if (ui instanceof SMS_Main) {
                    ((SMS_Main) ui).navigateTo(LoginView.VIEW_NAME);
                }
            }
        });

        String uriFragment = UI.getCurrent().getPage().getUriFragment();
        if (uriFragment != null) {
            switch (uriFragment) {
                case '!' + OverviewView.VIEW_NAME:
                    overviewMItem.setStyleName("selected");
                    break;
                case '!' + UserManagementView.VIEW_NAME:
                    userManagementMItem.setStyleName("selected");
                    break;
                case '!' + TimeManagementView.VIEW_NAME:
                    timeManagementMItem.setStyleName("selected");
                default:
                    String s = UI.getCurrent().getPage().getUriFragment();
                    System.out.println(s);
            }
        }

        logoutMItem.setStyleName("right");

        super.setCompositionRoot(menuBar);
    }

}
