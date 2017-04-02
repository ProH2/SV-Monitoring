/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.window;

import at.htlpinkafeld.sms.gui.LoginView;
import at.htlpinkafeld.sms.gui.SMS_Main;
import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.gui.container.DaoDelegatingContainer;
import at.htlpinkafeld.sms.gui.container.HashMapWithListeners;
import at.htlpinkafeld.sms.gui.container.MapReferenceContainer;
import at.htlpinkafeld.sms.gui.overviewComponents.HostPanel;
import static at.htlpinkafeld.sms.gui.overviewComponents.HostPanel.getDurationString;
import at.htlpinkafeld.sms.pojo.Comment;
import at.htlpinkafeld.sms.pojo.Host;
import at.htlpinkafeld.sms.pojo.User;
import at.htlpinkafeld.sms.service.NoUserLoggedInException;
import at.htlpinkafeld.sms.service.PermissionService;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * {@link Window} which displays the data of a single {@link Host} and its
 * {@link Comment Comments} in detail.
 *
 * @author Martin Six
 */
public class HostDetailWindow extends Window implements HashMapWithListeners.MapChangeListener {

    private Map.Entry<String, Host> hostEntry;
    private final MapReferenceContainer<Host> container;

    private final TextField hostnameText;
    private final TextField statusText;
    private final TextArea hostInformationText;
    private final TextField lastCheckedText;
    private final TextField durationText;

    /**
     * Constructor for the {@link HostDetailWindow}
     *
     * @param hostEntry Host-Entry which is wrapped with the Window
     * @param container container of the Host-Entry which is used for
     * Synchronisation
     */
    public HostDetailWindow(Map.Entry<String, Host> hostEntry, MapReferenceContainer<Host> container) {
        super(hostEntry.getKey());
        super.center();
        super.setModal(true);
        this.hostEntry = hostEntry;
        this.container = container;

        Host host = hostEntry.getValue();

        FormLayout leftDataLayout = new FormLayout();

        //add Layout Components
        hostnameText = new TextField("Hostname", host.getHostname());
        hostnameText.setReadOnly(true);
        leftDataLayout.addComponent(hostnameText);

        statusText = new TextField("Status", host.getStatus().name());
        statusText.setReadOnly(true);
        leftDataLayout.addComponent(statusText);

        hostInformationText = new TextArea("Information", host.getInformation());
        hostInformationText.setReadOnly(true);
        leftDataLayout.addComponent(hostInformationText);

        lastCheckedText = new TextField("Last checked", host.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        lastCheckedText.setReadOnly(true);
        leftDataLayout.addComponent(lastCheckedText);

        durationText = new TextField("Duration", HostPanel.getDurationString(host.getDuration()));
        durationText.setReadOnly(true);
        leftDataLayout.addComponent(durationText);

        leftDataLayout.setSizeFull();
        leftDataLayout.setMargin(true);

        VerticalLayout rightCommentLayout = new VerticalLayout();

        Grid commentGrid = new Grid(ContainerFactory.createHostCommentContainer(host.getHostname()));
        commentGrid.setSizeFull();
        commentGrid.removeAllColumns();
        commentGrid.addColumn("comment");

        rightCommentLayout.addComponent(commentGrid);

        commentGrid.setEditorEnabled(true);

        Button addCommentButton = new Button("Add Comment", (Button.ClickEvent event) -> {
            Comment newComment = new Comment("", host.getHostname(), VaadinSession.getCurrent().getAttribute(User.class).getId(), LocalDateTime.now());
            ((BeanItemContainer<Comment>) commentGrid.getContainerDataSource()).addBean(newComment);
            commentGrid.editItem(newComment);
            commentGrid.setEnabled(true);

            commentGrid.focus();
        });

        commentGrid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                Object item = commentGrid.getEditedItemId();
                if (item instanceof Comment) {
                    ((DaoDelegatingContainer<Comment>) commentGrid.getContainerDataSource()).updateItem((Comment) item);
                    try {
                        if (!PermissionService.isAdmin()) {
                            commentGrid.setEnabled(false);
                        }
                    } catch (NoUserLoggedInException ex) {
                        ((SMS_Main) UI.getCurrent()).navigateTo(LoginView.VIEW_NAME);
                    }
                }
            }
        });

        Button removeCommentButton = new Button("Delete Comment", (Button.ClickEvent event) -> {
            commentGrid.getContainerDataSource().removeItem(commentGrid.getSelectedRow());
        });
        Label spaceHolder = new Label("");

        try {
            if (!PermissionService.isAdmin()) {
                commentGrid.setEnabled(false);
                removeCommentButton.setVisible(false);
            }
        } catch (NoUserLoggedInException ex) {
            ((SMS_Main) UI.getCurrent()).navigateTo(LoginView.VIEW_NAME);
        }

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponents(addCommentButton, spaceHolder, removeCommentButton);
        buttonLayout.setWidth(94, Unit.PERCENTAGE);

        rightCommentLayout.addComponent(buttonLayout);

        HorizontalSplitPanel parentSplitPanel = new HorizontalSplitPanel(leftDataLayout, rightCommentLayout);

        parentSplitPanel.setSizeUndefined();

        parentSplitPanel.setWidth(860, Unit.PIXELS);

        super.setContent(parentSplitPanel);
    }

    /**
     * method used to update the TextFields when the
     * {@link HashMapWithListeners.MapChangeListener} was notified
     */
    private void updateLabels() {
        Host host = hostEntry.getValue();
        statusText.setValue(host.getStatus().name());

        hostInformationText.setValue(host.getInformation());
        lastCheckedText.setValue(host.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        durationText.setValue(getDurationString(host.getDuration()));
    }

    @Override
    public void mapChanged() {
        this.hostEntry = ((BeanItem<Map.Entry<String, Host>>) container.getItem(this.hostEntry.getKey())).getBean();
        updateLabels();
        super.markAsDirty();

    }

}
