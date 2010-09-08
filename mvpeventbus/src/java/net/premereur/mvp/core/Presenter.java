package net.premereur.mvp.core;

public interface Presenter<V extends View<E>, E extends EventBus> {

	void setView (V view);
	void setEventBus(E eventBus);
}
