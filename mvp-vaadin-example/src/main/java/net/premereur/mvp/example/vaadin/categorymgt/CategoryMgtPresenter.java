package net.premereur.mvp.example.vaadin.categorymgt;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.vaadin.app.ApplicationBus;
import net.premereur.mvp.example.vaadin.app.ApplicationWindow;
import net.premereur.mvp.example.vaadin.data.CategoryItem;

import com.google.inject.Inject;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;

/**
 * Handles events related to category management.
 * 
 * @author gpremer
 * 
 */
public final class CategoryMgtPresenter extends BasePresenter<CategoryMgtView, CategoryMgtBus> {

    private static final long serialVersionUID = 1L;
    private final CategoryRepository categoryRepository;
    private boolean active = false;

    @Inject
    public CategoryMgtPresenter(final EventBus eventBus, final CategoryMgtView view, final CategoryRepository categoryRepository) {
        super((CategoryMgtBus) eventBus, view);
        this.categoryRepository = categoryRepository;
    }

    /**
     * See {@link CategoryMgtBus#activate(ApplicationWindow)}.
     */
    public void onActivate(final ApplicationWindow window) {
        if (!active) {
            window.setWorkPane(getView()); // TODO use event bus
            final Container catContainer = new BeanItemContainer<Category>(categoryRepository.allCategories());
            getView().setCategories(catContainer);
            getView().forwardCategorySelection(this);
            getView().forwardCategorySave(this);
            active = true;
        }
    }

    void selectCategory(final Category category) {
        CategoryItem formData = new CategoryItem(category);
        getView().edit(formData);
    }

    void deselectCategory() {
        getView().edit(new CategoryItem(new Category("new category")));
    }

    void saveCategory(final CategoryItem categoryItem) {
        final Category category = categoryItem.getBean();
        categoryRepository.save(category); // if this could throw, we would have to catch
        getEventBus().changedCategory(category);
    }

    /**
     * See {@link CategoryMgtBus#changedCategory(Category)}.
     */
    public void onChangedCategory(final Category category) {
        getView().refreshCategories(category);
        getEventBus(ApplicationBus.class).showMessage("Saved category");
    }
}
