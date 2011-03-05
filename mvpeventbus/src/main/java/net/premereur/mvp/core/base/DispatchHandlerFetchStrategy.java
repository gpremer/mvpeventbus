package net.premereur.mvp.core.base;

import java.lang.reflect.Method;
import java.util.List;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.core.Event.Policy;
import net.premereur.mvp.core.base.EventMethodMapper.HandlerMethodPair;

/**
 * Fetches the handler mappings for normal, dispatch event handlers.
 * 
 * @author gpremer
 * 
 */
class DispatchHandlerFetchStrategy implements HandlerFetchStrategy {

    @Override
    public List<EventHandler> getHandlers(final EventHandlerManager presenterMgr, final Class<?> handlerClass, final EventBus bus) {
        return presenterMgr.getEventHandlers(handlerClass, bus);
    }

    @Override
    public Iterable<HandlerMethodPair> getHandlerMethodPairs(final EventMethodMapper methodMapper, final Method method) {
        return methodMapper.getHandlerEvents(method, Policy.TO_INSTANCES);
    }

}
