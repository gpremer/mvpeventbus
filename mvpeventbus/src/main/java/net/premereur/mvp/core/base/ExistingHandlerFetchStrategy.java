package net.premereur.mvp.core.base;

import java.lang.reflect.Method;
import java.util.List;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.core.Event.Instantiation;
import net.premereur.mvp.core.base.EventMethodMapper.HandlerMethodPair;

/**
 * Fetches the handler mappings for normal, dispatch event handlers.
 * 
 * @author gpremer
 * 
 */
class ExistingHandlerFetchStrategy implements HandlerFetchStrategy {

    @Override
    public List<EventHandler> getHandlers(final EventHandlerManager presenterMgr, final Class<?> handlerClass, final EventBus bus) {
        return presenterMgr.getExistingEventHandlers(handlerClass, bus);
    }

    @Override
    public Iterable<HandlerMethodPair> getHandlerMethodPairs(final EventMethodMapper methodMapper, final Method method) {
        return methodMapper.getHandlerEvents(method, Instantiation.TO_EXISTING_INSTANCES);
    }

}
