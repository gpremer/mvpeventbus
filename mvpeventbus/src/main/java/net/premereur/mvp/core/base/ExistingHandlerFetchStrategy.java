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
class ExistingHandlerFetchStrategy extends HandlerFetchStrategy {

    public ExistingHandlerFetchStrategy(final EventHandlerManager presenterFactory, final EventMethodMapper methodMapper) {
        super(presenterFactory, methodMapper);
    }

    @Override
    public List<EventHandler> getHandlers(final Class<?> handlerClass, final EventBus bus) {
        return getPresenterFactory().getExistingEventHandlers(handlerClass, bus);
    }

    @Override
    public Iterable<HandlerMethodPair> getHandlerMethodPairs(final Method method) {
        return getMethodMapper().getHandlerEvents(method, Policy.TO_EXISTING_INSTANCES);
    }

}
