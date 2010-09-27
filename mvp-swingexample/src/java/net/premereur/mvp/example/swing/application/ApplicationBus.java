package net.premereur.mvp.example.swing.application;

import javax.swing.JComponent;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;

public interface ApplicationBus extends EventBus {

	@Event(ApplicationPresenter.class)
	void applicationStarted();

	@Event(ApplicationPresenter.class)
	void clearScreen();

	@Event(ApplicationPresenter.class)
	void setLeftComponent(JComponent component);

	@Event(ApplicationPresenter.class)
	void setCenterComponent(JComponent component);

	@Event(ApplicationPresenter.class)
	void setFeedback(String text);

}
