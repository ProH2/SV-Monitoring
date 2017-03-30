/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.service.JSONService;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author masix
 */
@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = SMS_Main.class, productionMode = false, resourceCacheTime = 10, widgetset = "at.htlpinkafeld.sms.gui.AppWidgetSet")
public class SMS_UIServlet extends VaadinServlet {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        JSONService.refresh();
    }

    @Override
    public void destroy() {
        JSONService.kill();
        super.destroy();
    }
    
    

}
