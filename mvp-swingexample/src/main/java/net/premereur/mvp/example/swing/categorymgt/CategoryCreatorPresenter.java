package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.swing.application.ApplicationBus;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CategoryCreatorPresenter extends SingleCategoryPresenterBase<CategoryCreatorPanel> {

    @Inject
    public CategoryCreatorPresenter(final CategoryMgtBus eventBus, final CategoryCreatorPanel view, final CategoryRepository repository) {
        super(eventBus, view, repository);
    }

    public void onActivateCategoryCreation() {
        CategoryCreatorPanel view = getView();
        view.bind(new Category(""));
        getEventBus(ApplicationBus.class).setCenterComponent(view);
    }

    public void saveClicked(final Category category) {
        getRepository().save(category);
        getEventBus().defaultCategoryPanelActivated();
        getEventBus().categoryAdded(category);
        getEventBus(ApplicationBus.class).setFeedback("Category saved");
    }

}
