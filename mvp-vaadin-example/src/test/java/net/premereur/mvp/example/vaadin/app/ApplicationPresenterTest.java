package net.premereur.mvp.example.vaadin.app;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import net.premereur.mvp.example.vaadin.MockTestBase;
import net.premereur.mvp.example.vaadin.SampleApplication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.vaadin.ui.Window;

public class ApplicationPresenterTest extends MockTestBase {

	private ApplicationPresenter presenter;
	@Mock
	private ApplicationWindow view;
	@Mock
	private SampleApplication application;
	@Mock
	private ApplicationBus eventBus;

	@Before
	public void setUpPresenterWithMockView() throws Exception {
		presenter = new ApplicationPresenter(eventBus, view);
	}

	@Test
	public void shouldSetViewAsMainWindow() throws Exception {
		presenter.onInit(application);
		verify(application).setMainWindow(view);
	}

	@Test
	public void shouldAddACloseListenerToWindow() throws Exception {
		presenter.onInit(application);
		verify(view).addListener(any(Window.CloseListener.class));
	}

	@Test
	public void shouldForwardCloseEventToApplication() throws Exception {
		presenter.onClose(application);
		verify(application).close();
	}

}
