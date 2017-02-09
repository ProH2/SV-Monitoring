/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.pojos.Service;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Martin Six
 */
public class ServicePanel extends Panel {

    private Service service;

    public ServicePanel() {
    }

    public ServicePanel(Service service) {
        this.service = service;

        AbsoluteLayout parentLayout = new AbsoluteLayout();

        //add Layout Components
        Label servicenameLabel = new Label(service.getName());
        servicenameLabel.addStyleName("subheading");
        servicenameLabel.setSizeUndefined();
        parentLayout.addComponent(servicenameLabel, "top:5%; left:5%; right:60%;");

        Label statusLabel = new Label(service.getStatus().name());
        switch (service.getStatus()) {
            case OK:
                statusLabel.addStyleName("green");
                break;
            case PENDING:
                statusLabel.addStyleName("gray");
                break;
            case WARNING:
                statusLabel.addStyleName("yellow");
                break;
            case UNKNOWN:
            //fall-through
            case CRITICAL:
                statusLabel.addStyleName("red");
                break;
            default:
        }
        statusLabel.setSizeUndefined();
        parentLayout.addComponent(statusLabel, "top:5%; right:5%;");

        Label hostInformationLabel = new Label(service.getInformation());
        parentLayout.addComponent(hostInformationLabel, "top:20%; left:6%; bottom:20%; right:5%;");

        parentLayout.addComponent(new Label(service.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))), "bottom:5%; left:6%; right:40%;");
        parentLayout.addComponent(new Label(service.getDuration().toString()), " bottom: 5%; right:5%; left:70%;");
        parentLayout.setSizeFull();

        super.setHeight(180, Sizeable.Unit.PIXELS);
        super.setContent(parentLayout);

    }
}
