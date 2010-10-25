package net.premereur.mvp.core.guice;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.AbstractEventBusInvocationHandler;
import net.premereur.mvp.core.basic.PresenterFactory;

/**
 * Invocation handler for guice-supported event busses.
 * 
 * @author gpremer
 * 
 */
public class GuiceEventBusInvocationHandler extends AbstractEventBusInvocationHandler {

    private static final GuiceEventBusVerifier GUICE_EVENT_BUS_VERIFIER = new GuiceEventBusVerifier();

    /**
     * Constructor.
     * 
     * @param eventBusClasses the classes to proxy
     * @param presenterFactory the factory to create presenter instances with
     */
    public GuiceEventBusInvocationHandler(Class<? extends EventBus>[] eventBusClasses, PresenterFactory presenterFactory) {
        super(eventBusClasses, presenterFactory, GUICE_EVENT_BUS_VERIFIER);
    }

}
