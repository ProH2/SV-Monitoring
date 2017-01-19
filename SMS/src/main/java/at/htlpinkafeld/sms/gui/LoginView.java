/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.service.JSONService;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * View for the Login Page
 *
 * @author Martin Six
 */
public class LoginView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "";

    public LoginView() {

        VerticalLayout innerLayout = new VerticalLayout();

        Label head = new Label("Login ", ContentMode.TEXT);
        head.setStyleName("heading");

        FormLayout formLayout = new FormLayout();

        final TextField usernameTextF = new TextField("Username");
        usernameTextF.addValidator(new NullValidator("Must be given!", false));
        final TextField passwordTextF = new TextField("Password");
        passwordTextF.addValidator(new NullValidator("Must be given!", false));

        formLayout.addComponent(usernameTextF);
        formLayout.addComponent(passwordTextF);

        Button loginButton = new Button("Login", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Login successfull", "Hello " + usernameTextF.getValue(), Notification.Type.TRAY_NOTIFICATION);
                ((SMS_Main) UI.getCurrent()).navigateTo(OverviewView.VIEW_NAME);
            }
        });

        formLayout.addComponent(loginButton);

        //Thread-Testing Button
        Button refreshButton = new Button("Refresh", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                JSONService.refresh();
            }
        });

        formLayout.addComponent(refreshButton);

        formLayout.setSizeUndefined();

        innerLayout.addComponent(head);
        innerLayout.addComponent(formLayout);

        innerLayout.setSizeUndefined();

        super.addComponent(innerLayout);
        super.setSizeFull();

        super.setComponentAlignment(innerLayout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
