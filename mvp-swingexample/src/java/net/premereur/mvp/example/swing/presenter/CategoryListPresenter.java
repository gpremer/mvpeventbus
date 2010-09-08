package net.premereur.mvp.example.swing.presenter;

import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.view.CategoryList;

@UsesView(CategoryList.class)
public class CategoryListPresenter extends CategoryPresenterBase<CategoryList> {

	public void onCategoryListActivated() {
		final CategoryList categoryList = getView();
		categoryList.bind(getRepository().allCategories());
		getEventBus().setLeftComponent(categoryList);
	}
	
	public void onCategoryChanged(final Category category) {
		getView().refreshList();
	}
}
