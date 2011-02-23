package net.premereur.mvp.core;

/**
 * The Presenter in the MVP pattern. Presenters are instantiated by an {@link EventBus} when they are about to receive their first event. At that time they also
 * receive a reference to an instantiated view and the event bus they can use to send new events to.
 * 
 * @author gpremer
 * 
 * @param <V> The {@link View} interface associated with the presenter.
 * @param <E> The {@link EventBus} interface exchanging events with the presenter.
 */
public interface Presenter<V extends View, E extends EventBus> extends EventHandler {

}
