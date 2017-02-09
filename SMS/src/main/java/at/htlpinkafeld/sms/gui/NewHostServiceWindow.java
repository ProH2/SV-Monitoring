/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.pojos.Host;
import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import java.util.Arrays;

/**
 *
 * @author Martin Six
 */
public class NewHostServiceWindow extends Window {

    public NewHostServiceWindow(Host host) {
        super(host == null ? "Create new host" : "Create new Service for " + host.getHostname());
        super.center();
        super.setModal(true);

        FormLayout newHostServiceFormLayout = new FormLayout();

//        NativeSelect typSelect = new NativeSelect("Typ", Arrays.asList("Host", "Service"));
//        typSelect.setRequired(true);
//        typSelect.select(typSelect.getItemIds().toArray()[0]);
//        typSelect.setNullSelectionAllowed(false);
//
//        newHostServiceFormLayout.addComponent(typSelect);
        if (host == null) {
            TextField destinationTextField = new TextField("IP");
            destinationTextField.setRequired(true);

            newHostServiceFormLayout.addComponent(destinationTextField);
        }

        TextField nameTextField = new TextField(host == null ? "hostname" : "servicename");
        nameTextField.setRequired(true);

        newHostServiceFormLayout.addComponent(nameTextField);

        Button addButton = new Button(host == null ? "Add host" : "Add service");
        newHostServiceFormLayout.addComponent(addButton);

//        typSelect.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                nameTextField.setCaption(event.getProperty().getValue() + "name");
//                addButton.setCaption("Add " + event.getProperty().getValue());
//                if (event.getProperty().getValue().equals("Host")) {
//                    destinationTextField.setCaption("IP");
//                } else if (event.getProperty().getValue().equals("Service")) {
//                    destinationTextField.setCaption("Hostname");
//                }
//            }
//        });
        newHostServiceFormLayout.setSizeUndefined();
        newHostServiceFormLayout.setMargin(true);
        newHostServiceFormLayout.setSpacing(true);

        super.setContent(newHostServiceFormLayout);

//        super.setComponentAlignment(newHostServiceFormLayout, Alignment.MIDDLE_CENTER);
    }

}
