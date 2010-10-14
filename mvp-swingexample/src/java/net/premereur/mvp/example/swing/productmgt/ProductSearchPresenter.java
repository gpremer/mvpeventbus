package net.premereur.mvp.example.swing.productmgt;

import java.util.Collections;
import java.util.List;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.example.domain.model.Product;
import net.premereur.mvp.example.domain.repository.ProductRepository;
import net.premereur.mvp.example.swing.application.ApplicationBus;

import com.google.inject.Inject;

public class ProductSearchPresenter extends ProductPresenterBase<ProductSearchPanel> {

	@Inject
	public ProductSearchPresenter(EventBus bus, ProductSearchPanel view, ProductRepository repository) {
		super((ProductMgtBus) bus, view, repository);
	}

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
