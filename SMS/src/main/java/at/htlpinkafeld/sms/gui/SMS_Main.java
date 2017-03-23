package at.htlpinkafeld.sms.gui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
@Title("SVISS SMS")
public class SMS_Main extends UI {

    private Navigator navigator;

    private MenuBarComponent mbc;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        mbc = new MenuBarComponent();

        navigator = new Navigator(UI.getCurrent(), this);

        OverviewView overviewView = new OverviewView();
//
        navigator.addView(OverviewView.VIEW_NAME, overviewView);
//        navigator.addView(OverviewView.VIEW_NAME, OverviewView.class);

        navigator.addView(LoginView.VIEW_NAME, LoginView.class);
        navigator.addView(UserManagementView.VIEW_NAME, UserManagementView.class);
        navigator.addView(TimeManagementView.VIEW_NAME, TimeManagementView.class);
        navigator.addView(Host_Service_ManagementView.VIEW_NAME, Host_Service_ManagementView.class);
        navigator.addView(LogView.VIEW_NAME, LogView.class);

        setPollInterval(5000);//Poll every 5 seconds
        setResizeLazy(true);

    }

    /**
     * Function used for the Navigation between the different Views which are
     * registered in the init above. The constant VIEW_NAME which is passed
     * should be defined in every View
     *
     * @param viewName the name of the View to which should be navigated
     */
    public void navigateTo(String viewName) {
        navigator.navigateTo(viewName);
    }

    /**
     * Getter for the {@link MenuBarComponent}
     *
     * @return The {@link MenuBarComponent} which is used in the current UI.
     */
    public MenuBarComponent getMenuBarComponent() {
        return mbc;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = SMS_Main.class, productionMode = false, resourceCacheTime = 10, widgetset = "at.htlpinkafeld.sms.gui.AppWidgetSet")
    public static class MyUIServlet extends VaadinServlet {

    }
}
