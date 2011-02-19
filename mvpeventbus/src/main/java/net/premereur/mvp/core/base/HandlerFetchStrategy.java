package net.premereur.mvp.core.base;

import java.lang.reflect.Method;
import java.util.List;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.base.EventMethodMapper.HandlerMethodPair;

/**
 * Utility class to exploit the similarity between dispatching normal event and handling create events.
 * 
 * @author gpremer
 * 
 */
abstract class HandlerFetchStrategy {

    private PresenterFactory presenterFactory;

    private EventMethodMapper methodMapper;

    abstract Iterable<HandlerMethodPair> getHandlerMethodPairs(final Method method);

    abstract List<Presenter<?, ?>> getHandlers(Class<?> handlerClass, EventBus bus);

    public HandlerFetchStrategy(final PresenterFactory presenterFactory, final EventMethodMapper methodMapper) {
        this.presenterFactory = presenterFactory;
        this.methodMapper = methodMapper;
    }

    public EventMethodMapper getMethodMapper() {
        return methodMapper;
    }

    public PresenterFactory getPresenterFactory() {
        return presenterFactory;
    }

}
