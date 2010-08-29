package net.premereur.mvp.example.swing.eventbus;

import javax.swing.JFrame;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.swing.presenter.ApplicationPresenter;
import net.premereur.mvp.example.swing.presenter.CategoryListPresenter;

public interface DemoEventBus extends EventBus {

	@Event(handlers = ApplicationPresenter.class)
	void applicationStarted();
	
	@Event(handlers = CategoryListPresenter.class)
	void categoryListActivated(JFrame frame);
}
