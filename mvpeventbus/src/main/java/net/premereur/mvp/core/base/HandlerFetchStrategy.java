package net.premereur.mvp.core.base;

import java.lang.reflect.Method;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.core.base.EventMethodMapper.HandlerMethodPair;

/**
 * Utility class to exploit the similarity between dispatching normal event and handling create events.
 * 
 * @author gpremer
 * 
 */
abstract class HandlerFetchStrategy {

    private EventHandlerManager presenterFactory;

    private EventMethodMapper methodMapper;

    abstract Iterable<HandlerMethodPair> getHandlerMethodPairs(final Method method);

    abstract Iterable<EventHandler> getHandlers(Class<?> handlerClass, EventBus bus);

    public HandlerFetchStrategy(final EventHandlerManager presenterFactory, final EventMethodMapper methodMapper) {
        this.presenterFactory = presenterFactory;
        this.methodMapper = methodMapper;
    }

    public EventMethodMapper getMethodMapper() {
        return methodMapper;
    }

    public EventHandlerManager getPresenterFactory() {
        return presenterFactory;
    }

}
