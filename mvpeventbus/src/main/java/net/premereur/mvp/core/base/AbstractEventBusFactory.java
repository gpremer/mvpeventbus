package net.premereur.mvp.core.base;

import static java.util.Arrays.asList;

import java.lang.reflect.InvocationHandler;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventBusFactory;

/**
 * A base class factory for event bus factories.
 * 
 * @param <E> the type of the main event bus.
 * 
 * @author gpremer
 * 
 */
public abstract class AbstractEventBusFactory<E extends EventBus> implements EventBusFactory<E> {

    private final Collection<Class<? extends EventBus>> eventBusInterfaces;

    /**
     * A specification for an event bus factory.
     * 
     * @author gpremer
     * 
     * @param <E> the type of the main event bus segment
     */
    public abstract static class Configuration<E extends EventBus> {
        private Set<Class<? extends EventBus>> eventBusInterfaces = new HashSet<Class<? extends EventBus>>();
        private final Class<E> mainEventBusInterface;

        /**
         * Constructs a specification that will ultimately generate a factory with the argument as main bus interface.
         * 
         * @param mainEventBusInterface the main interface of the busses to create
         */
        protected Configuration(final Class<E> mainEventBusInterface) {
            this.mainEventBusInterface = mainEventBusInterface;
            eventBusInterfaces.add(mainEventBusInterface);
        }

        /**
         * Adds a bunch of segments.
         * 
         * @param additionalEventBusInterfaces the segments to add
         */
        protected final void add(final Class<? extends EventBus>... additionalEventBusInterfaces) {
            this.eventBusInterfaces.addAll(asList(additionalEventBusInterfaces));
        }

        /**
         * Finally creates the factory.
         * 
         * @return an event bus factory that can be used to create event bus instances
         */
        public abstract EventBusFactory<E> build();

        /**
         * The event bus interfaces that where specified.
         * 
         * @return the eventBusInterfaces
         */
        protected final Set<Class<? extends EventBus>> getEventBusInterfaces() {
            return eventBusInterfaces;
        }

        /**
         * The main main event bus interface that was specified.
         * 
         * @return the mainEventBusInterface
         */
        protected final Class<E> getMainEventBusInterface() {
            return mainEventBusInterface;
        }

    }

    /**
     * Creates the base part of an EventBusFactory.
     * 
     * @param eventBusInterfaces the interfaces the event bus will implement
     */
    protected AbstractEventBusFactory(final Collection<Class<? extends EventBus>> eventBusInterfaces) {
        this.eventBusInterfaces = eventBusInterfaces;
    }

    /**
     * Returns all event bus interfaces that created event busses need to implement.
     */
    protected final Collection<Class<? extends EventBus>> getEventBusInterfaces() {
        return eventBusInterfaces;
    }

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
     * @return an event bus implementing all of the segment interfaces
     */
    @SuppressWarnings("unchecked")
    public final E create() {
        InvocationHandler handler = createHandler(getEventBusInterfaceArray());
        return (E) createProxy(getEventBusInterfaceArray(), handler);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends EventBus>[] getEventBusInterfaceArray() {
        return getEventBusInterfaces().toArray((Class<? extends EventBus>[]) (new Class<?>[] {}));
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
