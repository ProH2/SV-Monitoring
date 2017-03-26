/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.window;

import at.htlpinkafeld.sms.gui.UserManagementView;
import at.htlpinkafeld.sms.pojo.User;
import at.htlpinkafeld.sms.service.PermissionService;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import java.util.Collection;

/**
 * Window used for creating a new User from {@link UserManagementView}. Receives
 * a {@link Indexed} as ContainerDataSource to create the User
 *
 * @author Martin Six
 */
public class NewUserWindow extends Window {

    /**
     * Constructor for a View which creates a new User and adds it to the given
     * {@link Container.Indexed}.
     *
     * @param containerDataSource Container where the new User will be added
     */
    public NewUserWindow(BeanItemContainer containerDataSource) {
        super("Create New User");
        super.center();
        super.setModal(true);

        FormLayout formLayout = new FormLayout();

        final TextField usernameTextF = new TextField("Username");
        usernameTextF.setRequired(true);
        usernameTextF.setRequiredError("Username is required!");
        usernameTextF.addValidator(new StringLengthValidator("The Username is too short!", 3, null, false));
        usernameTextF.addValidator(new Validator() {
            @Override
            public void validate(Object username) throws Validator.InvalidValueException {
                Collection<User> users = (Collection<User>) containerDataSource.getItemIds();
                if (users.stream().anyMatch((u) -> {
                    return username.equals(u.getUsername());
                })) {
                    throw new Validator.InvalidValueException("Username already in use!");
                }

            }
        });
        usernameTextF.addBlurListener((FieldEvents.BlurEvent event) -> {
            AbstractTextField c = ((AbstractTextField) event.getComponent());
            if (c.getValue() != null) {
                c.setValue(c.getValue().trim());
            }
        });

        final TextField passwordTextF = new TextField("Password");
        passwordTextF.setRequired(true);
        passwordTextF.setRequiredError("Password is required!");
        passwordTextF.addValidator(new StringLengthValidator("The Password is too short!", 8, null, false));
        passwordTextF.addBlurListener((FieldEvents.BlurEvent event) -> {
            AbstractTextField c = ((AbstractTextField) event.getComponent());
            if (c.getValue() != null) {
                c.setValue(c.getValue().trim());
            }
        });

        final TextField nameTextF = new TextField("Name");
        nameTextF.setRequired(true);
        nameTextF.setRequiredError("Name is required!");
        nameTextF.addValidator(new StringLengthValidator("The Name is too short!", 2, null, false));
        nameTextF.addBlurListener((FieldEvents.BlurEvent event) -> {
            AbstractTextField c = ((AbstractTextField) event.getComponent());
            if (c.getValue() != null) {
                c.setValue(c.getValue().trim());
            }
        });

        final TextField emailTextF = new TextField("Email");
        emailTextF.setImmediate(true);
        emailTextF.addValidator(new EmailValidator("The Email is invalid"));
        emailTextF.setRequired(true);
        emailTextF.setRequiredError("Email is required!");

        formLayout.addComponents(usernameTextF, passwordTextF, nameTextF, emailTextF);

        Button createButton = new Button("Create User", (Button.ClickEvent event) -> {
            try {
                usernameTextF.validate();
                passwordTextF.validate();
                nameTextF.validate();
                emailTextF.validate();

                containerDataSource.addBean(new User(usernameTextF.getValue(), PermissionService.hashPassword(passwordTextF.getValue()), nameTextF.getValue(), emailTextF.getValue(), null));

                close();
            } catch (Validator.InvalidValueException e) {
                Notification.show("Validation Error!", e.getLocalizedMessage(), Notification.Type.WARNING_MESSAGE);
            }
        });

        Button cancelButton = new Button("Cancel", (Button.ClickEvent event) -> {
            close();
        });

        formLayout.addComponents(createButton, cancelButton);
        formLayout.setSizeUndefined();
        formLayout.setMargin(true);
        super.setContent(formLayout);
    }

}
