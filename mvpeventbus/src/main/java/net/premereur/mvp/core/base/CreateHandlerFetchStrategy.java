package net.premereur.mvp.core.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.Event.Policy;
import net.premereur.mvp.core.base.EventMethodMapper.HandlerMethodPair;

/**
 * Fetches the handler mappings for event handlers that create a new presenter on every call and then dispatch the event only to that newly created presenter.
 * 
 * @author gpremer
 * 
 */
class CreateHandlerFetchStrategy extends HandlerFetchStrategy {

    public CreateHandlerFetchStrategy(final PresenterFactory presenterFactory, final EventMethodMapper methodMapper) {
        super(presenterFactory, methodMapper);
    }

    @Override
    public List<Presenter<?, ?>> getHandlers(final Class<?> handlerClass, final EventBus bus) {
        List<Presenter<?, ?>> singleton = new ArrayList<Presenter<?, ?>>(); // Collections.singleton is not generics-smart enough
        singleton.add(getPresenterFactory().getNewPresenter(handlerClass, bus));
        return singleton;
    }

    @Override
    public Iterable<HandlerMethodPair> getHandlerMethodPairs(final Method method) {
        return getMethodMapper().getHandlerEvents(method, Policy.TO_NEW_INSTANCE);
    }

}
