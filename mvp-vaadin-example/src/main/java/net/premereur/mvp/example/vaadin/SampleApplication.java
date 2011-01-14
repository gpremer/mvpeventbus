package net.premereur.mvp.example.vaadin;

import net.premereur.mvp.core.guice.GuiceEventBusFactory;
import net.premereur.mvp.example.vaadin.app.ApplicationBus;
import net.premereur.mvp.example.vaadin.app.ApplicationModule;
import net.premereur.mvp.example.vaadin.categorymgt.CategoryMgtBus;
import net.premereur.mvp.example.vaadin.categorymgt.CategoryMgtModule;

import com.vaadin.Application;

/**
 * An application for demonstrating how mvpeventbus could be used with Vaadin.
 * 
 * @author gpremer
 * 
 */
@SuppressWarnings("serial")
public class SampleApplication extends Application {

    private static final GuiceEventBusFactory<ApplicationBus> EVENT_BUS_FACTORY;

    static {
        EVENT_BUS_FACTORY = GuiceEventBusFactory.withMainSegment(ApplicationBus.class).withAdditionalSegment(CategoryMgtBus.class).using(
                new ApplicationModule(), new CategoryMgtModule()).build();
    }

    @Override
    public final void init() {
        EVENT_BUS_FACTORY.create().init(this);
    }
}
