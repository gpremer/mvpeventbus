package net.premereur.mvp.example.swing.productmgt;

import net.premereur.mvp.core.View;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.domain.repository.ProductRepository;

public abstract class ProductPresenterBase<V extends View> extends BasePresenter<V, ProductMgtBus> {

	private final ProductRepository repository;

	public ProductPresenterBase (ProductMgtBus bus, V view, ProductRepository repository) {
		super(bus, view);
		this.repository = repository;
	}
	
	public ProductRepository getProductRepository() {
		return repository;
	}
	
}
