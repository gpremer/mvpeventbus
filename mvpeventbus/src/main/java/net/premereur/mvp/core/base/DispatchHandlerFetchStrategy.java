package net.premereur.mvp.core.base;

import java.lang.reflect.Method;
import java.util.List;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.base.EventMethodMapper.HandlerMethodPair;

/**
 * Fetches the handler mappings for normal, dispatch event handlers.
 * 
 * @author gpremer
 * 
 */
class DispatchHandlerFetchStrategy extends HandlerFetchStrategy {

    public DispatchHandlerFetchStrategy(final PresenterFactory presenterFactory, final EventMethodMapper methodMapper) {
        super(presenterFactory, methodMapper);
    }

    @Override
    public List<Presenter<?, ?>> getHandlers(final Class<?> handlerClass, final EventBus bus) {
        return getPresenterFactory().getPresenters(handlerClass, bus);
    }

    @Override
    public Iterable<HandlerMethodPair> getHandlerMethodPairs(final Method method) {
        return getMethodMapper().getDispatchHandlerEvents(method);
    }

}
