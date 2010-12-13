package net.premereur.mvp.example.vaadin.app;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.Application;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.guice.BasePresenter;

@Singleton
public final class ApplicationPresenter extends BasePresenter<ApplicationWindow, ApplicationBus> {

	@Inject
	public ApplicationPresenter(final EventBus eventBus, final ApplicationWindow view) {
		super((ApplicationBus) eventBus, view);
	}


	@SuppressWarnings("serial")
	public void onInit(final Application application) {
		ApplicationWindow main = getView();
		application.setMainWindow(main);
		main.addListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				getEventBus().close(application);
			}
		});
	}
	
	public void onClose(final Application application) {
		application.close();
	}
}
