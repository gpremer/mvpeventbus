package net.premereur.mvp.core.base;

import java.util.List;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;

/**
 * Interface for classes that know how to manage {@link EventHandler}s.
 * 
 * @author gpremer
 * 
 */
public interface EventHandlerManager {

    /**
     * Returns a concrete handler of the given type for use with the given event bus. If no handler of the requested type exists yet, create a new one. If this
     * method is called twice for the same handlerClass/eventbus combination, the same handler instance should be returned (until the handler is detached or an
     * additional is created).
     * 
     * @param handlerClass the class of the handler to return.
     * @param eventBus the event bus instance the handler is associated with.
     * @return a collection of {@link EventHandler}s handling the event.
     */
    List<EventHandler> getEventHandlers(Class<?> handlerClass, EventBus eventBus);

    /**
     * Returns a new instance of the specified handler.
     * 
     * @param handlerClass the class of the handler to return.
     * @param eventBus the event bus instance the handler is associated with.
     * @return an {@link EventHandler}.
     */
    EventHandler getNewEventHandler(Class<?> handlerClass, EventBus eventBus);

    /**
     * Detaches a previously returned handler from the event bus.
     * 
     * @param eventHandler the handler to release
     * @param eventBus the event bus from which the handler should be released.
     */
    void detachPresenter(EventHandler eventHandler, EventBus eventBus);

    /**
     * Attaches a handler created by the caller to the event bus.
     * 
     * @param eventHandler the handler to associate
     * @param eventBus the event bus to which the handler should be attached.
     */
    void attachPresenter(EventHandler eventHandler, EventBus eventBus);

    /**
     * Returns previously returned event handlers from the event bus. Does not create a new handler if there was none already.
     * 
     * @param handlerClass the class of the handler to return.
     * @param eventBus the event bus instance the handler is associated with.
     * @return a collection of {@link EventHandler}s handling the event.
     */
    List<EventHandler> getExistingEventHandlers(Class<?> handlerClass, EventBus eventBus);

}
