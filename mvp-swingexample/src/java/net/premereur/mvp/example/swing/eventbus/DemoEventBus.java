package net.premereur.mvp.example.swing.eventbus;

import javax.swing.JComponent;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.presenter.ApplicationPresenter;
import net.premereur.mvp.example.swing.presenter.CategoryListPresenter;
import net.premereur.mvp.example.swing.presenter.CategoryUpdatePresenter;

public interface DemoEventBus extends EventBus {

	@Event(handlers = ApplicationPresenter.class)
	void applicationStarted();

	@Event(handlers = ApplicationPresenter.class)
	void setLeftComponent(JComponent component);

	@Event(handlers = ApplicationPresenter.class)
	void setCenterComponent(JComponent component);

	@Event(handlers = ApplicationPresenter.class)
	void setFeedback(String text);

	@Event(handlers = CategoryListPresenter.class)
	void categoryListActivated();

	@Event(handlers = CategoryUpdatePresenter.class)
	void categorySelected(Category selectedCategory);

	@Event(handlers = CategoryListPresenter.class)
	void categoryChanged(Category category);
}
