/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import at.htlpinkafeld.sms.gui.window.NewUserWindow;
import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.gui.container.DaoDelegatingContainer;
import at.htlpinkafeld.sms.gui.window.ResetPasswordWindow;
import at.htlpinkafeld.sms.pojo.User;
import at.htlpinkafeld.sms.service.NoUserLoggedInException;
import at.htlpinkafeld.sms.service.PermissionService;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;
import java.util.Collection;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * View for the UserManagement Page
 *
 * @author Martin Six
 */
public class UserManagementView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "usermanagementview";

    /**
     * Constructor for UserManagementView
     */
    public UserManagementView() {
        super.addComponent(((SMS_Main) UI.getCurrent()).getMenuBarComponent());

        VerticalLayout innerLayout = new VerticalLayout();

        baseUserContainer = ContainerFactory.createIndexedUserContainer();
        Grid grid = new Grid(addGeneratedProperties(baseUserContainer));
//Restructure auto-generated columns
        grid.removeColumn(USERNR_PROPERTY);
        grid.removeColumn(PASSWORD_PROPERTY);
        grid.removeColumn(DISABLED_PROPERTY);
        grid.removeColumn(PHONENR_PROPERTY);
        grid.setColumnOrder(USERNAME_PROPERTY, NAME_PROPERTY, RESETPASSW_COLUMN, EMAIL_PROPERTY, EDITUSER_COLUMN, DISABLEUSER_COLUMN);

//        grid.setSelectionMode(SelectionMode.MULTI);
        grid.setEditorEnabled(true);

        setGridValidators(grid);

        grid.getColumn(RESETPASSW_COLUMN).setRenderer(new ButtonRenderer((event) -> {
            grid.select(event.getItemId());
            UI.getCurrent().addWindow(new ResetPasswordWindow(baseUserContainer, event.getItemId()));
        }));

        grid.getColumn(EDITUSER_COLUMN).setRenderer(new ButtonRenderer((ClickableRenderer.RendererClickEvent event) -> {
            grid.editItem(event.getItemId());
        }));

        grid.getColumn(DISABLEUSER_COLUMN).setRenderer(new ButtonRenderer((ClickableRenderer.RendererClickEvent event) -> {
            User u = VaadinSession.getCurrent().getAttribute(User.class);
            if (event.getItemId() instanceof User) {
                User selectedUser = (User) event.getItemId();
                if (selectedUser.isDisabled()) {
                    selectedUser.setDisabled(false);
                    baseUserContainer.updateItem(selectedUser);
                } else if (u.equals(selectedUser)) {
                    Notification.show("You can't disable your own User!", Notification.Type.ERROR_MESSAGE);
                } else {
                    ConfirmDialog.show(UI.getCurrent(), "Do you want to disable this User?", (ConfirmDialog cd) -> {
                        if (cd.isConfirmed()) {
//                        grid.getContainerDataSource().removeItem(event.getItemId());

                            selectedUser.setDisabled(true);
                            baseUserContainer.updateItem(selectedUser);
                        }
                    });
                }
            }
        }));

        Button newUserButton = new Button("New User", (Button.ClickEvent event) -> {
            UI.getCurrent().addWindow(new NewUserWindow(baseUserContainer));
        });
        newUserButton.setSizeFull();

        grid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                baseUserContainer.updateItem((User) grid.getEditedItemId());
            }
        });
//        Button delSelectedButton = new Button("Delete Selected");
//        delSelectedButton.addClickListener((Button.ClickEvent event) -> {
//            User u = VaadinSession.getCurrent().getAttribute(User.class);
//            MultiSelectionModel selection = (MultiSelectionModel) grid.getSelectionModel();
//
//            for (Object itemId : selection.getSelectedRows()) {
//                if (u.equals(grid.getContainerDataSource().getItem(itemId))) {
//                    throw new Validator.InvalidValueException("You can't delete your own User!");
//                }
//            }
//
//            ConfirmDialog.show(UI.getCurrent(), "Do you want to delete these Users?", (ConfirmDialog cd) -> {
//                if (cd.isConfirmed()) {
//                    // Delete all selected data items
//                    for (Object itemId : selection.getSelectedRows()) {
//                        grid.getContainerDataSource().removeItem(itemId);
//                    }
//
//                    // Otherwise out of sync with container
//                    grid.getSelectionModel().reset();
//
//                    // Disable after deleting
//                    delSelectedButton.setEnabled(false);
//                }
//            });
//        });
//        delSelectedButton.setEnabled(grid.getSelectedRows().size() > 0);
//
//        grid.addSelectionListener((SelectionEvent event) -> {
//            // Allow deleting only if there's any selected
//            delSelectedButton.setEnabled(grid.getSelectedRows().size() > 0);
//        });
//        head.setSizeUndefined(); 
//        delSelectedButton.setSizeFull();
        grid.appendFooterRow();
//        grid.getFooterRow(0).join(USERNAME_PROPERTY, NAME_PROPERTY, RESETPASSW_COLUMN).setComponent(newUserButton);
//        grid.getFooterRow(0).join(EMAIL_PROPERTY, EDITUSER_COLUMN, DISABLEUSER_COLUMN);

//        grid.getFooterRow(0).join(PHONENR_PROPERTY, EDITUSER_COLUMN, DELETEUSER_COLUMN).setComponent(delSelectedButton);

        grid.setSizeFull();

//        innerLayout.addComponent(head);
        innerLayout.addComponent(grid);

        innerLayout.setSizeFull();

        super.addComponent(innerLayout);

        super.setComponentAlignment(innerLayout, Alignment.MIDDLE_CENTER);
    }

    @Override

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        try {
            if (PermissionService.isAdmin()) {

            } else {
                ((SMS_Main) UI.getCurrent()).navigateTo(OverviewView.VIEW_NAME);
            }
        } catch (NoUserLoggedInException ex) {
            //redirect not logged in Users to the Login-Page
            ((SMS_Main) UI.getCurrent()).navigateTo(LoginView.VIEW_NAME);
        }

        ((SMS_Main) UI.getCurrent()).getMenuBarComponent().switchStyle();
    }

    /**
     * Wrapps the baseContainer for {@link User Users} in a
     * {@link GeneratedPropertyContainer} to add the Buttons in the Grid.
     *
     * @return Container with the Button-Properties
     */
    private Container.Indexed addGeneratedProperties(Container.Indexed baseUserContainer) {

        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(baseUserContainer);

        gpc.addGeneratedProperty(RESETPASSW_COLUMN, new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return "Reset Password";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

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

        gpc.addGeneratedProperty(DISABLEUSER_COLUMN, new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                if ((Boolean) item.getItemProperty(DISABLED_PROPERTY).getValue()) {
                    return "Enable User";
                } else {
                    return "Disable User";
                }
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }

            @Override
            public SortOrder[] getSortProperties(SortOrder order) {
                return new SortOrder[]{new SortOrder(DISABLED_PROPERTY, order.getDirection())};
            }

        });

        return gpc;
    }

    private static void setGridValidators(Grid grid) {
        Field textField = new TextField();
        textField.addValidator(new StringLengthValidator("The Username is too short!", 3, null, false));
        textField.addValidator((Object username) -> {

            Collection<User> users = (Collection<User>) grid.getContainerDataSource().getItemIds();
            if (users.stream().filter((user) -> {
                return !user.equals(grid.getEditedItemId());
            }).anyMatch((u) -> {
                return username.equals(u.getUsername());
            })) {
                throw new Validator.InvalidValueException("Username already in use!");
            }
        });

        grid.getColumn(USERNAME_PROPERTY).setEditorField(textField);

        textField = new TextField();
        textField.addValidator(new StringLengthValidator("The Name is too short!", 2, null, false));
        grid.getColumn(NAME_PROPERTY).setEditorField(textField);

        textField = new TextField();
        textField.setRequired(true);
        textField.setRequiredError("an Email is required!");
        textField.addValidator(new EmailValidator("The Email is invalid!"));
        grid.getColumn(EMAIL_PROPERTY).setEditorField(textField);
    }

    /**
     * PropertyId constant for {@link Container}
     */
    public static final String USERNR_PROPERTY = "id";
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
    public static final String DISABLED_PROPERTY = "disabled";
    /**
     * PropertyId constant for {@link Container}
     */
    public static final String RESETPASSW_COLUMN = "resetPassword";
    /**
     * PropertyId constant for {@link Container}
     */
    public static final String EDITUSER_COLUMN = "editUser";
    /**
     * PropertyId constant for {@link Container}
     */
    public static final String DISABLEUSER_COLUMN = "disabledUser";

    private DaoDelegatingContainer<User> baseUserContainer;

}
