package net.premereur.mvp.core.base;

import static java.util.Arrays.asList;

import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
    private final Collection<EventInterceptor> interceptors;

    /**
     * A specification for an event bus factory.
     * 
     * @author gpremer
     * 
     * @param <E> the type of the main event bus segment
     * @param <D> the type of a Configuration specialisation
     */
    public abstract static class Configuration<E extends EventBus, D extends Configuration<?, ?>> {
        private Set<Class<? extends EventBus>> eventBusInterfaces = new HashSet<Class<? extends EventBus>>();
        private final Class<E> mainEventBusInterface;
        private final List<EventInterceptor> interceptors = new ArrayList<EventInterceptor>();

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
        private void add(final Class<? extends EventBus>... additionalEventBusInterfaces) {
            this.eventBusInterfaces.addAll(asList(additionalEventBusInterfaces));
        }

        /**
         * Adds an additional segment to the factory. Can be called many times, but see also {@link #withAdditionalSegments(Class...)} to add several segments
         * at once.
         * 
         * @param additionalEventBusInterface a segment the factory should create a bus implementation for
         */
        @SuppressWarnings("unchecked")
        public final D withAdditionalSegment(final Class<? extends EventBus> additionalEventBusInterface) {
            add(additionalEventBusInterface);
            return (D) this;
        }

        /**
         * Adds several additional segments to the factory. Can be called many times, but see also {@link #withAdditionalSegment(Class...)} to add only a single
         * segment.
         * 
         * @param additionalEventBusInterfaces a segment the factory should create a bus implementation for
         */
        @SuppressWarnings("unchecked")
        public final D withAdditionalSegments(final Class<? extends EventBus>... additionalEventBusInterfaces) {
            add(additionalEventBusInterfaces);
            return (D) this;
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
         */
        protected final Set<Class<? extends EventBus>> getEventBusInterfaces() {
            return eventBusInterfaces;
        }

        /**
         * The main main event bus interface that was specified.
         * 
         */
        protected final Class<E> getMainEventBusInterface() {
            return mainEventBusInterface;
        }

        /**
         * Adds an additional interceptor to the list of event interceptors.
         * 
         * @param interceptor will intercept method invocations
         */
        private void addInterceptor(final EventInterceptor interceptor) {
            interceptors.add(interceptor);
        }

        /**
         * All configured interceptors.
         * 
         */
        protected final List<EventInterceptor> getInterceptors() {
            return interceptors;
        }

        /**
         * The given intercepter will be called for all events sent to the bus.
         * 
         * @param interceptor intercepts the event
         * @return a Configuration instance
         */
        @SuppressWarnings("unchecked")
        public final D interceptedBy(final EventInterceptor interceptor) {
            addInterceptor(interceptor);
            return (D) this;
        }

    }

    /**
     * Creates the base part of an EventBusFactory.
     * 
     * @param eventBusInterfaces the interfaces the event bus will implement
     * @param interceptors Interceptors to be called before invoking an event
     */
    protected AbstractEventBusFactory(final Collection<Class<? extends EventBus>> eventBusInterfaces, final Collection<EventInterceptor> interceptors) {
        this.eventBusInterfaces = eventBusInterfaces;
        this.interceptors = interceptors;
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
     * event handlers. When there's a need to send events to event sinks on other, combined, busses, the designer has the choice of replicating event methods in the
     * attached bus or calling events directly on the master bus.
     * 
     * @return an event bus implementing all of the segment interfaces
     */
    @SuppressWarnings("unchecked")
    public final E create() {
        InvocationHandler handler = createInvocationHandler(getEventBusInterfaceArray(), interceptors);
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
     * @param intercepters The interceptors to call before the event dispatch
     * @return a dynamic proxy invocation handler
     */
    protected abstract InvocationHandler createInvocationHandler(Class<? extends EventBus>[] eventBusIntfs, Collection<EventInterceptor> intercepters);

}
