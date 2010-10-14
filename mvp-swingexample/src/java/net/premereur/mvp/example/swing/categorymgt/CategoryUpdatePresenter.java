package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.swing.application.ApplicationBus;

import com.google.inject.Inject;

public class CategoryUpdatePresenter extends SingleCategoryPresenterBase<CategoryUpdaterPanel> {		
	
	@Inject
	public CategoryUpdatePresenter(CategoryMgtBus eventBus, CategoryUpdaterPanel view, CategoryRepository repository) {
		super(eventBus, view, repository);
	}

	public void onCategorySelected(Category selectedCategory) {
		CategoryUpdaterPanel view = getView();
		view.bind(selectedCategory);
		getEventBus(ApplicationBus.class).setCenterComponent(view);
	}
		
	public void saveClicked(Category category) {
		getRepository().save(category);
		getEventBus().defaultCategoryPanelActivated();
		getEventBus().categoryChanged(category);
		getEventBus(ApplicationBus.class).setFeedback("Category updated");
	}

}
