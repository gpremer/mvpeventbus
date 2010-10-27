package net.premereur.mvp.example.swing.productmgt;

import net.premereur.mvp.example.domain.repository.ProductRepository;
import net.premereur.mvp.example.domain.repository.mem.InMemoryProductRepository;

import com.google.inject.AbstractModule;

/**
 * Guice module for Product management dependencies.
 * 
 * @author gpremer
 * 
 */
public class ProductMgtModule extends AbstractModule {

    @Override
    protected final void configure() {
        bind(ProductRepository.class).to(InMemoryProductRepository.class);
    }

}
