package net.premereur.mvp.core;

/**
 * EventBusses receive events from {@link Presenter}s (or {@link View}s) and
 * distribute them to other Presenters.
 * 
 * Events are sent to an EventBus by calling methods that are annotated by
 * {@link Event} annotations. The event annotation defines which to Presenters
 * to method invocation will be forwarded to. By design, a method <i>event</i>
 * on the EventBus will forward to a method <i><b>on</b>Event</i> on each
 * associated Presenter.
 * 
 * Concrete implementations of an EventBus are created by an
 * {@link EventBusFactory}.
 * 
 * EventBusses can be combined with other {@link EventBus}ses (called "master"
 * EventBus). See {@link JoinableEventBus}. 
 * 
 * The way event busses are typically implemented it doesn't matter much how many
 * events they respond to.
 * 
 * @author gpremer
 * 
 */
public interface EventBus {

}
