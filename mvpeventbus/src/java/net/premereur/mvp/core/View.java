package net.premereur.mvp.core;

public interface View<E extends EventBus> {

	void setEventBus(E eventBus);

}
