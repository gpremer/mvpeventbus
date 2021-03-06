package net.premereur.mvp.example.swing.categorymgt;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.swing.application.ApplicationBus;
import net.premereur.mvp.example.swing.categorymgt.CategoryCreatorPanel;
import net.premereur.mvp.example.swing.categorymgt.CategoryCreatorPresenter;
import net.premereur.mvp.example.swing.categorymgt.CategoryMgtBus;

import org.junit.Before;
import org.junit.Test;

public class CategoryCreatorPresenterTest {

	private CategoryCreatorPresenter presenter;
	private CategoryCreatorPanel view;
	private CategoryMgtBus eventBus;
	private ApplicationBus appBus;
	private CategoryRepository repository;

	@Before
	public void setUpPresenterWithMockView() {
		view = mock(CategoryCreatorPanel.class);
		eventBus = mock(CategoryMgtBus.class, withSettings().extraInterfaces(ApplicationBus.class));
		appBus = (ApplicationBus) eventBus;
		repository = mock(CategoryRepository.class);
		presenter = new CategoryCreatorPresenter(eventBus, view, repository);
	}

	@Test
	public void shouldBindCategoryToViewWhenCategorySelected() throws Exception {
		presenter.onActivateCategoryCreation();
		verify(view).bind(any(Category.class));
	}

	@Test
	public void shouldSendEventToAttachViewWhenCategorySelected() throws Exception {
		presenter.onActivateCategoryCreation();
		verify(appBus).setCenterComponent(view);
	}

	@Test
	public void shouldSaveCategoryWhenCategoryCreated() throws Exception {
		Category category = new Category("cat");
		presenter.saveClicked(category);
		verify(repository).save(category);
	}

	@Test
	public void shouldSetFeedbackWhenCategoryCreatod() throws Exception {
		Category category = new Category("cat");
		presenter.saveClicked(category);
		verify(appBus).setFeedback(any(String.class));
	}

	@Test
	public void shouldSendCategoryAddedEventWhenCategoryCreated() throws Exception {
		Category category = new Category("cat");
		presenter.saveClicked(category);
		verify(eventBus).categoryAdded(category);
	}

	@Test
	public void shouldClearMainPanelWhenCategoryCreated() throws Exception {
		Category category = new Category("cat");
		presenter.saveClicked(category);
		verify(eventBus).noCategorySelected();
	}

	@Test
	public void shouldClearMainPanelWhenCancelled() throws Exception {
		presenter.cancelClicked();
		verify(eventBus).noCategorySelected();
	}

	@Test
	public void shouldRegisterItselfAsCreatorListener() throws Exception {
		verify(view).setSaveButtonListener(presenter);
	}

	@Test
	public void shouldRegisterItselfAsCancelListener() throws Exception {
		verify(view).setCancelButtonListener(presenter);
	}

}
