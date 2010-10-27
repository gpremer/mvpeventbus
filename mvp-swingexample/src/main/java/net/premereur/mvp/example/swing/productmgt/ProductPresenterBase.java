package net.premereur.mvp.example.swing.productmgt;

import net.premereur.mvp.core.View;
import net.premereur.mvp.core.guice.BasePresenter;
import net.premereur.mvp.example.domain.repository.ProductRepository;

/**
 * Base class for presenters that work on Products.
 * 
 * @author gpremer
 * 
 * @param <V> The view class.
 */
public abstract class ProductPresenterBase<V extends View> extends BasePresenter<V, ProductMgtBus> {

    private final ProductRepository repository;

    public ProductPresenterBase(final ProductMgtBus bus, final V view, final ProductRepository repository) {
        super(bus, view);
        this.repository = repository;
    }

    public final ProductRepository getProductRepository() {
        return repository;
    }

}
