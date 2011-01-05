package net.premereur.mvp.example.vaadin.categorymgt;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.vaadin.app.ApplicationWindow;

/**
 * Event bus for events coming out from category management.
 * 
 * @author gpremer
 * 
 */
public interface CategoryMgtBus extends EventBus {

    /**
     * Event received when category management is activated.
     * 
     * @param window the window in which to display category management.
     */
    @Event(CategoryMgtPresenter.class)
    void activate(ApplicationWindow window);

    /**
     * Event received when a category has been changed.
     * 
     * @param category the changed category
     */
    @Event(CategoryMgtPresenter.class)
    void changedCategory(Category category);
}
