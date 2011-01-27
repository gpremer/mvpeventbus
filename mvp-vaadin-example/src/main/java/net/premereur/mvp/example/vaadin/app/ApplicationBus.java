package net.premereur.mvp.example.vaadin.app;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.vaadin.common.URLContext;

import com.vaadin.Application;
import com.vaadin.ui.ComponentContainer;

/**
 * Event bus segment that routes major, application-level events.
 * 
 * @author gpremer
 */
public interface ApplicationBus extends EventBus {

    /**
     * The applications is initialising.
     * 
     * @param app the current application instance.
     */
    @Event(ApplicationPresenter.class)
    void init(Application app);

    /**
     * The applications is closing.
     * 
     * @param app the current application instance.
     */
    @Event(ApplicationPresenter.class)
    void close(Application app);

    /**
     * Category management is selected.
     */
    @URLContext("categories")
    @Event(ApplicationPresenter.class)
    void selectCategoryMgt();

    /**
     * Shows a message in the center of the screen.
     * 
     * @param message the message to show
     */
    @Event(ApplicationPresenter.class)
    void showMessage(final String message);

    /**
     * Replaces (or sets the first time called) the central pane of the application window.
     * 
     * @param view the component to display
     */
    @Event(ApplicationPresenter.class)
    void setWorkPane(ComponentContainer view);
    
    /**
     * Sets the URL shown in the browser window.
     * @param fragment the part of the URL after #
     */
    @Event(ApplicationPresenter.class)
    void setURIFragment(String fragment);

}
