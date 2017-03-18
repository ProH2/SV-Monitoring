/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.gui.OverviewView;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import java.util.Map;

/**
 * {@link CustomComponent} for the SearchComponent in {@link OverviewView}
 *
 * @author Martin Six
 */
public class SearchComponent extends CustomComponent {

    private TextField searchTermField;
    private NativeSelect filterSelect;

    private final Container.Filterable container;

    /**
     * Constructor for SearchComponent
     *
     * @param searchFilterMapping Mapping for shown value in the select and the
     * filtered PropertyId of the container
     * @param container container which is filtered
     */
    public SearchComponent(Map<String, String> searchFilterMapping, Container.Filterable container) {

        this.container = container;

        filterSelect = new NativeSelect();

        for (String filterId : searchFilterMapping.keySet()) {
            filterSelect.addItem(filterId);
        }

        filterSelect.setHeight(37, Unit.PIXELS);
        filterSelect.select(filterSelect.getItemIds().toArray()[0]);
        filterSelect.setNullSelectionAllowed(false);

        filterSelect.addValueChangeListener((Property.ValueChangeEvent event) -> {
            searchTermField.clear();
        });

        searchTermField = new TextField();
        searchTermField.setSizeFull();
//        searchTermField.setImmediate(true);

        Button searchButton = new Button("Search", (Button.ClickEvent event) -> {
            container.removeAllContainerFilters();

            String searchTerm = searchTermField.getValue();
            if (!searchTerm.isEmpty()) {
                String filterType = filterSelect.getValue().toString();

                if (searchFilterMapping.containsKey(filterType)) {
                    Container.Filter f = null;
                    f = new SimpleStringFilter(searchFilterMapping.get(filterSelect.getValue().toString()), searchTerm, true, false);

                    container.addContainerFilter(f);
                } else {
                    Notification.show("Some Error occured during searching", Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        //Automatically apply filter if searchTermField is changed
        searchTermField.addValueChangeListener((Property.ValueChangeEvent event) -> {
            searchButton.click();
            SearchComponent.super.focus();
        });

        HorizontalLayout searchLayout = new HorizontalLayout(filterSelect, searchTermField, searchButton);
        searchLayout.setMargin(true);

        super.setSizeUndefined();
        super.setCompositionRoot(searchLayout);

    }

}
