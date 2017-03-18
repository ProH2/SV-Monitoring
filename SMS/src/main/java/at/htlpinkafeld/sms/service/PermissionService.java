/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.service;

import at.htlpinkafeld.sms.pojo.User;
import com.vaadin.server.VaadinSession;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Service to check the Permissions of the current User
 *
 * @author masix
 */
public class PermissionService {

    private static final boolean ALL_USERS_ARE_ADMIN = true;

    /**
     * static method which checks the {@link VaadinSession} for "currentUser"
     * User and verifies if he is an admin
     *
     * @return true if the currentUser is an Admin, false otherwise
     * @throws NoUserLoggedInException is thrown if the attribute "currentUser"
     * is not set
     */
    public static boolean isAdmin() throws NoUserLoggedInException {

        try {
            User currentUser = (User) VaadinSession.getCurrent().getAttribute(User.class);
            if (currentUser != null) {
                if (ALL_USERS_ARE_ADMIN && !"Kant".equals(currentUser.getUsername())) {
                    return true;
                } else {
                    //TODO do something
                    return false;
                }
            } else {
                throw new NoUserLoggedInException();
            }
        } catch (NullPointerException exception) {
            throw new NoUserLoggedInException();
        }
    }

    public static String hashPassword(String password) {
        return DigestUtils.sha512Hex(password);
    }

}
