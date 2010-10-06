package net.premereur.mvp.example.swing.productmgt;

import net.premereur.mvp.example.domain.repository.mem.InMemoryProductRepository;

import com.google.inject.AbstractModule;

public class ProductMgtModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(InMemoryProductRepository.class).to(InMemoryProductRepository.class);
	}

}
