package net.premereur.mvp.core.base;

import java.lang.reflect.Method;

import net.premereur.mvp.core.EventBus;

/**
 * EventInterceptors (that are configured in an EventBusFactory) are called for every event sent to the event bus. This creates the opportunity to define
 * cross-cutting behaviour only once.
 * 
 * The invocation receives the event method itself, not the event handler. I.e. if an event is processed by multiple handlers, it will still be intercepted only
 * once.
 * 
 * Note that it is possible to use custom annotations on your event bus methods to distinguish between methods and to pass additional input to your interceptor.
 * 
 * Also note that Guice enabled busses can use aop alliance-based interception, but that is only applicable to objects that are created by Guice. Which is not
 * the case for event bus and presenter instances since they are created by the mvpeventbus framework.
 * 
 * @author gpremer
 * 
 */
public interface EventInterceptor {

    /**
     * Clearer way of indicating that execution should proceed.
     */
    boolean PROCEED = true;
    /**
     * Clearer way of indicating that execution should not proceed.
     */
    boolean HALT = false;

    /**
     * Called before an intercepted is processed.
     * 
     * @param eventBus the event bus to which the event was dispatched and that the interceptor can use to send other events
     * @param eventMethod the event about to be processed
     * @param args the arguments given to the event
     * @return true if the event should be further processed, false if not
     */
    boolean beforeEvent(final EventBus eventBus, final Method eventMethod, final Object[] args);
}
