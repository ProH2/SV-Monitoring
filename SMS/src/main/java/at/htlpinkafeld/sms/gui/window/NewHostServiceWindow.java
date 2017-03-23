/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.window;

import at.htlpinkafeld.sms.gui.Host_Service_ManagementView;
import at.htlpinkafeld.sms.gui.container.HostServiceHierarchical_Container;
import at.htlpinkafeld.sms.pojo.Host;
import at.htlpinkafeld.sms.pojo.Service;
import at.htlpinkafeld.sms.service.AddHostsAndServices_Service;
import at.htlpinkafeld.sms.service.KeyParameterEnum;
import at.htlpinkafeld.sms.service.LinuxServiceCommands;
import at.htlpinkafeld.sms.service.ServiceCommandsInterface;
import at.htlpinkafeld.sms.service.WindowsServiceCommands;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Window for adding new {@link Host Hosts} and {@link Service Services}. used
 * in {@link Host_Service_ManagementView}
 *
 * @author Martin Six
 */
public class NewHostServiceWindow extends Window {

    private NativeSelect osSelect;

    private TextField destinationTextField;

    private NativeSelect serivceTypeSelect;
    private TextField warningLimit;
    private TextField criticalLimit;

    private TextField nameTextField;

    /**
     * Constructor for NewHostServiceWindow
     *
     * @param host the Host to which the Service is added.
     * <p>
     * If Host is null the window will be used to add a new Host. If the Host is
     * not null the window will be used to add a Service under the Host.</p>
     * @param hostServiceHierarchical_Container Container which contains all the
     * Hosts and Services
     */
    public NewHostServiceWindow(Host host, HostServiceHierarchical_Container hostServiceHierarchical_Container) {
        super(host == null ? "Create new Host" : "Create new Service for " + host.getHostname());
        super.center();
        super.setModal(true);

        FormLayout newHostServiceFormLayout = new FormLayout();

        osSelect = new NativeSelect("Operation System", Arrays.asList("Windows", "Linux"));
        osSelect.setNullSelectionAllowed(false);
        osSelect.setRequired(true);
        osSelect.setRequiredError("Operation System is required!");

        osSelect.select("Linux");
        newHostServiceFormLayout.addComponent(osSelect);

        nameTextField = new TextField(host == null ? "Hostname" : "Servicename");
        nameTextField.setRequired(true);
        nameTextField.setRequiredError(nameTextField.getCaption() + " required!");

        newHostServiceFormLayout.addComponent(nameTextField);

        //if this window is used to add a Host
        if (host == null) {
            nameTextField.addValidator(new AbstractStringValidator("The Hostname has to be unique") {
                @Override
                protected boolean isValidValue(String value) {
                    return !hostServiceHierarchical_Container.containsId(value.trim());
                }
            });

            destinationTextField = new TextField("IP");
            destinationTextField.setRequired(true);
            destinationTextField.setRequiredError(destinationTextField.getCaption() + " is required!");

            newHostServiceFormLayout.addComponent(destinationTextField);
        } else //If this window is used to add a Service
        {
            nameTextField.addValidator(new AbstractStringValidator("The Servicename has to be unique") {
                @Override
                protected boolean isValidValue(String value) {
                    return !hostServiceHierarchical_Container.containsId(host.getHostname() + "/" + value.trim());
                }
            });

            serivceTypeSelect = new NativeSelect("Service Type", Arrays.asList(LinuxServiceCommands.values()));
            serivceTypeSelect.setNullSelectionAllowed(false);
            serivceTypeSelect.setRequired(true);
            serivceTypeSelect.setRequiredError("Service Type is required!");

            osSelect.addValueChangeListener((Property.ValueChangeEvent event) -> {
                if ("Linux".equals(event.getProperty().getValue())) {
                    serivceTypeSelect.setContainerDataSource(new BeanItemContainer(LinuxServiceCommands.class, Arrays.asList(LinuxServiceCommands.values())));
                } else if ("Windows".equals(event.getProperty().getValue())) {
                    serivceTypeSelect.setContainerDataSource(new BeanItemContainer(WindowsServiceCommands.class, Arrays.asList(WindowsServiceCommands.values())));
                }
            });

            newHostServiceFormLayout.addComponent(serivceTypeSelect);

            warningLimit = new TextField("Warning Limit");
            warningLimit.setRequired(true);
            warningLimit.setRequiredError("Warning Limit is required!");

            newHostServiceFormLayout.addComponent(warningLimit);
            criticalLimit = new TextField("Critical Limit");
            criticalLimit.setRequired(true);
            criticalLimit.setRequiredError("Critical Limit is required!");

            newHostServiceFormLayout.addComponent(criticalLimit);
        }

        Button addButton = new Button(host == null ? "Add Host" : "Add Service");

        addButton.addClickListener((event) -> {
            try {
                nameTextField.validate();
                if (host == null) {
                    destinationTextField.validate();

                    AddHostsAndServices_Service.addHost((String)this.osSelect.getValue(), nameTextField.getValue(), destinationTextField.getValue());
                } else {
                    warningLimit.validate();
                    criticalLimit.validate();
                    serivceTypeSelect.validate();

                    Map<KeyParameterEnum, String> parameterMap = new HashMap();
                    parameterMap.put(KeyParameterEnum.WARNING, warningLimit.getValue());
                    parameterMap.put(KeyParameterEnum.CRITICAL, criticalLimit.getValue());

                    AddHostsAndServices_Service.addService((String)this.osSelect.getValue() , host.getHostname(), (ServiceCommandsInterface) serivceTypeSelect.getValue(), parameterMap, nameTextField.getValue().trim());
                }
                close();
            } catch (Validator.InvalidValueException e) {
                Notification.show("Validation Error!", e.getLocalizedMessage(), Notification.Type.WARNING_MESSAGE);
            }
        });

        newHostServiceFormLayout.addComponent(addButton);

        newHostServiceFormLayout.setSizeUndefined();
        newHostServiceFormLayout.setMargin(true);
        newHostServiceFormLayout.setSpacing(true);

        super.setContent(newHostServiceFormLayout);

    }

}
