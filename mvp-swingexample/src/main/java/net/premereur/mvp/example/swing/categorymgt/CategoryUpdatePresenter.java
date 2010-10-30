package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.swing.application.ApplicationBus;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Manages the Category update view.
 * 
 * @author gpremer
 * 
 */
@Singleton
public class CategoryUpdatePresenter extends SingleCategoryPresenterBase<CategoryUpdaterPanel> {

    @Inject
    public CategoryUpdatePresenter(final CategoryMgtBus eventBus, final CategoryUpdaterPanel view, final CategoryRepository repository) {
        super(eventBus, view, repository);
    }

    /**
     * See {@link CategoryMgtBus#categorySelected(Category)}.
     */
    public final void onCategorySelected(final Category selectedCategory) {
        CategoryUpdaterPanel view = getView();
        view.bind(selectedCategory);
        getEventBus(ApplicationBus.class).setCenterComponent(view);
    }

    /**
     * {@inheritDoc}
     */
    public final void saveClicked(final Category category) {
        getRepository().save(category);
        getEventBus().noCategorySelected();
        getEventBus().categoryChanged(category);
        getEventBus(ApplicationBus.class).setFeedback("Category updated");
    }

}
