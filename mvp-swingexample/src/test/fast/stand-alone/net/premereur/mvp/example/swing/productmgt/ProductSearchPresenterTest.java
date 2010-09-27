package net.premereur.mvp.example.swing.productmgt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import net.premereur.mvp.example.swing.application.ApplicationBus;

import org.junit.Before;
import org.junit.Test;

public class ProductSearchPresenterTest {

	private ProductSearchPresenter presenter;
	private ProductSearchPanel view;
	private ProductMgtBus eventBus;
	private ApplicationBus appBus;

	@Before
	public void setUpPresenterWithMockView() {
		view = mock(ProductSearchPanel.class);
		eventBus = mock(ProductMgtBus.class, withSettings().extraInterfaces(ApplicationBus.class));
		appBus = (ApplicationBus) eventBus;
		presenter = new ProductSearchPresenter();
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
}
