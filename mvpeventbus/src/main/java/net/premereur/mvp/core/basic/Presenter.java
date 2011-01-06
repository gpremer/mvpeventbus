package net.premereur.mvp.core.basic;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.View;

/**
 * The interface for all presenters using the basic event bus factory. These presenters don't support dependency injection and thus have to provide explicit
 * setters for the view and event bus dependencies.
 * 
 * @author gpremer
 * 
 * @param <V> The {@link View} the presenter manages
 * @param <E> The main {@link EventBus} segment type
 */

public interface Presenter<V extends View, E extends EventBus> extends net.premereur.mvp.core.Presenter<V, E> {
    /**
     * Called by an event bus when creating a presenter to inject the associated view.
     * 
     * @param view The view for the presenter.
     */
    void setView(V view);

    /**
     * Called by an event bus when creating a presenter to inject itself.
     * 
     * @param eventBus The bus on which to send events.
     */
    void setEventBus(E eventBus);

}
