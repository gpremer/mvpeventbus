package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.core.View;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.domain.repository.CategoryRepository;

/**
 * Base class for presenters that need a {@link CategoryRepository}.
 * 
 * @author gpremer
 * 
 * @param <V> The view class for the presenter
 */
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
