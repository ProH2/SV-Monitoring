/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.pojos.Service;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Martin Six
 */
public class ServiceDetailWindow extends Window {

    private Service service;

    public ServiceDetailWindow(Service service) {
        super(service.getName());
        super.center();
        super.setModal(true);
        this.service = service;

        FormLayout dataLayout = new FormLayout();

        //add Layout Components
        TextField servicenameText = new TextField("Servicename", service.getName());
        servicenameText.setReadOnly(true);
        dataLayout.addComponent(servicenameText);

        TextField hostnameText = new TextField("Hostname", service.getHostname());
        hostnameText.setReadOnly(true);
        dataLayout.addComponent(hostnameText);

        TextField statusText = new TextField("Status", service.getStatus().name());
        statusText.setReadOnly(true);
        dataLayout.addComponent(statusText);

        TextArea hostInformationText = new TextArea("Information", service.getInformation());
        hostInformationText.setReadOnly(true);
        dataLayout.addComponent(hostInformationText);

        TextField lastCheckedText = new TextField("Last checked", service.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        lastCheckedText.setReadOnly(true);
        dataLayout.addComponent(lastCheckedText);

        TextField durationText = new TextField("Duration", service.getDuration().toString());
        durationText.setReadOnly(true);
        dataLayout.addComponent(durationText);

        dataLayout.setSizeFull();
        dataLayout.setMargin(true);

        //add Comment List
        IndexedContainer testComments = new IndexedContainer();
        testComments.addContainerProperty("comment", String.class, "testComment");
        for (int i = 1; i < 30; i++) {
            testComments.addItem("test " + i).getItemProperty("comment").setValue("test " + i);
        }
        Grid commentGrid = new Grid("Kommentare", testComments);
        commentGrid.setEditorEnabled(true);
        commentGrid.setSizeFull();

        HorizontalSplitPanel parentSplitPanel = new HorizontalSplitPanel(dataLayout, commentGrid);
        parentSplitPanel.setSizeUndefined();
        parentSplitPanel.setWidth(860, Unit.PIXELS);

        super.setContent(parentSplitPanel);
    }

}
