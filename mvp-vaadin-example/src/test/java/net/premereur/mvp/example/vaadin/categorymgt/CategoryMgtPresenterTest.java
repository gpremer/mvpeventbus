package net.premereur.mvp.example.vaadin.categorymgt;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.vaadin.MockTestBase;
import net.premereur.mvp.example.vaadin.app.ApplicationWindow;
import net.premereur.mvp.example.vaadin.data.CategoryItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.vaadin.data.Container;

public class CategoryMgtPresenterTest extends MockTestBase {

    private CategoryMgtPresenter presenter;
    @Mock
    private CategoryMgtView view;
    @Mock
    private CategoryMgtBus eventBus;
    @Mock
    private CategoryRepository repo;
    @Mock
    private ApplicationWindow appWindow;

    @Before
    public void setUpPresenterWithMockView() throws Exception {
        presenter = new CategoryMgtPresenter(eventBus, view, repo);
        List<Category> categories = Arrays.asList(new Category("cat1"));
        when(repo.allCategories()).thenReturn(categories);
    }

    @Test
    public void shouldAddViewToApplicationWindow() throws Exception {
        presenter.onActivate(appWindow);
        verify(appWindow).setWorkPane(view);
    }

    @Test
    public void shouldProvideViewWithAllCategories() throws Exception {
        presenter.onActivate(appWindow);
        verify(view).setCategories(any(Container.class));
    }

    @Test
    public void shouldBindItselfAsSelectListener() throws Exception {
        presenter.onActivate(appWindow);
        verify(view).forwardCategorySelection(presenter);
    }

    @Test
    public void shouldAskViewToEditCategoryWhenCategoryIsSelected() throws Exception {
        final Category cat = new Category("cat1");
        presenter.selectCategory(cat);
        // A better matcher would be nice!
        verify(view).edit(any(CategoryItem.class));
    }
}
