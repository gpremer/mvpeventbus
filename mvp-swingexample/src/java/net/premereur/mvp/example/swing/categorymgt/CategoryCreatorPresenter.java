package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.application.ApplicationBus;

@UsesView(CategoryCreatorPanel.class)
public class CategoryCreatorPresenter extends SingleCategoryPresenterBase<CategoryCreatorPanel> {

	public void onActivateCategoryCreation()  {
		CategoryCreatorPanel view = getView();
		view.bind(new Category(""));
		getEventBus(ApplicationBus.class).setCenterComponent(view);
	}
	
	public void saveClicked(Category category) {
		getRepository().save(category);
		getEventBus().defaultCategoryPanelActivated();
		getEventBus().categoryAdded(category);
		getEventBus(ApplicationBus.class).setFeedback("Category saved");
	}

}
