package net.premereur.mvp.example.swing.categorymgt;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.swing.application.ApplicationBus;
import net.premereur.mvp.example.swing.categorymgt.CategoryMgtBus;
import net.premereur.mvp.example.swing.categorymgt.CategoryUpdatePresenter;
import net.premereur.mvp.example.swing.categorymgt.CategoryUpdaterPanel;

import org.junit.Before;
import org.junit.Test;

public class CategoryUpdatePresenterTest {

	private CategoryUpdatePresenter presenter;
	private CategoryUpdaterPanel view;
	private CategoryMgtBus eventBus;
	private ApplicationBus appBus;
	private CategoryRepository repository;

	@Before
	public void setUpPresenterWithMockView() {
		view = mock(CategoryUpdaterPanel.class);
		eventBus = mock(CategoryMgtBus.class, withSettings().extraInterfaces(ApplicationBus.class));
		appBus = (ApplicationBus) eventBus;
		repository = mock(CategoryRepository.class);
		presenter = new CategoryUpdatePresenter();
		presenter.setView(view);
		presenter.setEventBus(eventBus);
		presenter.setRepository(repository);
	}

	@Test
	public void shouldBindCategoryToViewWhenCategorySelected() throws Exception {
		Category category = new Category("cat");
		presenter.onCategorySelected(category);
		verify(view).bind(category);
	}

	@Test
	public void shouldSendEventToAttachViewWhenCategorySelected() throws Exception {
		Category category = new Category("cat");
		presenter.onCategorySelected(category);
		verify(appBus).setCenterComponent(view);
	}

	@Test
	public void shouldSaveCategoryWhenCategoryUpdated() throws Exception {
		Category category = new Category("cat");
		presenter.saveClicked(category);
		verify(repository).save(category);
	}

	@Test
	public void shouldSetFeedbackWhenCategoryUpdated() throws Exception {
		Category category = new Category("cat");
		presenter.saveClicked(category);
		verify(appBus).setFeedback(any(String.class));
	}

	@Test
	public void shouldSendCategoryChangedEventWhenCategoryUpdated() throws Exception {
		Category category = new Category("cat");
		presenter.saveClicked(category);
		verify(eventBus).categoryChanged(category);
	}

	@Test
	public void shouldClearMainPanelWhenCategoryUpdated() throws Exception {
		Category category = new Category("cat");
		presenter.saveClicked(category);
		verify(eventBus).defaultCategoryPanelActivated();
	}

	@Test
	public void shouldClearMainPanelWhenCancelled() throws Exception {
		presenter.cancelClicked();
		verify(eventBus).defaultCategoryPanelActivated();
	}

	@Test
	public void shouldRegisterItselfAsUpdateListener() throws Exception {
		verify(view).setSaveButtonListener(presenter);
	}

	@Test
	public void shouldRegisterItselfAsCancelListener() throws Exception {
		verify(view).setCancelButtonListener(presenter);
	}

}
