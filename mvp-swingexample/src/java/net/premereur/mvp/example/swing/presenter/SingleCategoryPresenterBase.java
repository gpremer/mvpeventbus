package net.premereur.mvp.example.swing.presenter;

import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.view.CategoryPanelBase;

public abstract class SingleCategoryPresenterBase<V extends CategoryPanelBase> extends CategoryPresenterBase<V> {

	@Override
	public void setView(V view) {
		super.setView(view);
		view.setSaveButtonListener(this);
		view.setCancelButtonListener(this);
	}

	public void cancelClicked() {
		getEventBus().defaultCategoryPanelActivated();		
	}
	
	public abstract void saveClicked(Category category);
}
