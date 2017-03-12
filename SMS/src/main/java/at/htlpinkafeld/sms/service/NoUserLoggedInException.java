/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.service;

/**
 * Exception which is thrown by {@link PermissionService#isAdmin()} if there is
 * currently no logged in User
 *
 * @author masix
 */
public class NoUserLoggedInException extends Exception {

}
