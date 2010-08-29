package net.premereur.mvp.example.swing.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.swing.JFrame;

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
		verify(eventBus).categoryListActivated(any(JFrame.class));
	}
}
