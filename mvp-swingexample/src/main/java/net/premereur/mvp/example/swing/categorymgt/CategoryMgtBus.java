package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.domain.model.Category;

/**
 * Event bus segment for presenters and views related to Category management.
 * 
 * @author gpremer
 * 
 */
public interface CategoryMgtBus extends EventBus {
    /**
     * Sends the signal that category management should be activated.
     */
    @Event(CategoryListPresenter.class)
    void categoryMgtActivated();

    /**
     * Sends the signal that a category is selected and should be edited.
     */
    @Event(CategoryUpdatePresenter.class)
    void categorySelected(Category selectedCategory);

    /**
     * Sends the signal that a category was modified.
     */
    @Event(CategoryListPresenter.class)
    void categoryChanged(Category category);

    /**
     * Sends the signal that no category is selected anymore.
     */
    @Event(DefaultCategoryPanelPresenter.class)
    void noCategorySelected();

    /**
     * Sends the signal that creating new category should be begin.
     */
    @Event(CategoryCreatorPresenter.class)
    void activateCategoryCreation();

    /**
     * Sends the signal that a new category is added.
     */
    @Event(CategoryListPresenter.class)
    void categoryAdded(Category category);

}
