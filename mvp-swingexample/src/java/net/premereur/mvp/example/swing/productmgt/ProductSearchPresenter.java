package net.premereur.mvp.example.swing.productmgt;

import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.swing.application.ApplicationBus;

@UsesView(ProductSearchPanel.class)
public class ProductSearchPresenter extends ProductPresenterBase<ProductSearchPanel> {

	public void onProductMgtActivated() {
		getEventBus(ApplicationBus.class).clearScreen();
		getEventBus(ApplicationBus.class).setCenterComponent(getView());
	}
}
