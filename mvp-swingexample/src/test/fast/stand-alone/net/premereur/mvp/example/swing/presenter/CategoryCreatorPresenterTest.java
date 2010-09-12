package net.premereur.mvp.example.swing.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.swing.eventbus.DemoEventBus;
import net.premereur.mvp.example.swing.view.CategoryCreatorPanel;

import org.junit.Before;
import org.junit.Test;

public class CategoryCreatorPresenterTest {

	private CategoryCreatorPresenter presenter;
	private CategoryCreatorPanel view;
	private DemoEventBus eventBus;
	private CategoryRepository repository;

	@Before
	public void setUpPresenterWithMockView() {
		view = mock(CategoryCreatorPanel.class);
		eventBus = mock(DemoEventBus.class);
		repository = mock(CategoryRepository.class);
		presenter = new CategoryCreatorPresenter();
		presenter.setView(view);
		presenter.setEventBus(eventBus);
		presenter.setRepository(repository);
	}

	@Test
	public void shouldBindCategoryToViewWhenCategorySelected() throws Exception {
		presenter.onActivateCategoryCreation();
		verify(view).bind(any(Category.class));
	}

	@Test
	public void shouldSendEventToAttachViewWhenCategorySelected() throws Exception {
		presenter.onActivateCategoryCreation();
		verify(eventBus).setCenterComponent(view);
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
		verify(eventBus).setFeedback(any(String.class));
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
		verify(eventBus).defaultCategoryPanelActivated();
	}

	@Test
	public void shouldClearMainPanelWhenCancelled() throws Exception {
		presenter.cancelClicked();
		verify(eventBus).defaultCategoryPanelActivated();
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
