package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.example.domain.model.Category;

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
