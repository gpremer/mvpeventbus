package net.premereur.mvp.example.vaadin;

import net.premereur.mvp.core.EventBusFactory;
import net.premereur.mvp.core.guice.GuiceEventBusFactory;
import net.premereur.mvp.example.vaadin.app.ApplicationBus;
import net.premereur.mvp.example.vaadin.app.ApplicationModule;

import com.vaadin.Application;

@SuppressWarnings("serial")
public class SampleApplication extends Application {

	private static final EventBusFactory EVENT_BUS_FACTORY;

	static {
		EVENT_BUS_FACTORY = new GuiceEventBusFactory(new ApplicationModule());
	}

	@Override
	public final void init() {
		EVENT_BUS_FACTORY.createEventBus(ApplicationBus.class).init(this);		
	}
}
