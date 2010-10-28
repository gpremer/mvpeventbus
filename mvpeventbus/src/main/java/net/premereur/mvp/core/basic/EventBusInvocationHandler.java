/**
 * 
 */
package net.premereur.mvp.core.basic;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.AbstractEventBusInvocationHandler;

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
     */
    public EventBusInvocationHandler(final Class<? extends EventBus>[] eventBusClasses) {
        super(eventBusClasses, new PresenterFactory(), VERIFIER);
    }

}
