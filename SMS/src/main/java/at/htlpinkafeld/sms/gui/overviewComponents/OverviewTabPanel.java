/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.gui.overviewComponents;

import at.htlpinkafeld.sms.gui.OverviewView;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

/**
 * Interface which is implemented by all
 * {@link Panel Panels}/{@link TabSheet.Tab Tabs} of {@link OverviewView}
 *
 * @author Martin Six
 */
public interface OverviewTabPanel {

    /**
     * Recreates the main Content of the Tab
     */
    public void refreshLayout();

}
