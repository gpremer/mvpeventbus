package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.core.BasePresenter;
import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.swing.application.ApplicationBus;

@UsesView(DefaultCategoryPanel.class)
public class DefaultCategoryPanelPresenter extends BasePresenter<DefaultCategoryPanel, CategoryMgtBus>{

	public void onDefaultCategoryPanelActivated() {
		getView().setCreateButtonListener(this);
		getEventBus(ApplicationBus.class).setCenterComponent(getView());
	}

	public void createNewCategory() {
		getEventBus().activateCategoryCreation();
	}
	
}
