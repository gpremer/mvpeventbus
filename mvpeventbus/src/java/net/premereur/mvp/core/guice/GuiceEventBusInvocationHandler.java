package net.premereur.mvp.core.guice;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.basic.EventBusInvocationHandler;
import net.premereur.mvp.core.basic.PresenterFactory;

public class GuiceEventBusInvocationHandler extends EventBusInvocationHandler {

	public GuiceEventBusInvocationHandler(Class<? extends EventBus>[] eventBusClasses, PresenterFactory presenterFactory) {
		super(eventBusClasses, presenterFactory, new GuiceEventBusVerifier());
	}

}
