/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.pojos.Host;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Martin Six
 */
public class HostPanel extends Panel {

    private Host host;

    public HostPanel(Host host) {
        this.host = host;

        AbsoluteLayout parentLayout = new AbsoluteLayout();

        //add Layout Components
        Label hostnameLabel = new Label(host.getHostname());
        hostnameLabel.addStyleName("subheading");
        parentLayout.addComponent(hostnameLabel, "top:5%; left:5%; right:70%;");

        Label statusLabel = new Label(host.getStatus().name());
        switch (host.getStatus()) {
            case UP:
                statusLabel.addStyleName("green");
                break;
            case PENDING:
                statusLabel.addStyleName("gray");
                break;
            case UNREACHABLE:
            //fall through
            case DOWN:
                statusLabel.addStyleName("red");
                break;
            default:
        }
        statusLabel.setSizeUndefined();
        parentLayout.addComponent(statusLabel, "top:5%; right:5%;");

        Label hostInformationLabel = new Label(host.getInformation());
        parentLayout.addComponent(hostInformationLabel, "top:20%; left:6%; bottom:20%; right:5%;");

        parentLayout.addComponent(new Label(host.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))), "bottom:5%; left:6%; right:40%;");
        parentLayout.addComponent(new Label(host.getDuration().toString()), " bottom: 5%; right:5%; left:70%;");
        parentLayout.setSizeFull();

        super.setHeight(180, Unit.PIXELS);
        super.setContent(parentLayout);

    }

}
