/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.service;

import at.htlpinkafeld.sms.dao.DutyDao;
import at.htlpinkafeld.sms.dao.DutyDaoImpl;
import at.htlpinkafeld.sms.pojo.Duty;
import at.htlpinkafeld.sms.pojo.User;
import at.htlpinkafeld.sms.pojo.Host;
import at.htlpinkafeld.sms.pojo.Service;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author DarkHell2
 */
public class EmailService {

    private static DateTimeFormatter dateTimeFormatter;
    private static DutyDao dutydao;

    private static final String SERVER_EMAILADDRESS = "zes@sviss.co.at";

    static {
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        dutydao = new DutyDaoImpl();
    }

    private static void sendEmail(String subject, String body, User from, List<User> to) {
        String host = "smtp.world4you.com";
        //String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "25");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        if (!to.isEmpty()) {

            // Get the default Session object.
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SERVER_EMAILADDRESS, "wLichanda=");
                }
            });

            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                if (from != null) {
                    message.setFrom(new InternetAddress(from.getEmail()));
                } else {
                    message.setFrom(new InternetAddress(SERVER_EMAILADDRESS));
                }

                // Set To: header field of the header.
                for (User u : to) {
                    if (!u.getEmail().isEmpty()) {
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(u.getEmail()));
                    }
                }

                // Set Subject: header field
                message.setSubject(subject, "utf-8");

                // Now set the actual message
                message.setText(body, "utf-8", "html");

                // Send message
                Transport.send(message);
            } catch (MessagingException mex) {
                throw new RuntimeException(mex);
            }
        }
    }


    
        public static void sendNotificationMailHost(String hostname, Host.Hoststatus oldstatus, Host.Hoststatus newstatus, String pluginOutput) {
        String subject;
        String body;
        List<Duty> dList = dutydao.getDutiesInTime(LocalDateTime.now());
        List<User> uList = new ArrayList<>();
        
        for(int i=0; i<dList.size(); i++){
            uList.add(dList.get(i).getUser());
        }
        
        
        subject = hostname + "wechselte von " + oldstatus + " zu " + newstatus + "!";
        body = LocalDateTime.now().format(dateTimeFormatter) + pluginOutput;

        sendEmail(subject, body, null, uList);
    }
        
    public static void sendNotificationMailService(String servicename, Service.Servicestatus oldstatus, Service.Servicestatus newstatus, String pluginOutput) {
        String subject;
        String body;
        List<Duty> dList = dutydao.getDutiesInTime(LocalDateTime.now());
        List<User> uList = new ArrayList<>();
        
        for(int i=0; i<dList.size(); i++){
            uList.add(dList.get(i).getUser());
        }
        
        
        subject = servicename + "wechselte von " + oldstatus + " zu " + newstatus + "!";
        body = LocalDateTime.now().format(dateTimeFormatter) + pluginOutput;

        sendEmail(subject, body, null, uList);
    }

    public static void sendUserNewPasswordEmail(String newPassword, User user) {
        String subject;
        String body;

        subject = "SVISS - Monitoring: Passwort wurde zurückgesetzt";
        body = "Dein neues Passwort für den Benutzer " + user.getUsername() + " lautet: " + newPassword;

        List<User> userL = new ArrayList<>();
        userL.add(user);

        sendEmail(subject, body, null, userL);
    }

    
}
