package net.premereur.mvp.core;

/**
 * 
 * EventBusFactories create event bus implementations. These implementations are synthetic classes that are typically generated on the fly. The factory
 * implementations are meant to be cached for performance, though it is not strictly necessary.
 * 
 * @param <E> the main event bus type.
 * 
 * @author gpremer
 * 
 */
public interface EventBusFactory<E extends EventBus> {

    /**
     * Creates an event bus implementation as configured with this factory's specification. The returned object implements <em>all</em> event bus interfaces
     * given in the specification, but the declared type is the main event bus type. In fact, this is the only difference between the main event bus type and
     * the additional event bus types.
     * 
     * @return an event bus implementing all supplied interfaces and thus all event methods contained therein.
     */
    E create();

}
