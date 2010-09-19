package net.premereur.mvp.core;

public abstract class BasePresenter<V extends View, E extends EventBus> implements Presenter<V, E> {

	private E eventBus;

	private V view;

	@Override
	public void setEventBus(E eventBus) {
		this.eventBus = eventBus;
	}

	protected E getEventBus() {
		return eventBus;
	}

	@Override
	public void setView(V view) {
		this.view = view;
	}

	protected V getView() {
		return view;
	}
}
