package net.premereur.mvp.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Event annotations are used to mark methods on {@link EventBus}es. Calling these event methods will cause the corresponding method in all {@link EventHandler}
 * s enumerated in the Event annotation's argument to be called. The corresponding method is a method with same arguments with its name capitalised and prefixed
 * with on.
 * 
 * E.g. method
 * 
 * <pre> &#064;Event( { Handler1.class, Handler2.class }) void event(Integer val); </pre>
 * 
 * will be forwarded to
 * 
 * <pre> public void onEvent(Integer val) </pre>
 * 
 * on both Handler1 and Handler2.
 * 
 * All arguments on the original are passed through.
 * 
 * Event methods should be public, have a void return type and only take reference data types as arguments.
 * 
 * @author gpremer
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Event {

    /**
     * Enumerates the creation policies that are supported.
     * 
     * @author gpremer
     * 
     */
    public static enum Instantiation {
        /**
         * Forces the creation of a new instance of the handler (and associates it with the bus). This instance is the only one that will receive the event.
         */
        TO_NEW_INSTANCE,
        /**
         * Only instantiates a new instance if one is not yet associated with the event bus. If multiple instances are registered (associated with the bus),
         * they will all receive the event. This is the default policy.
         */
        TO_INSTANCES,
        /**
         * Only send the event if at least one handler already exists. In that case all instances (associated with the bus) will receive the event.
         */
        TO_EXISTING_INSTANCES
    }

    /**
     * The invocation policies that are supported.
     * 
     * @author gpremer
     * 
     */
    public static enum Invocation {
        /**
         * Process this event immediately, even when called when another event handler is executing.
         */
        IMMEDIATE,
        /**
         * Process this event after the currently executing event has finished. If no event is executing when sending this event, execute immediately.
         */
        DELAYED
    }

    /**
     * The {@link EventHandler}s to forward this event to.
     */
    Class<? extends EventHandler>[] value();

    /**
     * Sets the instantiation policy for an event handler.
     */
    Instantiation instantiation() default Instantiation.TO_INSTANCES;

    /**
     * Sets the invocation policy for an event handler.
     */
    Invocation invocation() default Invocation.IMMEDIATE;
}
