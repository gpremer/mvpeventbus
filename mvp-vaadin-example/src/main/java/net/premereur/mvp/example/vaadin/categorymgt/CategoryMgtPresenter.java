package net.premereur.mvp.example.vaadin.categorymgt;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.vaadin.app.ApplicationWindow;
import net.premereur.mvp.example.vaadin.data.CategoryItem;

import com.google.inject.Inject;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;

public class CategoryMgtPresenter extends BasePresenter<CategoryMgtView, CategoryMgtBus> {

    private final CategoryRepository categoryRepository;

    @Inject
    public CategoryMgtPresenter(final EventBus eventBus, final CategoryMgtView view, final CategoryRepository categoryRepository) {
        super((CategoryMgtBus) eventBus, view);
        this.categoryRepository = categoryRepository;
    }

    public void onActivate(final ApplicationWindow window) {
        window.setWorkPane(getView());
        Container catContainer = new BeanItemContainer<Category>(categoryRepository.allCategories());
        getView().setCategories(catContainer);
        getView().forwardCategorySelection(this);
    }

    public void selectCategory(final Category category) {
        getView().edit(new CategoryItem(category));
    }
}
