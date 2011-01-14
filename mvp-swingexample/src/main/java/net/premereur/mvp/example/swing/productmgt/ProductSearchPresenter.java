package net.premereur.mvp.example.swing.productmgt;

import java.util.Collections;
import java.util.List;

import net.premereur.mvp.example.domain.model.Product;
import net.premereur.mvp.example.domain.repository.ProductRepository;
import net.premereur.mvp.example.swing.application.ApplicationBus;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Manages the {@link ProductSearchPanel}.
 * 
 * @author gpremer
 * 
 */
@Singleton
public class ProductSearchPresenter extends ProductPresenterBase<ProductSearchPanel> {

    @Inject
    public ProductSearchPresenter(final ProductMgtBus bus, final ProductSearchPanel view, final ProductRepository repository) {
        super(bus, view, repository);
    }

    /**
     * See {@link ProductMgtBus#productMgtActivated()}.
     */
    public final void onProductMgtActivated() {
        getEventBus(ApplicationBus.class).clearScreen();
        getEventBus(ApplicationBus.class).setCenterComponent(getView());
        getView().setNameChangeListener(this);
    }

    /**
     * Call back for new search names entered in the view.
     */
    public final void newSearchName(final String text) {
        if (text.length() > 1) {
            getView().setProducts(getProductRepository().searchProducts(text));
        } else {
            final List<Product> emptyList = Collections.emptyList();
            getView().setProducts(emptyList);
        }

    }
}
