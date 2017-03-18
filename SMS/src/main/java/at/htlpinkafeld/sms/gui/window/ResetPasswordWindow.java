/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.window;

import at.htlpinkafeld.sms.pojo.User;
import at.htlpinkafeld.sms.service.PermissionService;
import at.htlpinkafeld.sms.service.RandomString;
import com.vaadin.data.Container;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Window which is used to reset/set the password of a user
 *
 * @author masix
 */
public class ResetPasswordWindow extends Window {

    /**
     * Constructor for the window which is used to reset the password
     *
     * @param userContainer The container with the user whos password will be
     * reset
     * @param objectId the objectId identifying the user whos password will be
     * changed
     */
    public ResetPasswordWindow(Container userContainer, Object objectId) {
        super("Reset Password");
        super.center();
        super.setModal(true);

        FormLayout mainLayout = new FormLayout();
        mainLayout.setSizeUndefined();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        RandomString rs = new RandomString(10);

        TextField textField = new TextField("new Password", rs.nextString());
        textField.setRequired(true);
        textField.addValidator(new StringLengthValidator("The Password is too short!", 8, null, false));
        mainLayout.addComponent(textField);

        mainLayout.addComponent(new Button("Reset", (event) -> {
            textField.validate();

            userContainer.getContainerProperty(objectId, "password").setValue(PermissionService.hashPassword(textField.getValue()));
            super.close();
        }));

        mainLayout.addComponent(new Button("Cancel", (event) -> {

            super.close();
        }));
        super.setContent(mainLayout);
    }

}
