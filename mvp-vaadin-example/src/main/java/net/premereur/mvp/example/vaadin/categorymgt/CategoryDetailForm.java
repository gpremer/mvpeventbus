package net.premereur.mvp.example.vaadin.categorymgt;

import java.util.Arrays;

import net.premereur.mvp.example.vaadin.data.CategoryItem;

import com.vaadin.ui.Form;

public final class CategoryDetailForm extends Form {

	private static final long serialVersionUID = 1L;

	public CategoryDetailForm() {
		setCaption("Category mgt");
	}

	public void setCategory(final CategoryItem categoryItem) {
		setItemDataSource(categoryItem);
		setVisibleItemProperties(Arrays.asList("name"));
	}
}
