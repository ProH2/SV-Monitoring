/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.gui.container.HostgroupHierarchical_Container;
import at.htlpinkafeld.sms.pojos.Hostgroup;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.Window;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author masix
 */
public class EditHostgroupWindow extends Window {

    private Container hostGroupContainer;
    private final String hostgroupName;

    public EditHostgroupWindow(HostgroupHierarchical_Container hostGroupContainer, String hostgroupName) {
        super(hostgroupName == null ? "add Hostgroup" : "edit Hostgroup");

        super.center();
        super.setModal(true);

        this.hostGroupContainer = hostGroupContainer;
        this.hostgroupName = hostgroupName;

        FormLayout mainLayout = new FormLayout();

        TextField hostgroupNameTextField = new TextField("Hostgroup name", hostgroupName == null ? "" : hostgroupName);
        hostgroupNameTextField.addValidator(new StringLengthValidator("Length of Hostgroup name is invalid", 4, 20, false));

        mainLayout.addComponent(hostgroupNameTextField);

        TwinColSelect hostassignmentColSelect = new TwinColSelect(null, ContainerFactory.createHostContainer().getItemIds());

        if (hostgroupName != null) {
            BeanItem<Hostgroup> hostGroupItem = (BeanItem<Hostgroup>) hostGroupContainer.getItem(hostgroupName);
            hostassignmentColSelect.setValue(hostGroupItem.getBean().getHostlist());
        }
        mainLayout.addComponent(hostassignmentColSelect);

        Button addButton = new Button(hostgroupName == null ? "Add hostgroup" : "save hostgroup");
        addButton.addClickListener((event) -> {
            hostgroupNameTextField.validate();

            if (hostgroupName == null) {

                Hostgroup newHostgroup = new Hostgroup();
                newHostgroup.setName(hostgroupNameTextField.getValue());
                newHostgroup.setHostlist(new LinkedList((Collection) hostassignmentColSelect.getValue()));

                hostGroupContainer.addHostgroup(newHostgroup);
            } else {
                BeanItem<Hostgroup> hostGroupItem = (BeanItem<Hostgroup>) hostGroupContainer.getItem(hostgroupName);

                hostGroupItem.getBean().setName(hostgroupNameTextField.getValue());
                hostGroupItem.getBean().setHostlist(new LinkedList((Collection) hostassignmentColSelect.getValue()));

                hostGroupContainer.fireItemSetChange();
            }
            super.close();
        });
        mainLayout.addComponent(addButton);

        mainLayout.setSizeUndefined();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        super.setContent(mainLayout);
    }

}
