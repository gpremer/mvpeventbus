package net.premereur.mvp.example.swing.presenter;

import net.premereur.mvp.core.BasePresenter;
import net.premereur.mvp.core.View;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.swing.eventbus.DemoEventBus;

public abstract class CategoryPresenterBase<V extends View> extends BasePresenter<V, DemoEventBus> {

	private CategoryRepository repository;

	public CategoryPresenterBase() {
		setRepository(CategoryRepository.instance());
	}
	
	public void setRepository(CategoryRepository repository) {
		this.repository = repository;
	}
	
	public CategoryRepository getRepository() {
		return repository;
	}
}
