package net.premereur.mvp.example.swing.productmgt;

import net.premereur.mvp.core.BasePresenter;
import net.premereur.mvp.core.View;
import net.premereur.mvp.example.domain.repository.ProductRepository;

public abstract class ProductPresenterBase<V extends View> extends BasePresenter<V, ProductMgtBus> {

	ProductRepository repository;
	
	public ProductRepository getProductRepository() {
		if ( repository == null ) {
			repository = ProductRepository.instance(); 
		}
		return repository;
	}
	
	public void setProductRepository(ProductRepository repository) {
		this.repository = repository;
	}
}
