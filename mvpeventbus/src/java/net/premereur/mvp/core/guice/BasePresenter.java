package net.premereur.mvp.core.guice;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;

public abstract class BasePresenter<V extends View, E extends EventBus> implements Presenter<V, E> {

	private final E eventBus;

	private final V view;

	public BasePresenter(final E eventBus, final V view) {
		this.eventBus = eventBus;
		this.view = view;
	}

	@Override
	public void setEventBus(E eventBus) {
		// To respect interface
	}

	protected E getEventBus() {
		return eventBus;
	}

	/**
	 * Returns the event bus associated with the presenter cast to the type
	 * given as an argument. This is meant to be used for composite busses that
	 * actually use the requested bus segment.
	 * 
	 * @param <Bus>
	 *            The type of the bus segment
	 * @param eventBusClass
	 *            The class of the type of the bus segment
	 * @return the event bus
	 */
	@SuppressWarnings("unchecked")
	protected <Bus extends EventBus> Bus getEventBus(Class<Bus> eventBusClass) {
		return (Bus) getEventBus();
	}

	@Override
	public void setView(V view) {
		// To respect interface
	}

	protected V getView() {
		return view;
	}

}