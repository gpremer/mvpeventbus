package net.premereur.mvp.example.vaadin.categorymgt;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.vaadin.app.ApplicationWindow;

/**
 * Event bus for events coming out from category management.
 * 
 * @author gpremer
 * 
 */
public interface CategoryMgtBus extends EventBus {

    @Event(CategoryMgtPresenter.class)
    void activate(ApplicationWindow window);
}
