package net.premereur.mvp.example.swing.eventbus;

import javax.swing.JComponent;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.swing.presenter.ApplicationPresenter;

public interface ApplicationBus extends EventBus {

	@Event(ApplicationPresenter.class)
	void applicationStarted();

	@Event(ApplicationPresenter.class)
	void setLeftComponent(JComponent component);

	@Event(ApplicationPresenter.class)
	void setCenterComponent(JComponent component);

	@Event(ApplicationPresenter.class)
	void setFeedback(String text);

}
