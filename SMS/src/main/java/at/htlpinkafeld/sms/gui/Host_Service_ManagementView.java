/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.Arrays;

/**
 *
 * @author Martin Six
 */
public class Host_Service_ManagementView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "host_service_managementview";

    public Host_Service_ManagementView() {
        super.addComponent(new MenuBarComponent());

        FormLayout parentLayout = new FormLayout();

        NativeSelect typSelect = new NativeSelect("Typ", Arrays.asList("Host", "Service"));
        typSelect.setRequired(true);
        typSelect.select(typSelect.getItemIds().toArray()[0]);
        typSelect.setNullSelectionAllowed(false);

        parentLayout.addComponent(typSelect);

        TextField destinationTextField = new TextField("IP");
        destinationTextField.setRequired(true);

        parentLayout.addComponent(destinationTextField);

        TextField nameTextField = new TextField(typSelect.getValue().toString() + "name");
        nameTextField.setRequired(true);

        parentLayout.addComponent(nameTextField);

        Button addButton = new Button("Add " + typSelect.getValue());
        parentLayout.addComponent(addButton);

        typSelect.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                nameTextField.setCaption(event.getProperty().getValue() + "name");
                addButton.setCaption("Add " + event.getProperty().getValue());
                if (event.getProperty().getValue().equals("Host")) {
                    destinationTextField.setCaption("IP");
                } else if (event.getProperty().getValue().equals("Service")) {
                    destinationTextField.setCaption("Hostname");
                }
            }
        });

        parentLayout.setSizeUndefined();
        parentLayout.setMargin(true);
        parentLayout.setSpacing(true);

        super.addComponent(parentLayout);

        super.setComponentAlignment(parentLayout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
