package net.premereur.mvp.example.swing.presenter;

import javax.swing.JPanel;

import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.view.CategoryUpdaterPanel;

@UsesView(CategoryUpdaterPanel.class)
public class CategoryUpdatePresenter extends CategoryPresenterBase<CategoryUpdaterPanel> {		
	
	@Override
	public void setView(CategoryUpdaterPanel view) {
		super.setView(view);
		getView().setOperationButtonListener(this);
		getView().setCancelButtonListener(this);
	}
	
	public void onCategorySelected(Category selectedCategory) {
		CategoryUpdaterPanel view = getView();
		view.bind(selectedCategory);
		getEventBus().setCenterComponent(view);
	}
		
	public void saveClicked(Category category) {
		getRepository().save(category);
		getEventBus().setCenterComponent(new JPanel());
		getEventBus().categoryChanged(category);
		getEventBus().setFeedback("Category updated");
	}

	public void cancelClicked() {
		getEventBus().setCenterComponent(new JPanel());		
	}

}
