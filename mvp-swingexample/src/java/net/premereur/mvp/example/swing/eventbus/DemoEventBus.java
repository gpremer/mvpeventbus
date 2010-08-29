package net.premereur.mvp.example.swing.eventbus;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.swing.presenter.ApplicationPresenter;

public interface DemoEventBus extends EventBus {

	@Event(handlers = ApplicationPresenter.class)
	void applicationStarted();
}
