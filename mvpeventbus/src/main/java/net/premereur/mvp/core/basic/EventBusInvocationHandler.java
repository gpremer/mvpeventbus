/**
 * 
 */
package net.premereur.mvp.core.basic;

import java.util.Collection;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.AbstractEventBusInvocationHandler;
import net.premereur.mvp.core.base.EventInterceptor;

/**
 * Invocation handler for basic event busses.
 * 
 * @author gpremer
 * 
 */
public class EventBusInvocationHandler extends AbstractEventBusInvocationHandler {

    private static final EventBusVerifier VERIFIER = new EventBusVerifier();

    /**
     * Constructor.
     * 
     * @param eventBusClasses the classes to proxy
     * @param interceptors the bus-level interceptors
     */
    public EventBusInvocationHandler(final Class<? extends EventBus>[] eventBusClasses, final Collection<EventInterceptor> interceptors) {
        super(eventBusClasses, new HandlerAndPresenterFactory(), VERIFIER, interceptors);
    }

}
