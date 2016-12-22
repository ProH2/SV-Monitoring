/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.LinkedList;
import java.util.List;

/**
 * View for the Overview of the Servicepoint Data
 *
 * @author Martin Six
 */
public class OverviewView extends VerticalLayout implements View, Page.BrowserWindowResizeListener {

    public static final String VIEW_NAME = "overview";

    GridLayout gridLayout;

    /**
     * Constant for the width of the CustomComponent which contains the Data
     * from the Servicepoints. It is used to calculate the amount of columns on
     * the page
     *
     */
    public static final int COMPONENT_WIDTH = 300;

    public OverviewView() {
        super.addComponent(new MenuBarComponent());

        gridLayout = new GridLayout();
        gridLayout.setColumns(UI.getCurrent().getPage().getBrowserWindowWidth() / COMPONENT_WIDTH);

        gridLayout.setMargin(true);
        gridLayout.setSpacing(true);

        for (int i = 0; i < 30; i++) {
            Label l = new Label("Test Component " + i);
            l.setWidth(COMPONENT_WIDTH, Unit.PIXELS);
            gridLayout.addComponent(new Panel(l));
        }

        UI.getCurrent().getPage().addBrowserWindowResizeListener(this);

        super.addComponent(gridLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    /**
     * BrowserWindowResized Listener which is registered in the Constructor. It
     * reconstructs the GridLayout with a different column count after a
     * {@link  Page.BrowserWindowResizeEvent}, based on the Page-Width
     *
     * @param event
     */
    @Override
    public synchronized void browserWindowResized(Page.BrowserWindowResizeEvent event) {
        int newColCount = event.getWidth() / COMPONENT_WIDTH;
        if (newColCount != gridLayout.getColumns()) {

            List<Component> componentList = new LinkedList<>();
            for (Component c : gridLayout) {
                componentList.add(c);
            }
            GridLayout newGridLayout = new GridLayout(newColCount, 1, componentList.toArray(new Component[0]));

            newGridLayout.setMargin(true);
            newGridLayout.setSpacing(true);

            super.replaceComponent(gridLayout, newGridLayout);
            gridLayout = newGridLayout;
        }

    }

}
