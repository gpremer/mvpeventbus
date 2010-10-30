package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;

/**
 * Base class for presenters that manage a single Category.
 * 
 * @author gpremer
 * 
 * @param <V> The type fo the view
 */
public abstract class SingleCategoryPresenterBase<V extends CategoryPanelBase> extends CategoryPresenterBase<V> {

    public SingleCategoryPresenterBase(final CategoryMgtBus eventBus, final V view, final CategoryRepository repository) {
        super(eventBus, view, repository);
        view.setSaveButtonListener(this);
        view.setCancelButtonListener(this);
    }

    /**
     * Call back method for when the view receives a cancel request.
     * 
     */
    public final void cancelClicked() {
        getEventBus().noCategorySelected();
    }

    /**
     * Call back method for when the view receives a save request.
     * 
     * @param category The category that is saved.
     */
    public abstract void saveClicked(Category category);
}
