package net.premereur.mvp.example.vaadin.categorymgt;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.Arrays;
import java.util.List;

import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.vaadin.MockTestBase;
import net.premereur.mvp.example.vaadin.app.ApplicationBus;
import net.premereur.mvp.example.vaadin.app.ApplicationWindow;
import net.premereur.mvp.example.vaadin.data.CategoryItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.vaadin.data.Container;

public class CategoryMgtPresenterTest extends MockTestBase {

    private CategoryMgtPresenter presenter;
    private CategoryMgtBus eventBus;
    @Mock
    private CategoryMgtView view;
    @Mock
    private CategoryRepository repo;
    @Mock
    private ApplicationWindow appWindow;

    @Before
    public void setUpPresenterWithMockView() throws Exception {
        eventBus = mock(CategoryMgtBus.class, withSettings().extraInterfaces(ApplicationBus.class));
        this.presenter = new CategoryMgtPresenter(this.eventBus, this.view, this.repo);
        final List<Category> categories = Arrays.asList(new Category("cat1"));
        when(this.repo.allCategories()).thenReturn(categories);
    }

    @Test
    public void shouldAddViewToApplicationWindowWhenActivated() throws Exception {
        this.presenter.onActivate(this.appWindow);
        verify((ApplicationBus)this.eventBus).setWorkPane(this.view);
    }

    @Test
    public void shouldProvideViewWithAllCategoriesWhenActivated() throws Exception {
        this.presenter.onActivate(this.appWindow);
        verify(this.view).setCategories(any(Container.class));
    }

    @Test
    public void shouldBindItselfAsSelectListenerWhenActivated() throws Exception {
        this.presenter.onActivate(this.appWindow);
        verify(this.view).forwardCategorySelection(this.presenter);
    }

    @Test
    public void shouldAskViewToEditCategoryWhenCategoryIsSelected() throws Exception {
        final Category cat = new Category("cat1");
        this.presenter.selectCategory(cat);
        // A better matcher would be nice!
        verify(this.view).edit(any(CategoryItem.class));
    }

    @Test
    public void shouldAskRepositoryToStoreCategoryWhenCategorySaveButtonIsClicked() throws Exception {
        final Category category = new Category("cat1");
        final CategoryItem categoryItem = new CategoryItem(category);
        this.presenter.saveCategory(categoryItem);
        verify(this.repo).save(category);
    }

    @Test
    public void shouldTellEventBusThatCategoryWasSavedWhenCategorySaveButtonIsClicked() throws Exception {
        final Category category = new Category("cat1");
        final CategoryItem categoryItem = new CategoryItem(category);
        this.presenter.saveCategory(categoryItem);
        verify(this.eventBus).changedCategory(category);
    }

    @Test
    public void shouldAskViewToRefreshTableWhenCategoryWasSaved() throws Exception {
        final Category category = new Category("cat1");
        this.presenter.onChangedCategory(category);
        verify(this.view).refreshCategories(category);
    }

    @Test
    public void shouldAskApplicationToShowMessageWhenCategoryWasSaved() throws Exception {
        final Category category = new Category("cat1");
        this.presenter.onChangedCategory(category);
        verify((ApplicationBus) this.eventBus).showMessage(any(String.class));
    }
}
