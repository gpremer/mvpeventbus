package net.premereur.mvp.core.guice;

import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.base.PresenterFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class EventBusModule extends AbstractModule {

    private EventBus eventBus;

    public void setEventBus(EventBus eventBus) {
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
