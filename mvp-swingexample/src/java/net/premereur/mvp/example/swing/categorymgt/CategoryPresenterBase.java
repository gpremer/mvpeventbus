package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.core.View;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.domain.repository.CategoryRepository;

public abstract class CategoryPresenterBase<V extends View> extends BasePresenter<V, CategoryMgtBus> {

	private CategoryRepository repository;

	public CategoryPresenterBase(CategoryMgtBus eventBus, V view, CategoryRepository repository) {
		super(eventBus, view);
		this.repository = repository;
	}

	public CategoryRepository getRepository() {
		return repository;
	}

}
