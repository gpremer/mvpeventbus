package net.premereur.mvp.core.basic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Set;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.AbstractEventBusFactory;

/**
 * A factory for event busses that uses only standard Java SE mechanisms.
 * 
 * @param <EB> the type of the main event bus.
 * 
 * @author gpremer
 * 
 */
public final class BasicEventBusFactory<EB extends EventBus> extends AbstractEventBusFactory<EB> {

    /**
     * Specification for {@link BasicEventBusFactory} instances.
     * 
     * @author gpremer
     * 
     * @param <E>
     */
    public static final class Configuration<E extends EventBus> extends AbstractEventBusFactory.Configuration<E> {

        private Configuration(final Class<E> mainEventBusInterface) {
            super(mainEventBusInterface);
        }

        /**
         * Adds an additional segment to the factory. Can be called many times, but see also {@link #withAdditionalSegments(Class...)} to add several segments
         * at once.
         * 
         * @param additionalEventBusInterface a segment the factory should create a bus implementation for
         */
        @SuppressWarnings("unchecked")
        public Configuration<E> withAdditionalSegment(final Class<? extends EventBus> additionalEventBusInterface) {
            add(additionalEventBusInterface);
            return this;
        }

        /**
         * Adds several additional segments to the factory. Can be called many times, but see also {@link #withAdditionalSegment(Class...)} to add only a single
         * segment.
         * 
         * @param additionalEventBusInterfaces a segment the factory should create a bus implementation for
         */
        public Configuration<E> withAdditionalSegments(final Class<? extends EventBus>... additionalEventBusInterfaces) {
            add(additionalEventBusInterfaces);
            return this;
        }

        /**
         * Finally creates the factory.
         * 
         * @return an event bus factory that can be used to create event bus instances
         */
        public BasicEventBusFactory<E> build() {
            return new BasicEventBusFactory<E>(getEventBusInterfaces());
        }

    }

    /**
     * Creates a factory creating busses with the supplied interfaces.
     * 
     * @param eventBusInterfaces the bus type interfaces
     */
    public BasicEventBusFactory(final Set<Class<? extends EventBus>> eventBusInterfaces) {
        super(eventBusInterfaces);
    }

    /**
     * Starts specifying an EventBusFactory by assigning the type of the main segments. The main segment is not different from the other segments, but it does
     * determine the type with which the event bus is instantiated.
     * 
     * @param <E> the main type of the event bus
     * @param mainEventBusInterface the main type of the event bus
     * @return a specification that can be configured further
     */
    public static <E extends EventBus> Configuration<E> withMainSegment(final Class<E> mainEventBusInterface) {
        return new Configuration<E>(mainEventBusInterface);
    }

    /**
     * Starts specifying an EventBusFactory by assigning the type of the main segments. The main segment is not different from the other segments, but it does
     * determine the type with which the event bus is instantiated.
     * 
     * @param <E> the main type of the event bus
     * @param mainEventBusInterface the main type of the event bus
     * @param additionalEventBusInterfaces Additional event bus interfaces to implement
     * @return a specification that can be configured further
     */
    public static <E extends EventBus> Configuration<E> withSegments(final Class<E> mainEventBusInterface,
            final Class<? extends EventBus>... additionalEventBusInterfaces) {
        final Configuration<E> configuration = new Configuration<E>(mainEventBusInterface);
        configuration.withAdditionalSegments(additionalEventBusInterfaces);
        return configuration;
    }

    /**
     * {@inheritDoc}
     */
    protected Object createProxy(final Class<? extends EventBus>[] eventBusIntfs, final InvocationHandler handler) {
        return Proxy.newProxyInstance(BasicEventBusFactory.class.getClassLoader(), eventBusIntfs, handler);
    }

    /**
     * {@inheritDoc}
     */
    protected EventBusInvocationHandler createInvocationHandler(final Class<? extends EventBus>[] eventBusIntfs) {
        return new EventBusInvocationHandler(eventBusIntfs);
    }

}
