package net.premereur.mvp.example.vaadin.app;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.Application;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.guice.BasePresenter;

@Singleton
public class ApplicationPresenter extends BasePresenter<ApplicationWindow, ApplicationBus> {

	@Inject
	public ApplicationPresenter(EventBus eventBus, ApplicationWindow view) {
		super((ApplicationBus) eventBus, view);
	}


	public void onInit(Application application) {
		application.setMainWindow(getView());
	}
}
