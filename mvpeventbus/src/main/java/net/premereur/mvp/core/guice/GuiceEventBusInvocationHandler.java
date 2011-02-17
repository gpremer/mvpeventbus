package net.premereur.mvp.core.guice;

import java.util.Collection;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.AbstractEventBusInvocationHandler;
import net.premereur.mvp.core.base.EventInterceptor;
import net.premereur.mvp.core.base.PresenterFactory;

/**
 * Invocation handler for guice-supported event busses.
 * 
 * @author gpremer
 * 
 */
public final class GuiceEventBusInvocationHandler extends AbstractEventBusInvocationHandler {

    private static final GuiceEventBusVerifier GUICE_EVENT_BUS_VERIFIER = new GuiceEventBusVerifier();

    /**
     * Constructor.
     * 
     * @param eventBusClasses the classes to proxy
     * @param presenterFactory the factory to create presenter instances with
     * @param interceptors The interceptors to call before the event dispatch
     */
    public GuiceEventBusInvocationHandler(final Class<? extends EventBus>[] eventBusClasses, final PresenterFactory presenterFactory,
            final Collection<EventInterceptor> interceptors) {
        super(eventBusClasses, presenterFactory, GUICE_EVENT_BUS_VERIFIER, interceptors);
    }

    @Override
    protected void prepareEventBusForCalling(final EventBus eventBus) {
        EventBusModule.setThreadEventBus(eventBus); // so that Guice can inject the proper event bus
    }

}
