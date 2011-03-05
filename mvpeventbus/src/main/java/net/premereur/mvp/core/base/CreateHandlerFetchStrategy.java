package net.premereur.mvp.core.base;

import java.lang.reflect.Method;
import java.util.Collections;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.core.Event.Policy;
import net.premereur.mvp.core.base.EventMethodMapper.HandlerMethodPair;

/**
 * Fetches the handler mappings for event handlers that create a new presenter on every call and then dispatch the event only to that newly created presenter.
 * 
 * @author gpremer
 * 
 */
class CreateHandlerFetchStrategy implements HandlerFetchStrategy {

    @Override
    public Iterable<EventHandler> getHandlers(final EventHandlerManager presenterMgr, final Class<?> handlerClass, final EventBus bus) {
        return Collections.singleton(presenterMgr.getNewEventHandler(handlerClass, bus));
    }

    @Override
    public Iterable<HandlerMethodPair> getHandlerMethodPairs(final EventMethodMapper methodMapper, final Method method) {
        return methodMapper.getHandlerEvents(method, Policy.TO_NEW_INSTANCE);
    }

}
