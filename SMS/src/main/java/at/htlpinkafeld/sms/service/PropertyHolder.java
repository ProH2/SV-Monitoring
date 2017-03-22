/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.service;

import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin Six
 */
public class PropertyHolder {

    public static final String ALL_USERS_ARE_ADMIN_PROPERTY = "ALL_USERS_ARE_ADMIN";
    public static final String HSQLDB_PROPERTY = "HSQLDB";
    public static final String MYSQLDB_PROPERTY = "MYSQLDB";

    private static final PropertyHolder INSTANCE = new PropertyHolder();

    private final Properties props;

    private PropertyHolder() {
        props = load();
    }

    public static PropertyHolder getInstance() {
        return INSTANCE;
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    private Properties load() {
        Properties p = new Properties();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties");

            p.load(is);
        } catch (IOException ex) {
            Logger.getLogger(PropertyHolder.class.getName()).log(Level.SEVERE, null, ex);
        }

        return p;
    }
}
