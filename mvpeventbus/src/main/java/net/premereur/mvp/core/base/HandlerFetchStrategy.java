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
interface HandlerFetchStrategy {

    Iterable<HandlerMethodPair> getHandlerMethodPairs(EventMethodMapper methodMapper2, final Method method);

    Iterable<EventHandler> getHandlers(EventHandlerManager presenterMgr, Class<?> handlerClass, EventBus bus);

}
