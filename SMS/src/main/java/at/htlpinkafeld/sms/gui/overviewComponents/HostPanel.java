/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.gui.container.HashMapWithListeners;
import at.htlpinkafeld.sms.gui.container.MapReferenceContainer;
import at.htlpinkafeld.sms.pojo.Host;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * A Panel which shows the Data of a {@link Host} which wraps a
 * {@link Map.Entry}
 *
 * @author Martin Six
 */
public class HostPanel extends Panel implements HashMapWithListeners.MapChangeListener {

    private Map.Entry<String, Host> hostEntry;

    private final MapReferenceContainer<Host> container;

    private final Label hostnameLabel;
    private final Label statusLabel;
    private final Label hostInformationLabel;
    private final Label lastCheckedLabel;
    private final Label durationLabel;

    /**
     * Constructor for the HostPanel
     *
     * @param hostEntry Host-Entry which is wrapped with the panel
     * @param container container of the Host-Entry which is used for
     * Synchronisation
     */
    public HostPanel(Map.Entry<String, Host> hostEntry, MapReferenceContainer<Host> container) {
        this.hostEntry = hostEntry;
        this.container = container;

        Host host = hostEntry.getValue();

        AbsoluteLayout parentLayout = new AbsoluteLayout();

        //add Layout Components
        hostnameLabel = new Label(host.getHostname());
        hostnameLabel.addStyleName("subheading");
        parentLayout.addComponent(hostnameLabel, "top:5%; left:5%; right:70%;");

        statusLabel = new Label(host.getStatus().name());
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

        hostInformationLabel = new Label(host.getInformation());
        parentLayout.addComponent(hostInformationLabel, "top:20%; left:6%; bottom:20%; right:5%;");

        parentLayout.addComponent(lastCheckedLabel = new Label(host.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))), "bottom:5%; left:6%; right:40%;");
        parentLayout.addComponent(durationLabel = new Label(getDurationString(host.getDuration())), " bottom: 5%; right:5%; left:70%;");
        parentLayout.setSizeFull();

        super.setHeight(180, Unit.PIXELS);
        super.setWidth(300, Unit.PIXELS);
        super.setContent(parentLayout);

    }

    /**
     * method used to update the labels when the
     * {@link HashMapWithListeners.MapChangeListener} was notified
     */
    private void updateLabels() {
        Host host = hostEntry.getValue();
        statusLabel.setValue(host.getStatus().name());
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

        hostInformationLabel.setValue(host.getInformation());
        lastCheckedLabel.setValue(host.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        durationLabel.setValue(getDurationString(host.getDuration()));
    }

    /**
     * calls {@link #updateLabels() } when it is notified
     */
    @Override
    public void mapChanged() {
        this.hostEntry = ((BeanItem<Map.Entry<String, Host>>) container.getItem(this.hostEntry.getKey())).getBean();
        updateLabels();
        super.markAsDirty();
    }

    /**
     * Parses the {@link Duration} to days and hours, hours and minutes or
     * minutes and seconds, depending on the duration
     *
     * @param duration Duration which is parsed
     * @return the duration in a certain format
     */
    public static String getDurationString(Duration duration) {
        if (duration.toDays() == 0) {
            if (duration.toHours() == 0) {
                return "m: " + duration.toMinutes() + " s: " + (duration.toMillis() / 1000) % 60;
            } else {
                return "h: " + duration.toHours() + " m: " + duration.toMinutes() % 60;
            }
        } else {
            return "d: " + duration.toDays() + " h: " + duration.toHours() % 24;

        }
    }

}
