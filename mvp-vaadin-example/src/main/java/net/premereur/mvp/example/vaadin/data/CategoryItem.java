package net.premereur.mvp.example.vaadin.data;

import net.premereur.mvp.example.domain.model.Category;

import com.vaadin.data.util.BeanItem;

/**
 * Wraps a category as a {@link BeanItem} for use by the Vaadin components.
 * 
 * @author gpremer
 * 
 */
public final class CategoryItem extends BeanItem<Category> {
    private static final long serialVersionUID = 1L;

    public CategoryItem(final Category category) {
        super(category);
    }

}
