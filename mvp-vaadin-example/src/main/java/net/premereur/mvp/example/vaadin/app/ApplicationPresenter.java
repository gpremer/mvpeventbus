package net.premereur.mvp.example.vaadin.app;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.guice.BasePresenter;

import com.google.inject.Inject;
import com.vaadin.Application;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Window;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Window.CloseEvent;

public final class ApplicationPresenter extends
		BasePresenter<ApplicationWindow, ApplicationBus> {

	@Inject
	public ApplicationPresenter(final EventBus eventBus,
			final ApplicationWindow view) {
		super((ApplicationBus) eventBus, view);
	}

	public void onInit(final Application application) {
		ApplicationWindow main = getView();
		application.setMainWindow(main);
		addAppListener(application, main);
		addMenu(main);
	}

	@SuppressWarnings("serial")
	private void addAppListener(final Application application,
			final ApplicationWindow main) {
		main.addListener(new Window.CloseListener() {

			@Override
			public void windowClose(CloseEvent e) {
				getEventBus().close(application);
			}
		});
	}

	@SuppressWarnings("serial")
	private void addMenu(final ApplicationWindow main) {
		final MenuBar menuBar = new MenuBar();
		main.addComponent(menuBar);
		final MenuItem appItem = menuBar.addItem("Application", null);
		appItem.addItem("Categories", new Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				// TODO send to event bus
			}
		});
		appItem.addItem("Products", new Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				// TODO send to event bus
			}
		});
	}

	public void onClose(final Application application) {
		application.close();
	}
}
