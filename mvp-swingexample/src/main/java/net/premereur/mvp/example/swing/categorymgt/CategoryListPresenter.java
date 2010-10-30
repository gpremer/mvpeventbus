package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.swing.application.ApplicationBus;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Manages the category list.
 * 
 * @author gpremer
 * 
 */
@Singleton
public class CategoryListPresenter extends CategoryPresenterBase<CategoryList> {

    @Inject
    public CategoryListPresenter(final EventBus eventBus, final CategoryList view, final CategoryRepository repository) {
        super((CategoryMgtBus) eventBus, view, repository);
        view.addSelectionListener(this);
    }

    /**
     * Handles the activation of the category management module by showing a list of all categories.
     */
    public final void onCategoryMgtActivated() {
        final CategoryList categoryList = getView();
        categoryList.bind(getRepository().allCategories());
        getEventBus(ApplicationBus.class).setLeftComponent(categoryList);
        getEventBus().noCategorySelected();
    }

    /**
     * Reacts on the change to a category.
     * 
     */
    public final void onCategoryChanged(final Category category) {
        getView().refreshList();
    }

    /**
     * Reacts on the addition of a new category.
     * 
     */
    public final void onCategoryAdded(final Category category) {
        getView().bind(getRepository().allCategories());
    }

    /**
     * Receives the UI category selection event and sends an event bus event.
     */
    public final void categorySelected(final Category selectedCategory) {
        getEventBus().categorySelected(selectedCategory);
    }

}
