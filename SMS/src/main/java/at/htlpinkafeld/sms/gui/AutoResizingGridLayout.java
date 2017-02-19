/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui;

import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UI;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link GridLayout} which uses a BrowserWindowResizeListener to automatically
 * change the column-count if the BrowserWindow is resized.<p>
 * The BrowserWindowResizeListener is automatically registered in
 * {@link #attach()} and removed in {@link #detach() }</p>
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
     * @param componentWidth the width of the components
     */
    public AutoResizingGridLayout(int componentWidth) {
        COMPONENT_WIDTH = componentWidth;
        //TODO Margin und Spacing in die Berechnung einbringen
        super.setColumns((UI.getCurrent().getPage().getBrowserWindowWidth() - 37 - 10) / (COMPONENT_WIDTH + 12));
        super.setMargin(true);
        super.setSpacing(true);
        super.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

    }

    /**
     * {@inheritDoc }
     * <p>
     * automatically reconstructs the {@link GridLayout} with a different column
     * count, calculated with the window-width and the component-size
     * </p>
     *
     * @see Page.BrowserWindowResizeListener
     */
    @Override
    public void browserWindowResized(Page.BrowserWindowResizeEvent event) {
        int newColCount = (event.getWidth() - 37 - 10) / (COMPONENT_WIDTH + 12);
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

    /**
     * {@inheritDoc }
     * <p>
     * additionaly removes itself as an {@link Page.BrowserWindowResizeListener}
     * from the current UI</p>
     */
    @Override
    public void detach() {
        UI.getCurrent().getPage().removeBrowserWindowResizeListener(this);
        super.detach();
    }

    /**
     * {@inheritDoc }
     * <p>
     * additionaly adds itself as an {@link Page.BrowserWindowResizeListener} to
     * the current UI</p>
     */
    @Override
    public void attach() {
        super.attach();
        UI.getCurrent().getPage().addBrowserWindowResizeListener(this);
    }

}
