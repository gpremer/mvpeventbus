package net.premereur.mvp.example.vaadin.app;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import net.premereur.mvp.example.vaadin.SampleApplication;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.ui.MenuBar;

public class ApplicationPresenterTest {

	private ApplicationPresenter presenter;
	private ApplicationWindow view;
	private SampleApplication application;
	private ApplicationBus eventBus;

	@Before
	public void setUpPresenterWithMockView() throws Exception {
		view = mock(ApplicationWindow.class);
		eventBus = mock(ApplicationBus.class);
		application = mock(SampleApplication.class);
		presenter = new ApplicationPresenter(eventBus, view);
	}

	@Test
	public void shouldSetViewAsMainWindow() throws Exception {
		presenter.onInit(application);
		verify(application).setMainWindow(view);
	}

	@Test
	public void shouldAddMenuToMainWindow() throws Exception {
		presenter.onInit(application);
		verify(view).addComponent(any(MenuBar.class));
	}

	@Test
	public void shouldForwardCloseEventToApplication() throws Exception {
		presenter.onClose(application);
		verify(application).close();
	}

}
