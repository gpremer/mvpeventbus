package net.premereur.mvp.example.swing.presenter;

import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.eventbus.ApplicationBus;
import net.premereur.mvp.example.swing.view.CategoryList;

@UsesView(CategoryList.class)
public class CategoryListPresenter extends CategoryPresenterBase<CategoryList> {

	@Override
	public void setView(CategoryList view) {	
		super.setView(view);
		view.addSelectionListener(this);
	}
	
	public void onCategoryListActivated() {
		final CategoryList categoryList = getView();
		categoryList.bind(getRepository().allCategories());
		getEventBus(ApplicationBus.class).setLeftComponent(categoryList);
		getEventBus().defaultCategoryPanelActivated();
	}
	
	public void onCategoryChanged(final Category category) {
		getView().refreshList();
	}
	
	public void onCategoryAdded(Category category) {
		getView().bind(getRepository().allCategories());
	}

	public void categorySelected(Category selectedCategory) {
		getEventBus().categorySelected(selectedCategory);		
	}

}
