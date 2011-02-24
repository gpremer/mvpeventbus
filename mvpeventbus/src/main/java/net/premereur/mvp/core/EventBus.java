package net.premereur.mvp.core;

/**
 * EventBusses receive events from {@link Presenter}s (or {@link View}s) and distribute them to {@link EventHandler}s, typically Presenters.
 * 
 * Events are sent to an EventBus by calling methods that are annotated by {@link Event} annotations. The event annotation defines to which event handlers the
 * method invocation is forwarded. By design, a method <i>event</i> on the EventBus will forward to a method <i><b>on</b>Event</i> on each associated
 * EventHandler.
 * 
 * Concrete implementations of an EventBus are created by an {@link net.premereur.mvp.core.basic.AbstractEventBusFactory.BasicEventBusFactory} or
 * {@link net.premereur.mvp.core.basic.AbstractEventBusFactory.GuiceEventBusFactory}.
 * 
 * EventBusses can be combined with other {@link EventBus}ses. Each EventBus interface then becomes a segment of a larger event bus. Events sent to one event
 * bus will also be handled by other event bus segments as long as that other segment declares the same event method. Strictly speaking there is no "master" or
 * "main" event bus segment. It is good practice though to have one event bus segment dispatch cross-application events while the others are focused on smaller
 * functional blocks.
 * 
 * The way event busses are typically implemented, performance isn't significantly impacted by how many events they contain.
 * 
 * @author gpremer
 * 
 */
public interface EventBus {

    /**
     * Detaches the event handler from the event bus. This means that it becomes available for garbage collection (if no other reference is held within the
     * application). A new instance of the same handler class will be created if an event that dispatches to this handler class is invoked. If an event handler
     * is detached from one segment it is detached from all segments of the bus.
     * 
     * @param handler the handler to be detached from the bus.
     */
    void detach(EventHandler handler);
}
