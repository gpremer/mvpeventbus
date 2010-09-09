package net.premereur.mvp.example.swing.presenter;

import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.view.CategoryUpdaterPanel;

@UsesView(CategoryUpdaterPanel.class)
public class CategoryUpdatePresenter extends CategoryPresenterBase<CategoryUpdaterPanel> {		
	
	public void onCategorySelected(Category selectedCategory) {
		CategoryUpdaterPanel view = getView();
		view.bind(selectedCategory);
		getEventBus().setCenterComponent(view);
	}
		
	public void onUpdateCategory(Category category) {
		getRepository().save(category);
		getEventBus().setFeedback("Category updated");
		getEventBus().categoryChanged(category);
	}
}
