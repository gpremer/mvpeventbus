package net.premereur.mvp.example.vaadin.categorymgt.data;

import net.premereur.mvp.example.domain.model.Category;

import com.vaadin.data.util.BeanItem;

public class CategoryItem extends BeanItem<Category> {
	private static final long serialVersionUID = 1L;

	public CategoryItem(final Category category) {
		super(category);
	}

}
