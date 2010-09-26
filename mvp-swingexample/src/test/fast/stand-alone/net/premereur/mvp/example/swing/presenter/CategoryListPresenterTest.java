package net.premereur.mvp.example.swing.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import java.util.List;

import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.eventbus.ApplicationBus;
import net.premereur.mvp.example.swing.eventbus.CategoryMgtBus;
import net.premereur.mvp.example.swing.view.CategoryList;

import org.junit.Before;
import org.junit.Test;

public class CategoryListPresenterTest {

	private CategoryListPresenter presenter;
	private CategoryList view;
	private CategoryMgtBus eventBus;
	private ApplicationBus appBus;

	@Before
	public void setUpPresenterWithMockView() {
		view = mock(CategoryList.class);
		eventBus = mock(CategoryMgtBus.class, withSettings().extraInterfaces(ApplicationBus.class));
		appBus = (ApplicationBus) eventBus;
		presenter = new CategoryListPresenter();
		presenter.setView(view);
		presenter.setEventBus(eventBus);
	}

	@Test
	public void shouldMakeTheViewVisible() throws Exception {
		presenter.onCategoryListActivated();
		verify(appBus).setLeftComponent(view);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldBindViewData() throws Exception {
		presenter.onCategoryListActivated();
		verify(view).bind(any(List.class));
	}

	@Test
	public void shouldSetItselfAsListListener() throws Exception {
		verify(view).addSelectionListener(presenter);
	}

	@Test
	public void shouldSendSelectionEventWhenCategoryIsSelected() throws Exception {		
		Category category = new Category("cat");
		presenter.categorySelected(category);
		verify(eventBus).categorySelected(category);
	}
	
	@Test
	public void shouldUpdateListWhenCategoryIsChanged() throws Exception {
		Category category = new Category("cat");
		presenter.onCategoryChanged(category);
		verify(view).refreshList();
	}
	
}
