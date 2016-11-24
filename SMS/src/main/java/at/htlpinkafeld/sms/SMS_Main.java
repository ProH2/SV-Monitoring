package at.htlpinkafeld.sms;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
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
public class SMS_Main extends UI {

    private Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        navigator = new Navigator(UI.getCurrent(), this);

        navigator.addView(LoginView.VIEW_NAME, LoginView.class);
        navigator.addView(MainView.VIEW_NAME, MainView.class);

        navigator.navigateTo(LoginView.VIEW_NAME);

    }

    public void navigateTo(String navigationState) {
        navigator.navigateTo(navigationState);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = SMS_Main.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
