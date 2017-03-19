/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.window;

import at.htlpinkafeld.sms.gui.LoginView;
import at.htlpinkafeld.sms.gui.SMS_Main;
import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.gui.container.HashMapWithListeners;
import at.htlpinkafeld.sms.gui.container.MapReferenceContainer;
import at.htlpinkafeld.sms.gui.overviewComponents.HostPanel;
import at.htlpinkafeld.sms.pojos.Comment;
import at.htlpinkafeld.sms.pojos.Service;
import at.htlpinkafeld.sms.service.NoUserLoggedInException;
import at.htlpinkafeld.sms.service.PermissionService;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
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
 * {@link Window} which displays the data of a single {@link Service} and its
 * {@link Comment Comments} in detail.
 *
 * @author Martin Six
 */
public class ServiceDetailWindow extends Window implements HashMapWithListeners.MapChangeListener {

    private Map.Entry<String, Service> serviceEntry;

    private MapReferenceContainer<Service> container;

    private final TextField servicenameText;
    private final TextField hostnameText;
    private final TextField statusText;
    private final TextArea serviceInformationText;
    private final TextField lastCheckedText;
    private final TextField durationText;

    /**
     * Constructor for the ServiceDetailWindow
     *
     * @param serviceEntry Service-Entry which is wrapped with the window
     * @param container container of the Service-Entry which is used for
     * Synchronisation
     */
    public ServiceDetailWindow(Map.Entry<String, Service> serviceEntry, MapReferenceContainer<Service> container) {
        super(serviceEntry.getKey());
        super.center();
        super.setModal(true);
        this.serviceEntry = serviceEntry;

        Service service = serviceEntry.getValue();

        FormLayout leftDataLayout = new FormLayout();

        //add Layout Components
        servicenameText = new TextField("Servicename", service.getName());
        servicenameText.setReadOnly(true);
        leftDataLayout.addComponent(servicenameText);

        hostnameText = new TextField("Hostname", service.getHostname());
        hostnameText.setReadOnly(true);
        leftDataLayout.addComponent(hostnameText);

        statusText = new TextField("Status", service.getStatus().name());
        statusText.setReadOnly(true);
        leftDataLayout.addComponent(statusText);

        serviceInformationText = new TextArea("Information", service.getInformation());
        serviceInformationText.setReadOnly(true);
        leftDataLayout.addComponent(serviceInformationText);

        lastCheckedText = new TextField("Last checked", service.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        lastCheckedText.setReadOnly(true);
        leftDataLayout.addComponent(lastCheckedText);

        durationText = new TextField("Duration", HostPanel.getDurationString(service.getDuration()));
        durationText.setReadOnly(true);
        leftDataLayout.addComponent(durationText);

        leftDataLayout.setSizeFull();
        leftDataLayout.setMargin(true);

        VerticalLayout rightCommentLayout = new VerticalLayout();
        Grid commentGrid = new Grid(ContainerFactory.createServiceCommentContainer(service.getHostname() + "/" + service.getName()));
        commentGrid.setSizeFull();
        rightCommentLayout.addComponent(commentGrid);

        try {
            if (PermissionService.isAdmin()) {
                commentGrid.setEditorEnabled(true);
                commentGrid.getColumn("author").setEditable(false);
                commentGrid.getColumn("entryTime").setEditable(false);

                Button addCommentButton = new Button("Add Comment", (Button.ClickEvent event) -> {
                    Comment newComment = new Comment(4, "Testcomment", "Testcomment to -* ServiceDetailWindow *-", 1, LocalDateTime.now());
                    ((BeanItemContainer<Comment>) commentGrid.getContainerDataSource()).addBean(newComment);
                    commentGrid.editItem(newComment);
                    commentGrid.focus();
                });

                Button removeCommentButton = new Button("Delete Comment", (Button.ClickEvent event) -> {
                    commentGrid.getContainerDataSource().removeItem(commentGrid.getSelectedRow());
                });

                HorizontalLayout buttonLayout = new HorizontalLayout();
                Label spaceHolder = new Label("");
                buttonLayout.addComponents(addCommentButton, spaceHolder, removeCommentButton);
                buttonLayout.setWidth(94, Unit.PERCENTAGE);

                rightCommentLayout.addComponent(buttonLayout);

            }
        } catch (NoUserLoggedInException ex) {
            ((SMS_Main) UI.getCurrent()).navigateTo(LoginView.VIEW_NAME);
        }

        HorizontalSplitPanel parentSplitPanel = new HorizontalSplitPanel(leftDataLayout, rightCommentLayout);
        parentSplitPanel.setSizeUndefined();
        parentSplitPanel.setWidth(860, Unit.PIXELS);

        super.setContent(parentSplitPanel);
    }

    /**
     * method used to update the textfields when the
     * {@link HashMapWithListeners.MapChangeListener} was notified
     */
    private void updateLabels() {
        Service service = serviceEntry.getValue();
        statusText.setValue(service.getStatus().name());

        hostnameText.setValue(service.getHostname());
        serviceInformationText.setValue(service.getInformation());
        lastCheckedText.setValue(service.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        durationText.setValue(HostPanel.getDurationString(service.getDuration()));
    }

    @Override
    public void mapChanged() {
        this.serviceEntry = ((BeanItem<Map.Entry<String, Service>>) container.getItem(this.serviceEntry.getKey())).getBean();
        updateLabels();
        super.markAsDirty();
    }

}
