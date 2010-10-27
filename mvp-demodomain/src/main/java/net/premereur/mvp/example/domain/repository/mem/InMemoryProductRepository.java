package net.premereur.mvp.example.domain.repository.mem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.premereur.mvp.example.domain.model.Product;
import net.premereur.mvp.example.domain.repository.ProductRepository;

/**
 * In-memory implementation of {@link ProductRepository}.
 * 
 * @author gpremer
 * 
 */
public final class InMemoryProductRepository implements ProductRepository {
    private final List<Product> products = new ArrayList<Product>(Arrays.asList(new Product[] {new Product("Cabriolet"), new Product("Minivan"),
            new Product("Ink"), new Product("Maintenace"), new Product("Beer"), new Product("Eggs"), new Product("Cariage"), new Product("Installation"),
            new Product("Insurance"), new Product("Income protection"), new Product("Hard disk")}));

    public InMemoryProductRepository() {

    }

    public List<Product> allProducts() {
        return Collections.unmodifiableList(products);
    }

    public List<Product> searchProducts(final String prefix) {
        List<Product> matches = new ArrayList<Product>();
        for (Product p : allProducts()) {
            if (p.getName().startsWith(prefix)) {
                matches.add(p);
            }
        }
        Collections.sort(matches, Product.NAME_COMPARATOR);
        return Collections.unmodifiableList(matches);
    }

    public void save(final Product product) {
        if (!products.contains(product)) {
            products.add(product);
        }
    }
}
