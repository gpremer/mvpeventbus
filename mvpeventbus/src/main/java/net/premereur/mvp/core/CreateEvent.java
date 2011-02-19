package net.premereur.mvp.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an event method on an {@link EventBus} so that it instantiation policy can be influenced. By default, an event handler is instantiated only the first
 * time when an event is received. Afterwards, the same handler instance will be used when the {@link Event} annotation specifies the same handler class. A
 * creation policy of REQUIRES_NEW, however, forces the creation of an additional handler.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CreateEvent {

    /**
     * Enumerates the creation policies that are supported.
     * 
     * @author gpremer
     * 
     */
    public static enum Policy2 {
        /**
         * Forces the creation of a new instance of the handler (and associates it with the bus).
         */
        ALWAYS_NEW,
        /**
         * Only instantiates a new instance if one is not yet associated with the event bus.
         */
        ONE_INSTANCE
    }

    /**
     * Sets the creation policy for an event handler.
     */
    Policy2 value() default Policy2.ONE_INSTANCE;
}
