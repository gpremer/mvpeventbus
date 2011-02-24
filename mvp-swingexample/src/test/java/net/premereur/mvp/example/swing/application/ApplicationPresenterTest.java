package net.premereur.mvp.example.swing.application;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.awt.Container;

import javax.swing.JComponent;

import net.premereur.mvp.example.support.ClickHandler;
import net.premereur.mvp.example.swing.categorymgt.CategoryMgtBus;

import org.junit.Before;
import org.junit.Test;

public class ApplicationPresenterTest {

	private ApplicationPresenter presenter;
	private ApplicationFrame view;
	private ApplicationBus eventBus;
	private CategoryMgtBus catBus;

	@Before
	public void setUpPresenterWithMockView() {
		view = mock(ApplicationFrame.class);
		eventBus = mock(ApplicationBus.class, withSettings().extraInterfaces(CategoryMgtBus.class));
		catBus = (CategoryMgtBus) eventBus;
		presenter = new ApplicationPresenter(eventBus, catBus, view);
	}

	@Test
	public void shouldMakeTheApplicationFrameVisible() throws Exception {
		presenter.onApplicationStarted();
		verify(view).pack();
		verify(view).setVisible(true);
	}

	@Test
	public void shouldSendEventToRequestCategoryListActivation() throws Exception {
		presenter.onApplicationStarted();
		verify(catBus).categoryMgtActivated();
	}

	@Test
	public void shouldAddComponentToLeftOfContentPaneWhenLeftEvent() throws Exception {
		JComponent component = mock(JComponent.class);
		Container contentPane = mock(Container.class);
		when(view.getContentPane()).thenReturn(contentPane);
		presenter.onSetLeftComponent(component);
		verify(view).setLeftComponent(component);
	}

	@Test
	public void shouldAddComponentToCenterOfContentPaneWhenCenterEvent() throws Exception {
		JComponent component = mock(JComponent.class);
		Container contentPane = mock(Container.class);
		when(view.getContentPane()).thenReturn(contentPane);
		presenter.onSetCenterComponent(component);
		verify(view).setCentralComponent(component);
	}

	@Test
	public void shouldSetFeedbackText() throws Exception {
		presenter.onSetFeedback("feedback");
		verify(view).setFeedback("feedback");
	}

	@Test
	public void shouldBindToExitMenu() {
		presenter.onApplicationStarted();
		verify(view).setExitListener(any(ClickHandler.class));
	}

	@Test
	public void shouldBindToCategoryMenu() {
		presenter.onApplicationStarted();
		verify(view).setCategoryListener(any(ClickHandler.class));
	}

	@Test
	public void shouldBindToProductMenu() {
		presenter.onApplicationStarted();
		verify(view).setProductListener(any(ClickHandler.class));
	}
	
	@Test
	public void shouldClearLeftComponentWhenClearEvent() {
		presenter.onClearScreen();
		verify(view).clearLeftComponent();		
	}

	@Test
	public void shouldClearCentralComponentWhenClearEvent() {
		presenter.onClearScreen();
		verify(view).clearCentralComponent();		
	}
}
