package net.premereur.mvp.core.base;

import java.lang.reflect.InvocationHandler;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventBusFactory;

/**
 * A base class factory for event bus factories.
 * 
 * @author gpremer
 * 
 */
public abstract class AbstractEventBusFactory implements EventBusFactory {

    /**
     * Creates an event bus that implements all of the supplied event bus interfaces. The first argument is the called the master event bus. This naming is
     * quite arbitrary however as all event bus segments are created equal. The first one just happens to be the one to which the whole event bus object is
     * casted to.
     * 
     * At run-time it doesn't matter if an application use several event buss segments that are joined with each other, or one large event bus responding to the
     * union of all events in all separate busses. At design-time, however, it is often easier to manage several distinct busses each with their own events and
     * presenters. When there's a need to send events to event sinks on other, combined, busses, the designer has the choice of replicating event methods in the
     * attached bus or calling events directly on the master bus.
     * 
     * @param <E> The main event bus interface type
     * @param mainEventBusIntf the main segment
     * @param segmentIntfs the other, optional, segments
     * @return an event bus implementing all of the segment interfaces
     */
    @SuppressWarnings("unchecked")
    public final <E extends EventBus> E createEventBus(final Class<E> mainEventBusIntf, final Class<?>... segmentIntfs) {
        Class<? extends EventBus>[] eventBusIntfs = (Class<? extends EventBus>[]) new Class<?>[1 + segmentIntfs.length];
        eventBusIntfs[0] = mainEventBusIntf;
        for (int i = 0; i < segmentIntfs.length; ++i) {
            eventBusIntfs[i + 1] = (Class<? extends EventBus>) segmentIntfs[i];
        }
        InvocationHandler handler = createHandler(eventBusIntfs);
        return (E) createProxy(eventBusIntfs, handler);
    }

    /**
     * Should create a dynamic proxy for the given interfaces and handler.
     * 
     * @param eventBusIntfs the interfaces that should be implemented
     * @param handler the handler that should handler methods on the proxy
     * @return a dynamic proxy implementing all of the supplied interfaces
     */
    protected abstract Object createProxy(Class<? extends EventBus>[] eventBusIntfs, InvocationHandler handler);

    /**
     * Should create a dynamic proxy handler.
     * 
     * @param eventBusIntfs the eventbus segment interfaces that make up the complete event bus
     * @return a dynamic proxy invocation handler
     */
    protected abstract InvocationHandler createHandler(Class<? extends EventBus>[] eventBusIntfs);

}
