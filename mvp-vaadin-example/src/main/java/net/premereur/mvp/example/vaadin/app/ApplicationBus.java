package net.premereur.mvp.example.vaadin.app;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;

import com.vaadin.Application;

/**
 * Event bus segment that routes major, application-level events.
 * 
 * @author gpremer
 */
public interface ApplicationBus extends EventBus {

    /**
     * The applications is initialising.
     * @param app the current application instance.
     */
    @Event(ApplicationPresenter.class)
    void init(Application app);

    /**
     * The applications is closing.
     * @param app the current application instance.
     */
    @Event(ApplicationPresenter.class)
    void close(Application app);

    /**
     * Category management is selected.
     */
    @Event(ApplicationPresenter.class)
    void selectCategoryMgt();

}
