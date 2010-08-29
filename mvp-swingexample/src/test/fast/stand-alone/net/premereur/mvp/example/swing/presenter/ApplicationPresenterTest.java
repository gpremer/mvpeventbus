package net.premereur.mvp.example.swing.presenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import net.premereur.mvp.example.swing.view.ApplicationFrame;

import org.junit.Before;
import org.junit.Test;

public class ApplicationPresenterTest {

	private ApplicationPresenter presenter;
	private ApplicationFrame view;

	@Before
	public void setUpPresenterWithMockView() {
		view = mock(ApplicationFrame.class);
		presenter = new ApplicationPresenter();
		presenter.setView(view);
	}
	
	@Test
	public void shouldMakeTheApplicationFrameVisible() throws Exception {
		presenter.onApplicationStarted();
		verify(view).pack();
		verify(view).setVisible(true);
	}
}
