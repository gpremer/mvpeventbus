package net.premereur.mvp.example.domain.model;

import java.util.Comparator;

/**
 * A product is something that ca be sold.
 * @author gpremer
 *
 */
public class Product extends IdentifiableEntity {
    /**
     * Compares products by name.
     */
    public static final Comparator<Product> NAME_COMPARATOR = new Comparator<Product>() {

        @Override
        public int compare(final Product o1, final Product o2) {
            if (o1 == null) {
                return o2 == null ? -1 : 1;
            }
            return o1.getName().compareTo(o2.getName());
        }

    };
    private String name;

    public Product(final String name) {
        super();
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }
}
