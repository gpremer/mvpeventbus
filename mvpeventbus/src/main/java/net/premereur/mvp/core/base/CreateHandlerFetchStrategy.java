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
class CreateHandlerFetchStrategy extends HandlerFetchStrategy {

    public CreateHandlerFetchStrategy(final EventHandlerManager presenterFactory, final EventMethodMapper methodMapper) {
        super(presenterFactory, methodMapper);
    }

    @Override
    public Iterable<EventHandler> getHandlers(final Class<?> handlerClass, final EventBus bus) {
//        List<EventHandler> singleton = new ArrayList<EventHandler>(); // Collections.singleton is not generics-smart enough
//        singleton.add(getPresenterFactory().getNewEventHandler(handlerClass, bus));
//        return singleton;
        return Collections.singleton(getPresenterFactory().getNewEventHandler(handlerClass, bus));
    }

    @Override
    public Iterable<HandlerMethodPair> getHandlerMethodPairs(final Method method) {
        return getMethodMapper().getHandlerEvents(method, Policy.TO_NEW_INSTANCE);
    }

}
