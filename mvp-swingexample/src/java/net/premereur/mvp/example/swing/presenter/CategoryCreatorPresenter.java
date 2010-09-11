package net.premereur.mvp.example.swing.presenter;

import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.view.CategoryCreatorPanel;

@UsesView(CategoryCreatorPanel.class)
public class CategoryCreatorPresenter extends SingleCategoryPresenterBase<CategoryCreatorPanel> {

	public void onActivateCategoryCreation()  {
		CategoryCreatorPanel view = getView();
		view.bind(new Category(""));
		getEventBus().setCenterComponent(view);
	}
	
	public void saveClicked(Category category) {
		getRepository().save(category);
		getEventBus().defaultCategoryPanelActivated();
		getEventBus().categoryAdded(category);
		getEventBus().setFeedback("Category saved");
	}

}