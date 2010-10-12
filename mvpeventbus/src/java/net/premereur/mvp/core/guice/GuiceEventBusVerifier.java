package net.premereur.mvp.core.guice;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;
import net.premereur.mvp.core.impl.EventBusVerifier;

public class GuiceEventBusVerifier extends EventBusVerifier {

	@Override
	protected void verifyHasUseViewAnnotation(Class<? extends Presenter<? extends View, ? extends EventBus>> handlerClass) {
	}

	@Override
	protected void verifyHasDefaultConstructor(Class<?> handlerClass) {
	}
}
