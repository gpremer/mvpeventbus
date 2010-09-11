package net.premereur.mvp.example.swing.presenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JComponent;

import net.premereur.mvp.example.swing.eventbus.DemoEventBus;
import net.premereur.mvp.example.swing.view.ApplicationFrame;

import org.junit.Before;
import org.junit.Test;

public class ApplicationPresenterTest {

	private ApplicationPresenter presenter;
	private ApplicationFrame view;
	private DemoEventBus eventBus;

	@Before
	public void setUpPresenterWithMockView() {
		view = mock(ApplicationFrame.class);
		eventBus = mock(DemoEventBus.class);
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
		verify(eventBus).categoryListActivated();
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
