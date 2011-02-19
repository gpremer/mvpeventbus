package net.premereur.mvp.core.base;

import java.util.List;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;

/**
 * Interface for classes that know how to create {@link Presenter}s.
 * 
 * @author gpremer
 * 
 */
public interface PresenterFactory {

    /**
     * Returns a concrete presenter of the given type for use with the given event bus. If this method is called twice for the same handlerClass/eventbus
     * combination, the same presenter instance should be returned (until the presenter is detached or an additional is created).
     * 
     * @param handlerClass the class of the presenter to return.
     * @param eventBus the event bus instance the presenter is associated with.
     * @return a collection of {@link Presenter}s handling the event.
     */
    List<Presenter<?, ?>> getPresenters(Class<?> handlerClass, EventBus eventBus);

    /**
     * Returns a new instance of the specified handler.
     * 
     * @param handlerClass the class of the presenter to return.
     * @param eventBus the event bus instance the presenter is associated with.
     * @return a {@link Presenter}.
     */
    Presenter<?, ?> getNewPresenter(Class<?> handlerClass, EventBus eventBus);

    /**
     * Releases a previously returned presenter from the event bus.
     * 
     * @param presenter the presenter to release
     * @param eventBus the event bus from which the presenter should be released.
     */
    void detachPresenter(Presenter<?, ?> presenter, EventBus eventBus);

}
