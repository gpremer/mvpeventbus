package net.premereur.mvp.example.swing.productmgt;

import net.premereur.mvp.core.BasePresenter;
import net.premereur.mvp.core.View;
import net.premereur.mvp.example.domain.repository.ProductRepository;

public abstract class ProductPresenterBase<V extends View> extends BasePresenter<V, ProductMgtBus> {

	public ProductRepository getProductRepository() {
		return ProductRepository.instance();
	}
}
