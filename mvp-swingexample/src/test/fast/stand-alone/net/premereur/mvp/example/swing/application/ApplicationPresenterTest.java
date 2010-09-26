package net.premereur.mvp.example.swing.application;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.awt.Container;

import javax.swing.JComponent;

import net.premereur.mvp.example.swing.application.ApplicationBus;
import net.premereur.mvp.example.swing.application.ApplicationFrame;
import net.premereur.mvp.example.swing.application.ApplicationPresenter;
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
		presenter = new ApplicationPresenter();
		presenter.setView(view);
		presenter.setEventBus(eventBus);
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
		verify(catBus).categoryListActivated();
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

}
