/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Martin Six
 */
public class MainView extends VerticalLayout implements View {
    
    public static String VIEW_NAME = "mainview";
    
    public MainView() {
        super.setSizeFull();
        
        MenuBar menuBar = new MenuBar();
        menuBar.setWidth(100, Unit.PERCENTAGE);
        menuBar.addItem("Overview", null);
        
        MenuBar.MenuItem logout = menuBar.addItem("Logout", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                UI ui = UI.getCurrent();
                if (ui instanceof SMS_Main) {
                    ((SMS_Main) ui).navigateTo(LoginView.VIEW_NAME);
                }
                System.out.println("change to mainView");
            }
        });
        
        logout.setStyleName("right");
        
        super.addComponent(menuBar);
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        
    }
    
}
