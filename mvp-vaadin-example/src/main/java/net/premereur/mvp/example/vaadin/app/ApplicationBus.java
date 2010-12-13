package net.premereur.mvp.example.vaadin.app;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;

import com.vaadin.Application;

public interface ApplicationBus extends EventBus {

	@Event(ApplicationPresenter.class)
	void init(Application app);

	@Event(ApplicationPresenter.class)
	void close(Application app);
}
