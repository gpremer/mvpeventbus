package net.premereur.mvp.core.guice;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.basic.PresenterFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.util.Providers;

public class EventBusModule extends AbstractModule {

	private EventBus eventBus;
	
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	
	@Override
	protected void configure() {
		bind(PresenterFactory.class).to(GuicePresenterFactory.class);
//		bind(EventBus.class).toProvider((Provider<? extends EventBus>) Providers.of(null));
//		for (Class<? extends EventBus> eventBusIntf : eventBusIntfs ) {
//		bind(((Key<EventBus>)(Key.get(eventBusIntf)))).toInstance(eventBus);
//	}
	}
	
	@Provides
	public EventBus providesEventBus() {
		return eventBus;
	}

	
}
