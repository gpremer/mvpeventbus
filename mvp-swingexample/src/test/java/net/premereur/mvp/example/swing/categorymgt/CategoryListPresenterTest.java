package net.premereur.mvp.example.swing.categorymgt;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import java.util.List;

import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.swing.application.ApplicationBus;
import net.premereur.mvp.example.swing.categorymgt.CategoryList;
import net.premereur.mvp.example.swing.categorymgt.CategoryListPresenter;
import net.premereur.mvp.example.swing.categorymgt.CategoryMgtBus;

import org.junit.Before;
import org.junit.Test;

public class CategoryListPresenterTest {

	private CategoryListPresenter presenter;
	private CategoryList view;
	private CategoryMgtBus eventBus;
	private ApplicationBus appBus;
	private CategoryRepository repository;

	@Before
	public void setUpPresenterWithMockView() {
		view = mock(CategoryList.class);
		repository = mock(CategoryRepository.class);
		eventBus = mock(CategoryMgtBus.class, withSettings().extraInterfaces(ApplicationBus.class));
		appBus = (ApplicationBus) eventBus;
		presenter = new CategoryListPresenter(eventBus, view, repository);
	}

	@Test
	public void shouldMakeTheViewVisible() throws Exception {
		presenter.onCategoryMgtActivated();
		verify(appBus).setLeftComponent(view);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldBindViewData() throws Exception {
		presenter.onCategoryMgtActivated();
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
