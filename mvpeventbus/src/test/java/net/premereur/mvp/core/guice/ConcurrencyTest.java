package net.premereur.mvp.core.guice;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import net.premereur.mvp.TestBase;
import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Singleton;

public class ConcurrencyTest extends TestBase {

    static class Capturer {
        MyPresenter presenter;

        void capture(final MyPresenter target) {
            this.presenter = target;
        }

    }

    static interface MyEventBus extends EventBus {
        @Event(MyPresenter.class)
        void event(Capturer capturer);

    }

    public static class MyPresenter implements Presenter<MyView, MyEventBus> {

        Dependency dependency;
        MyEventBus eventBus;

        @Inject
        public MyPresenter(final Dependency dependency, final MyEventBus bus) {
            this.dependency = dependency;
            this.eventBus = bus;
        }

        public void onEvent(final Capturer capturer) {
            capturer.capture(this);
        }
    }

    public static class MyView implements View {

    }

    static interface Dependency {
    }

    @Singleton
    static class MyDependency implements Dependency {

    }

    private Module testModule = new AbstractModule() {
        @Override
        protected void configure() {
            bind(Dependency.class).to(MyDependency.class).asEagerSingleton();
        }
    };

    private GuiceEventBusFactory<MyEventBus> guiceEventBusFactory;

    @Before
    public void createFactory() {
        // It is important that the tested event busses are created by the same factory instance
        guiceEventBusFactory = GuiceEventBusFactory.withMainSegment(MyEventBus.class).using(testModule).build();
    }

    @Test
    public void shouldCreateDifferentBusses() throws Exception {
        MyEventBus eventBus1 = guiceEventBusFactory.create();
        MyEventBus eventBus2 = guiceEventBusFactory.create();
        assertNotSame(eventBus1, eventBus2);
    }

    @Test
    public void shouldCreateDifferentPresentersForDifferentEventBus() throws Exception {
        MyEventBus eventBus1 = guiceEventBusFactory.create();
        MyEventBus eventBus2 = guiceEventBusFactory.create();
        Capturer capturer1 = new Capturer();
        eventBus1.event(capturer1);
        Capturer capturer2 = new Capturer();
        eventBus2.event(capturer2);
        assertNotSame(capturer1.presenter, capturer2.presenter);
    }

    @Test
    public void shouldUseSamePresenterForSameEventBus() throws Exception {
        MyEventBus eventBus1 = guiceEventBusFactory.create();
        Capturer capturer1 = new Capturer();
        eventBus1.event(capturer1);
        Capturer capturer2 = new Capturer();
        eventBus1.event(capturer2);
        assertSame(capturer1.presenter, capturer2.presenter);
    }

    @Test
    public void shouldUseSameSingletonDependencyForAllEventBusses() throws Exception {
        MyEventBus eventBus1 = guiceEventBusFactory.create();
        MyEventBus eventBus2 = guiceEventBusFactory.create();
        Capturer capturer1 = new Capturer();
        eventBus1.event(capturer1);
        Capturer capturer2 = new Capturer();
        eventBus2.event(capturer2);
        assertSame(capturer1.presenter.dependency, capturer2.presenter.dependency);
    }

    @Test
    public void shouldInjectDifferentEventBussesOnPresentersFromDifferentBusses() throws Exception {
        MyEventBus eventBus1 = guiceEventBusFactory.create();
        MyEventBus eventBus2 = guiceEventBusFactory.create();
        Capturer capturer1 = new Capturer();
        eventBus1.event(capturer1);
        Capturer capturer2 = new Capturer();
        eventBus2.event(capturer2);
        assertNotSame(capturer1.presenter.eventBus, capturer2.presenter.eventBus);
    }

}
