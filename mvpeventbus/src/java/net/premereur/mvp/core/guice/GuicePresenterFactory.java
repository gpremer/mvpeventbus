package net.premereur.mvp.core.guice;

import com.google.inject.Inject;
import com.google.inject.Injector;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;
import net.premereur.mvp.core.basic.PresenterFactory;

public class GuicePresenterFactory extends PresenterFactory {

	final private Injector injector;
	
	@Inject
	public GuicePresenterFactory(Injector injector) {
		this.injector = injector;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Presenter<View, ? extends EventBus> getPresenter(Class<?> presenterClass, EventBus eventBus) {
		Presenter<View, ? extends EventBus> presenter = (Presenter<View, ? extends EventBus>) injector.getInstance(presenterClass);
		return presenter;
	}
	
}
