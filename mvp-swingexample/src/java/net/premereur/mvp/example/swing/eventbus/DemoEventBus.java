package net.premereur.mvp.example.swing.eventbus;

import javax.swing.JComponent;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.presenter.ApplicationPresenter;
import net.premereur.mvp.example.swing.presenter.CategoryCreatorPresenter;
import net.premereur.mvp.example.swing.presenter.CategoryListPresenter;
import net.premereur.mvp.example.swing.presenter.CategoryUpdatePresenter;
import net.premereur.mvp.example.swing.presenter.DefaultCategoryPanelPresenter;

public interface DemoEventBus extends EventBus {

	@Event(ApplicationPresenter.class)
	void applicationStarted();

	@Event(ApplicationPresenter.class)
	void setLeftComponent(JComponent component);

	@Event(ApplicationPresenter.class)
	void setCenterComponent(JComponent component);

	@Event(ApplicationPresenter.class)
	void setFeedback(String text);

	@Event(CategoryListPresenter.class)
	void categoryListActivated();

	@Event(CategoryUpdatePresenter.class)
	void categorySelected(Category selectedCategory);

	@Event(CategoryListPresenter.class)
	void categoryChanged(Category category);

	@Event(DefaultCategoryPanelPresenter.class)
	void defaultCategoryPanelActivated();

	@Event(CategoryCreatorPresenter.class)
	void activateCategoryCreation();

	@Event(CategoryListPresenter.class)
	void categoryAdded(Category category);

}
