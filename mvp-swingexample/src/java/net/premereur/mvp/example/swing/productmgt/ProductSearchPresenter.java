package net.premereur.mvp.example.swing.productmgt;

import java.util.Collections;
import java.util.List;

import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.domain.model.Product;
import net.premereur.mvp.example.swing.application.ApplicationBus;

@UsesView(ProductSearchPanel.class)
public class ProductSearchPresenter extends ProductPresenterBase<ProductSearchPanel> {

	public void onProductMgtActivated() {
		getEventBus(ApplicationBus.class).clearScreen();
		getEventBus(ApplicationBus.class).setCenterComponent(getView());
		getView().setNameChangeListener(this);
	}

	public void searchForName(String text) {
		if (text.length() > 1) {
			getView().setProducts(getProductRepository().searchProducts(text));
		} else {
			final List<Product> emptyList = Collections.emptyList();
			getView().setProducts(emptyList);
		}

	}
}
