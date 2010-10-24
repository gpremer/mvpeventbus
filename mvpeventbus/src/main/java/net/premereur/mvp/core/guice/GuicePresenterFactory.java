package net.premereur.mvp.core.guice;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.google.inject.Inject;
import com.google.inject.Injector;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;
import net.premereur.mvp.core.basic.PresenterFactory;

public class GuicePresenterFactory extends PresenterFactory {

	final private Injector injector;
	final private WeakHashMap<EventBus, Map<Class<?>, Presenter<View, ? extends EventBus>>> cache = new WeakHashMap<EventBus, Map<Class<?>, Presenter<View, ? extends EventBus>>>();

	@Inject
	public GuicePresenterFactory(Injector injector) {
		this.injector = injector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Presenter<View, ? extends EventBus> getPresenter(Class<?> presenterClass, EventBus eventBus) {
		// There should be no 2 threads requiring the same event bus, just to be
		// on the safe side
		synchronized (eventBus) {
			Map<Class<?>, Presenter<View, ? extends EventBus>> eventBusPresenters = cache.get(eventBus);
			if (eventBusPresenters == null) {
				eventBusPresenters = new HashMap<Class<?>, Presenter<View, ? extends EventBus>>();
				cache.put(eventBus, eventBusPresenters);
			}
			Presenter<View, ? extends EventBus> presenter = eventBusPresenters.get(presenterClass);
			if (presenter == null) {
				presenter = (Presenter<View, ? extends EventBus>) injector.getInstance(presenterClass);
				eventBusPresenters.put(presenterClass, presenter);
			}
			return presenter;
		}
	}

}
