package net.premereur.mvp.example.swing.productmgt;

import com.google.inject.Inject;

import net.premereur.mvp.core.BasePresenter;
import net.premereur.mvp.core.View;
import net.premereur.mvp.example.domain.repository.ProductRepository;

public abstract class ProductPresenterBase<V extends View> extends BasePresenter<V, ProductMgtBus> {

	ProductRepository repository;

	@Inject
	public ProductPresenterBase (ProductRepository repository) {
		this.repository = repository;
	}
	
	public ProductRepository getProductRepository() {
		return repository;
	}
	
}
