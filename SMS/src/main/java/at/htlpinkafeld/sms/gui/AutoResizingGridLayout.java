/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UI;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link GridLayout} which uses a BrowserWindowResizeListener to automatically
 * change the column-count if the BrowserWindow is resized
 *
 * @author Martin Six
 */
public class AutoResizingGridLayout extends GridLayout implements Page.BrowserWindowResizeListener {

    private final int COMPONENT_WIDTH;

    /**
     * The same as calling AutoResizingGridLayout(300)
     */
    public AutoResizingGridLayout() {
        this(300);
    }

    /**
     * Constructor which uses the componentWidth Parameter to calculate the
     * column count
     *
     * @param componentWidth
     */
    public AutoResizingGridLayout(int componentWidth) {
        COMPONENT_WIDTH = componentWidth;
        //TODO Margin und Spacing in die Berechnung einbringen
        super.setColumns(UI.getCurrent().getPage().getBrowserWindowWidth() / (COMPONENT_WIDTH + 15));
        super.setMargin(true);
        super.setSpacing(true);

    }

    /**
     * BrowserWindowResized Listener which is registered in the Constructor. It
     * reconstructs the GridLayout with a different column count after a
     * {@link  Page.BrowserWindowResizeEvent}, based on the Page-Width
     *
     * @param event
     */
    @Override
    public void browserWindowResized(Page.BrowserWindowResizeEvent event) {
        int newColCount = event.getWidth() / COMPONENT_WIDTH;
        if (newColCount != super.getColumns()) {

            List<Component> componentList = new LinkedList<>();
            for (Component c : this) {
                componentList.add(c);
            }

            super.removeAllComponents();

            super.setColumns(newColCount);
            super.addComponents(componentList.toArray(new Component[0]));
        }
    }

    public int getCOMPONENT_WIDTH() {
        return COMPONENT_WIDTH;
    }

}
