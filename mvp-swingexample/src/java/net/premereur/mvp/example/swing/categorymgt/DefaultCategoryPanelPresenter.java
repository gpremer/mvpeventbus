package net.premereur.mvp.example.swing.categorymgt;

import com.google.inject.Inject;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.swing.application.ApplicationBus;


public class DefaultCategoryPanelPresenter extends BasePresenter<DefaultCategoryPanel, CategoryMgtBus>{

	@Inject
	public DefaultCategoryPanelPresenter(EventBus eventBus, DefaultCategoryPanel view) {
		super((CategoryMgtBus) eventBus, view);
	}

	public void onDefaultCategoryPanelActivated() {
		getView().setCreateButtonListener(this);
		getEventBus(ApplicationBus.class).setCenterComponent(getView());
	}

	public void createNewCategory() {
		getEventBus().activateCategoryCreation();
	}
	
}
