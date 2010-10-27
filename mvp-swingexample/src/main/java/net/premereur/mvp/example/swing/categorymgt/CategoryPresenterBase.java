package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.core.View;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.domain.repository.CategoryRepository;

public abstract class CategoryPresenterBase<V extends View> extends BasePresenter<V, CategoryMgtBus> {

    private CategoryRepository repository;

    public CategoryPresenterBase(final CategoryMgtBus eventBus, final V view, final CategoryRepository repository) {
        super(eventBus, view);
        this.repository = repository;
    }

    public final CategoryRepository getRepository() {
        return repository;
    }

}
