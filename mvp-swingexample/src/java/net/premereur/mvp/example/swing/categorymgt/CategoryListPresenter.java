package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.swing.application.ApplicationBus;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CategoryListPresenter extends CategoryPresenterBase<CategoryList> {
	
	@Inject
	public CategoryListPresenter(EventBus eventBus, CategoryList view, CategoryRepository repository) {
		super((CategoryMgtBus) eventBus, view, repository);
		view.addSelectionListener(this);
	}

	public void onCategoryMgtActivated() {
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
