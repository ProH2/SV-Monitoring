/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.gui.container.HashMapWithListeners;
import at.htlpinkafeld.sms.pojos.Service;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * A Panel which shows the Data of a {@link Service} which wraps a
 * {@link Map.Entry}
 *
 * @author Martin Six
 */
public class ServicePanel extends Panel implements HashMapWithListeners.MapChangeListener {

    private Map.Entry<String, Service> serviceEntry;

    private final Label servicenameLabel;
    private final Label statusLabel;
    private final Label serviceInformationLabel;
    private final Label lastCheckedLabel;
    private final Label durationLabel;

    /**
     * Constructor for the ServicePanel
     *
     * @param serviceEntry Service-Entry which is wrapped with the panel
     */
    public ServicePanel(Map.Entry<String, Service> serviceEntry) {
        this.serviceEntry = serviceEntry;
        Service service = serviceEntry.getValue();

        AbsoluteLayout parentLayout = new AbsoluteLayout();

        //add Layout Components
        servicenameLabel = new Label(service.getName());
        servicenameLabel.addStyleName("subheading");
        servicenameLabel.setSizeUndefined();
        parentLayout.addComponent(servicenameLabel, "top:5%; left:5%; right:60%;");

        statusLabel = new Label(service.getStatus().name());
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

        parentLayout.addComponent(serviceInformationLabel = new Label(service.getInformation()), "top:20%; left:6%; bottom:20%; right:5%;");

        parentLayout.addComponent(lastCheckedLabel = new Label(service.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))), "bottom:5%; left:6%; right:40%;");
        parentLayout.addComponent(durationLabel = new Label(service.getDuration().toString()), " bottom: 5%; right:5%; left:70%;");
        parentLayout.setSizeFull();

        super.setHeight(180, Sizeable.Unit.PIXELS);
        super.setWidth(300, Unit.PIXELS);
        super.setContent(parentLayout);

    }

    /**
     * method used to update the labels when the
     * {@link HashMapWithListeners.MapChangeListener} was notified
     */
    private void updateLabels() {
        Service service = serviceEntry.getValue();
        statusLabel.setValue(service.getStatus().name());
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

        serviceInformationLabel.setValue(service.getInformation());
        lastCheckedLabel.setValue(service.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        durationLabel.setValue(service.getDuration().toString());
    }

    /**
     * calls {@link #updateLabels() } when it is notified
     */
    @Override
    public void mapChanged() {
        updateLabels();
        super.markAsDirtyRecursive();
    }

}
