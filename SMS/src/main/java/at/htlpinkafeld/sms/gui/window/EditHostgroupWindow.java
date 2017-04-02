/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.window;

import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.gui.container.HostgroupHierarchical_Container;
import at.htlpinkafeld.sms.pojo.Hostgroup;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.Window;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Window which is used to edit or create {@link Hostgroup Hostgroups}
 *
 * @author masix
 */
public class EditHostgroupWindow extends Window {

    private Container hostGroupContainer;
    private final String hostgroupName;
    private IndexedContainer hostContainer;

    /**
     * Constructor for EditHostgroupWindow
     *
     * @param hostGroupContainer the container where the {@link Hostgroup} will
     * be inserted/updated
     * @param hostgroupName the name of the hostgroup which will be edited or
     * null
     */
    public EditHostgroupWindow(HostgroupHierarchical_Container hostGroupContainer, String hostgroupName) {
        super(hostgroupName == null ? "Add Hostgroup" : "Edit Hostgroup");

        super.center();
        super.setModal(true);

        this.hostGroupContainer = hostGroupContainer;
        this.hostgroupName = hostgroupName;

        this.hostContainer = new IndexedContainer();
        hostContainer.addContainerProperty("name", String.class, "");
        ContainerFactory.createHostContainer().getItemIds().stream().sorted().forEach((t) -> {
            hostContainer.addItem(t).getItemProperty("name").setValue(t);
        });

        FormLayout mainLayout = new FormLayout();

        TextField hostgroupNameTextField = new TextField("Hostgroup name", hostgroupName == null ? "" : hostgroupName);
        hostgroupNameTextField.addValidator(new StringLengthValidator("Length of Hostgroup name is invalid", 4, 20, false));
        hostgroupNameTextField.addValidator(new AbstractValidator("Hostgroupname has to be unique!") {
            @Override
            protected boolean isValidValue(Object value) {
                //Hostgroupname is the same as before or is unique
                return value.equals(hostgroupName) || !hostGroupContainer.containsId(value);
            }

            @Override
            public Class getType() {
                return String.class;
            }
        });

        mainLayout.addComponent(hostgroupNameTextField);

        TextField searchHostField = new TextField("Search Host");

        searchHostField.addValueChangeListener((event) -> {
            hostContainer.removeAllContainerFilters();

            String searchTerm = searchHostField.getValue();
            if (!searchTerm.isEmpty()) {
                Container.Filter f = null;
                f = new SimpleStringFilter("name", searchTerm, true, true);

                hostContainer.addContainerFilter(f);

            }
        });

        mainLayout.addComponent(searchHostField);

        TwinColSelect hostassignmentColSelect = new TwinColSelect(null, hostContainer);
        hostassignmentColSelect.setLeftColumnCaption("available Hosts");
        hostassignmentColSelect.setRightColumnCaption("assigned Hosts");

        if (hostgroupName != null) {
            BeanItem<Hostgroup> hostGroupItem = (BeanItem<Hostgroup>) hostGroupContainer.getItem(hostgroupName);
            hostassignmentColSelect.setValue(hostGroupItem.getBean().getHostlist());
        }
        mainLayout.addComponent(hostassignmentColSelect);

        Button addButton = new Button(hostgroupName == null ? "Add Hostgroup" : "Save Hostgroup");
        addButton.addClickListener((event) -> {
            hostgroupNameTextField.validate();

            if (hostgroupName == null) {

                Hostgroup newHostgroup = new Hostgroup();
                newHostgroup.setName(hostgroupNameTextField.getValue());
                newHostgroup.setHostlist(new LinkedList((Collection) hostassignmentColSelect.getValue()));

                hostGroupContainer.addHostgroup(newHostgroup);
                Page.getCurrent().reload();
            } else {
                BeanItem<Hostgroup> hostGroupItem = (BeanItem<Hostgroup>) hostGroupContainer.getItem(hostgroupName);

                hostGroupItem.getBean().setName(hostgroupNameTextField.getValue());
                hostGroupItem.getBean().setHostlist(new LinkedList((Collection) hostassignmentColSelect.getValue()));

                hostGroupContainer.updateHostgroup(hostGroupItem.getBean());

                Page.getCurrent().reload();
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
