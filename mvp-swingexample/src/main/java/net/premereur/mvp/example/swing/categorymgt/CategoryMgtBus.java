package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.domain.model.Category;

public interface CategoryMgtBus extends EventBus {
    @Event(CategoryListPresenter.class)
    void categoryMgtActivated();

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
