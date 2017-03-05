/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.gui.container.ContainerFactory;
import at.htlpinkafeld.sms.pojos.Comment;
import at.htlpinkafeld.sms.pojos.Host;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * {@link Window} which displays the data of a single {@link Host} and its
 * {@link Comment Comments} in detail.
 *
 * @author Martin Six
 */
//TODO Synchronisation über Map.Entry wie bei HostPanel?
public class HostDetailWindow extends Window {

    private final Host host;

    /**
     * Constructor for the {@link HostDetailWindow}
     *
     * @param host contains the data which will be shown
     */
    public HostDetailWindow(Host host) {
        super(host.getHostname());
        super.center();
        super.setModal(true);
        this.host = host;

        FormLayout leftDataLayout = new FormLayout();

        //TODO Close Window by clicking outside(Clicklistener in Parent)
        //add Layout Components
        TextField hostnameText = new TextField("Hostname", host.getHostname());
        hostnameText.setReadOnly(true);
        leftDataLayout.addComponent(hostnameText);

        TextField statusText = new TextField("Status", host.getStatus().name());
        statusText.setReadOnly(true);
        leftDataLayout.addComponent(statusText);

        TextArea hostInformationText = new TextArea("Information", host.getInformation());
        hostInformationText.setReadOnly(true);
        leftDataLayout.addComponent(hostInformationText);

        TextField lastCheckedText = new TextField("Last checked", host.getLastChecked().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        lastCheckedText.setReadOnly(true);
        leftDataLayout.addComponent(lastCheckedText);

        TextField durationText = new TextField("Duration", HostPanel.getDurationString(host.getDuration()));
        durationText.setReadOnly(true);
        leftDataLayout.addComponent(durationText);

        leftDataLayout.setSizeFull();
        leftDataLayout.setMargin(true);

        VerticalLayout rightCommentLayout = new VerticalLayout();

        Grid commentGrid = new Grid(ContainerFactory.createHostCommentContainer(host.getHostname()));
        commentGrid.setEditorEnabled(true);
        commentGrid.getColumn("author").setEditable(false);
        commentGrid.getColumn("entryTime").setEditable(false);
        commentGrid.setSizeFull();

        rightCommentLayout.addComponent(commentGrid);

        Button addCommentButton = new Button("add Comment", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Comment newComment = new Comment(LocalDateTime.now(), "Current User", "");
                ((BeanItemContainer<Comment>) commentGrid.getContainerDataSource()).addBean(newComment);
                commentGrid.editItem(newComment);
                commentGrid.focus();
            }
        });

        Button removeCommentButton = new Button("delete Comment", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                commentGrid.getContainerDataSource().removeItem(commentGrid.getSelectedRow());
            }
        });
        Label spaceHolder = new Label("");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(addCommentButton);
        buttonLayout.addComponent(spaceHolder);
        buttonLayout.addComponent(removeCommentButton);
        buttonLayout.setWidth(94, Unit.PERCENTAGE);

        rightCommentLayout.addComponent(buttonLayout);

        HorizontalSplitPanel parentSplitPanel = new HorizontalSplitPanel(leftDataLayout, rightCommentLayout);
        parentSplitPanel.setSizeUndefined();
        parentSplitPanel.setWidth(860, Unit.PIXELS);

        super.setContent(parentSplitPanel);
    }

}
