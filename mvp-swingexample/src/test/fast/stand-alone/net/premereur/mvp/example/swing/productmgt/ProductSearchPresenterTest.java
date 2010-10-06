package net.premereur.mvp.example.swing.productmgt;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.Arrays;
import java.util.List;

import net.premereur.mvp.example.domain.model.Product;
import net.premereur.mvp.example.domain.repository.ProductRepository;
import net.premereur.mvp.example.swing.application.ApplicationBus;
import net.premereur.mvp.example.test.IsEmptyList;

import org.junit.Before;
import org.junit.Test;

public class ProductSearchPresenterTest {

	private ProductSearchPresenter presenter;
	private ProductSearchPanel view;
	private ProductMgtBus eventBus;
	private ApplicationBus appBus;
	private ProductRepository repo;

	@Before
	public void setUpPresenterWithMockView() {
		view = mock(ProductSearchPanel.class);
		eventBus = mock(ProductMgtBus.class, withSettings().extraInterfaces(ApplicationBus.class));
		appBus = (ApplicationBus) eventBus;
		repo = mock(ProductRepository.class);
		presenter = new ProductSearchPresenter(repo);
		presenter.setView(view);
		presenter.setEventBus(eventBus);
	}
	
	@Test
	public void shouldClearApplicationFrameWhenActivated() throws Exception {
		presenter.onProductMgtActivated();
		verify(appBus).clearScreen();
	} 

	@Test
	public void shouldAddSearchPanelWhenActivated() throws Exception {
		presenter.onProductMgtActivated();
		verify(appBus).setCenterComponent(view);
	} 

	@Test
	public void shouldSetItselfAsNameSearchListenerWhenActivated() throws Exception {
		presenter.onProductMgtActivated();
		verify(view).setNameChangeListener(presenter);
	} 

	@Test
	public void shouldAskRepositoryForProductsWhenNameChanges() {
		presenter.searchForName("pro");
		verify(repo).searchProducts("pro");
	}

	@Test
	public void shouldUseProductsFromRepositoryWhenNameChanges() {
		List<Product> searchResults = Arrays.asList(new Product("prod1"));
		when(repo.searchProducts("pro")).thenReturn(searchResults);
		presenter.searchForName("pro");
		verify(view).setProducts(searchResults);
	}

	@Test
	public void shouldNotAskRepositoryForProductsWhenNameIsShort() {
		presenter.searchForName("p");
		verifyZeroInteractions(repo);
	}

	@Test
	public void shouldReturnEmptyListWhenNameIsShort() {
		presenter.searchForName("p");
		verify(view).setProducts(emptyList());
	}

	private <T extends Product> List<T> emptyList() {
		return argThat(new IsEmptyList<T>());
	}

}
