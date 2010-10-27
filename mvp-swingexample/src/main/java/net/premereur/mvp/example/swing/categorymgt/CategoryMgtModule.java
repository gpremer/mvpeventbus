package net.premereur.mvp.example.swing.categorymgt;

import net.premereur.mvp.example.domain.repository.CategoryRepository;
import net.premereur.mvp.example.domain.repository.mem.InMemoryCategoryRepository;

import com.google.inject.AbstractModule;

public class CategoryMgtModule extends AbstractModule {

    @Override
    protected final void configure() {
        bind(CategoryRepository.class).to(InMemoryCategoryRepository.class);
    }

}
