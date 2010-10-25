package net.premereur.mvp.core.guice;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.PresenterFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * A Guice module that registers the {@link PresenterFactory}.
 * 
 * @author gpremer
 * 
 */
public final class EventBusModule extends AbstractModule {

    private EventBus eventBus;

    public void setEventBus(final EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    protected void configure() {
        bind(PresenterFactory.class).to(GuicePresenterFactory.class);
    }

    @Provides
    public EventBus providesEventBus() {
        return eventBus;
    }

}
