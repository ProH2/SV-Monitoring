/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.gui.OverviewView;
import at.htlpinkafeld.sms.pojos.Hostgroup;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel which represents the {@link Hostgroup Hostgroups}. It is used as a
 * {@link TabSheet.Tab} in {@link OverviewView}
 *
 * @author Martin Six
 */
public class HostGroupOverviewTabPanel extends Panel implements OverviewTabPanel {

    private AutoResizingGridLayout gridLayout = null;
    private BeanItemContainer container;

    /**
     * Constant for the width of the CustomComponent which contains the Data
     * from the Servicepoints. It is used to calculate the amount of columns on
     * the page
     *
     */
    public static final int COMPONENT_WIDTH = 300;

    /**
     * Constructor for the {@link HostGroupOverviewTabPanel}
     *
     * @param container which contains the {@link Hostgroup Hostgroups}
     */
    public HostGroupOverviewTabPanel(BeanItemContainer container) {
        this.container = container;

        //Todo default sorting
        this.container.addItemSetChangeListener(new Container.ItemSetChangeListener() {
            @Override
            public void containerItemSetChange(Container.ItemSetChangeEvent event) {
                refreshLayout();
            }
        });

        VerticalLayout parentVerticalLayout = new VerticalLayout();

        //create Filtermapping
        Map<String, String> filterMap = new HashMap<>();
        filterMap.put("Host Gruppe", "name");

        SearchComponent searchComponent = new SearchComponent(filterMap, container);
        parentVerticalLayout.addComponent(searchComponent);
        parentVerticalLayout.setComponentAlignment(searchComponent, Alignment.MIDDLE_RIGHT);

        this.gridLayout = new AutoResizingGridLayout(COMPONENT_WIDTH);
        parentVerticalLayout.addComponent(gridLayout);

        super.setContent(parentVerticalLayout);

    }

    @Override
    final public void refreshLayout() {
        Collection itemList = container.getItemIds();
        if (gridLayout != null) {
            gridLayout.removeAllComponents();

            for (Object o : itemList) {
                //TODO create Panel for Host Group
//                Panel compPanel = new HostPanel((Map.Entry<String, Host>) o);

//                compPanel.addClickListener(new MouseEvents.ClickListener() {
//                    @Override
//                    public void click(MouseEvents.ClickEvent event) {
//                        UI.getCurrent().addWindow(new HostDetailWindow((Host) o));
//                    }
//                });
//
//                compPanel.setWidth(COMPONENT_WIDTH, Unit.PIXELS);
//                gridLayout.addComponent(compPanel);
            }

            gridLayout.markAsDirty();
        }
    }

}
