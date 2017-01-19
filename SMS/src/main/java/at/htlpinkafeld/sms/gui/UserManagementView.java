/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.pojo.User;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.event.SelectionEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.MultiSelectionModel;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;

/**
 * View for the UserManagement Page
 *
 * @author Martin Six
 */
public class UserManagementView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "usermanagementview";

    /**
     * PropertyId constant for {@link Container}
     */
    public static final String USERNR_PROPERTY = "userNr";
    /**
     * PropertyId constant for {@link Container}
     */
    public static final String USERNAME_PROPERTY = "username";
    /**
     * PropertyId constant for {@link Container}
     */
    public static final String PASSWORD_PROPERTY = "password";
    /**
     * PropertyId constant for {@link Container}
     */
    public static final String NAME_PROPERTY = "name";
    /**
     * PropertyId constant for {@link Container}
     */
    public static final String EMAIL_PROPERTY = "email";
    /**
     * PropertyId constant for {@link Container}
     */
    public static final String PHONENR_PROPERTY = "phoneNr";
    /**
     * PropertyId constant for {@link Container}
     */
    public static final String EDITUSER_COLUMN = "editUser";
    /**
     * PropertyId constant for {@link Container}
     */
    public static final String DELETEUSER_COLUMN = "deleteUser";

    public UserManagementView() {
        super.addComponent(new MenuBarComponent());

        VerticalLayout innerLayout = new VerticalLayout();

        Label head = new Label("Benutzerverwaltung ", ContentMode.TEXT);
        head.setStyleName("heading");

        Grid grid = new Grid(createIndexedContainer());
        grid.removeColumn(USERNR_PROPERTY);
        grid.setColumnOrder(USERNAME_PROPERTY, PASSWORD_PROPERTY, NAME_PROPERTY, EMAIL_PROPERTY, PHONENR_PROPERTY, EDITUSER_COLUMN, DELETEUSER_COLUMN);

        grid.setSelectionMode(SelectionMode.MULTI);
        grid.setEditorEnabled(true);

        MultiSelectionModel selection = (MultiSelectionModel) grid.getSelectionModel();

        grid.getColumn(EDITUSER_COLUMN).setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
            @Override
            public void click(ClickableRenderer.RendererClickEvent event) {
                grid.editItem(event.getItemId());
            }
        }));

        grid.getColumn(DELETEUSER_COLUMN).setRenderer(new ButtonRenderer(new ClickableRenderer.RendererClickListener() {
            @Override
            public void click(ClickableRenderer.RendererClickEvent event) {
                grid.getContainerDataSource().removeItem(event.getItemId());
            }
        }));

        Button newUserButton = new Button("New User", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().addWindow(new NewUserWindow(grid.getContainerDataSource()));
            }
        });
        newUserButton.setSizeFull();

        Button delSelectedButton = new Button("Delete Selected");
        delSelectedButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // Delete all selected data items
                for (Object itemId : selection.getSelectedRows()) {
                    grid.getContainerDataSource().removeItem(itemId);
                }

                // Otherwise out of sync with container
                grid.getSelectionModel().reset();

                // Disable after deleting
                delSelectedButton.setEnabled(false);
            }
        });
        delSelectedButton.setEnabled(grid.getSelectedRows().size() > 0);

        grid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent event) {
                // Allow deleting only if there's any selected
                delSelectedButton.setEnabled(grid.getSelectedRows().size() > 0);
            }
        });

        head.setSizeUndefined();

        newUserButton.setSizeFull();
        delSelectedButton.setSizeFull();

        grid.appendFooterRow();
        grid.getFooterRow(0).join(USERNAME_PROPERTY, PASSWORD_PROPERTY, NAME_PROPERTY).setComponent(newUserButton);
        grid.getFooterRow(0).join(PHONENR_PROPERTY, EDITUSER_COLUMN, DELETEUSER_COLUMN).setComponent(delSelectedButton);

        grid.setSizeFull();

        innerLayout.addComponent(head);
        innerLayout.addComponent(grid);

        innerLayout.setSizeFull();

        super.addComponent(innerLayout);

        super.setComponentAlignment(innerLayout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    /**
     * Convenience Method to create a {@link Container} for testing purpose.
     *
     * @return
     */
    private Container.Indexed createIndexedContainer() {

//        BeanItemContainer<User> container = new BeanItemContainer<>(User.class);
//
//        container.addBean(new User(1, "Dogist", "1234", "Martin", "noplan@gmc.at", "5421575"));
//        container.addBean(new User(2, "Irish", "4321", "Sebastian", "noplan@qmail.com", "5788775"));
        IndexedContainer container = new IndexedContainer();

        container.addContainerProperty(USERNR_PROPERTY, Integer.class, "Default UserNr");
        container.addContainerProperty(USERNAME_PROPERTY, String.class, "Default Username");
        container.addContainerProperty(PASSWORD_PROPERTY, String.class, "Default Password");
        container.addContainerProperty(NAME_PROPERTY, String.class, "Default Name");
        container.addContainerProperty(EMAIL_PROPERTY, String.class, "Default Email");
        container.addContainerProperty(PHONENR_PROPERTY, String.class, "Default PhoneNr");

        container.addItem();
        container.addItem();

        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(container);

        gpc.addGeneratedProperty(EDITUSER_COLUMN, new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return "Edit User";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        gpc.addGeneratedProperty(DELETEUSER_COLUMN, new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return "Delete User";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        return gpc;
    }
}
