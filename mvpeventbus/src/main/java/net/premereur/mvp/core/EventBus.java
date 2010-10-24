package net.premereur.mvp.core;

/**
 * EventBusses receive events from {@link Presenter}s (or {@link View}s) and distribute them to other Presenters.
 * 
 * Events are sent to an EventBus by calling methods that are annotated by {@link Event} annotations. The event annotation defines which Presenters the method
 * invocation will be forwarded to. By design, a method <i>event</i> on the EventBus will forward to a method <i><b>on</b>Event</i> on each associated
 * Presenter.
 * 
 * Concrete implementations of an EventBus are created by an {@link net.premereur.mvp.core.basic.BasicEventBusFactory.BasicEventBusFactory}.
 * 
 * EventBusses can be combined with other {@link EventBus}ses. Each EventBus interface then becomes a segment of a greater event bus. Events sent to one event
 * bus will also be handled by other event buses as long as that other bus declares the same event method. Strictly speaking there is no "master" event bus
 * segment. It is good practice though to have one event bus segment dispatch cross-application events while the others are focused on smaller functional
 * blocks.
 * 
 * The way event busses are typically implemented, performance isn't significantly impacted by how many events they respond to.
 * 
 * @author gpremer
 * 
 */
public interface EventBus {

}
