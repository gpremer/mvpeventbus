package net.premereur.mvp.core.base;

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
     * combination, the same presenter instance should be returned.
     * 
     * @param handlerClass the class of the presenter to return.
     * @param eventbus the event bus instance the presenter is associated with.
     * @return a {@link Presenter}.
     */
    Presenter<?, ?> getPresenter(Class<?> handlerClass, EventBus eventbus);

}
