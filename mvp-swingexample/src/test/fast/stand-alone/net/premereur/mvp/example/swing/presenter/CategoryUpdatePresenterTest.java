package net.premereur.mvp.example.swing.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.swing.JPanel;

import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.swing.eventbus.DemoEventBus;
import net.premereur.mvp.example.swing.view.CategoryUpdaterPanel;

import org.junit.Before;
import org.junit.Test;

public class CategoryUpdatePresenterTest {

	private CategoryUpdatePresenter presenter;
	private CategoryUpdaterPanel view;
	private DemoEventBus eventBus;
	private CategoryRepository repository;

	@Before
	public void setUpPresenterWithMockView() {
		view = mock(CategoryUpdaterPanel.class);
		eventBus = mock(DemoEventBus.class);
		repository = mock (CategoryRepository.class);
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
		verify(eventBus).setCenterComponent(view);
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
		verify(eventBus).setFeedback(any(String.class));
	}

	@Test
	public void shouldSendCategoryChangedEventWhenCategoryUpdated() throws Exception {
		Category category = new Category("cat");
		presenter.saveClicked(category);
		verify(eventBus).categoryChanged(category);
	}
	
	@Test
	public void shouldClearMainPanelWhenCancelled() throws Exception {
		Category category = new Category("cat");
		presenter.saveClicked(category);
		verify(eventBus).setCenterComponent(any(JPanel.class));
	}

	@Test
	public void shouldClearMainPanelWhenCategoryUpdated() throws Exception {
		presenter.cancelClicked();
		verify(eventBus).setCenterComponent(any(JPanel.class));
	}

	@Test
	public void shouldRegisterItselfAsUpdateListener() throws Exception {
		verify(view).setOperationButtonListener(presenter);
	}
	
	@Test
	public void shouldRegisterItselfAsCancelListener() throws Exception {
		verify(view).setCancelButtonListener(presenter);
	}
	
}
