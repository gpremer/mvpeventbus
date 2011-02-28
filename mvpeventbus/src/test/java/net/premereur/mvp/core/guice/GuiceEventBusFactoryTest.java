package net.premereur.mvp.core.guice;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.premereur.mvp.TestBase;
import net.premereur.mvp.UniCapturer;
import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;

public class GuiceEventBusFactoryTest extends TestBase {

    static class BusCapturer extends UniCapturer<MyEventBus> {
    }

    static interface MyEventBus extends EventBus {
        @Event(MyPresenter.class)
        void event();

        @Event(MyPresenter.class)
        void eventBusCall(BusCapturer capturer);
    }

    static interface MyOtherEventBus extends EventBus {
    }

    static interface YetAnotherEventBus extends EventBus {
    }

    static interface Dependency {
        void f();
    }

    static public class MyPresenter implements Presenter<MyView, MyEventBus> {

        private final MyEventBus eventBus;
        private final MyEventBus myEventBus;
        private final MyOtherEventBus myOtherEventBus;
        final private Dependency dependency;

        public MyEventBus getEventBus() {
            return eventBus;
        }

        public MyEventBus getMyEventBus() {
            return myEventBus;
        }

        public MyOtherEventBus getMyOtherEventBus() {
            return myOtherEventBus;
        }

        @Inject
        public MyPresenter(EventBus eventBus, MyEventBus myEventBus, MyOtherEventBus myOtherEventBus, MyView view, Dependency dependency) {
            this.eventBus = (MyEventBus) eventBus; // we can cast the general interface to one of the specified
            this.myEventBus = myEventBus; // we can directly inject the proper type
            this.myOtherEventBus = myOtherEventBus; // we can directly inject the proper type
            this.dependency = dependency;
        }

        public void onEvent() {
            this.dependency.f();
        }

        public void onEventBusCall(final BusCapturer capturer) {
            capturer.capture(eventBus);
        }
    }

    static public class MyView implements View {

    }

    @Before
    public void setUpLogging() {
        LogManager logManager = LogManager.getLogManager();
        Logger rootLogger = logManager.getLogger("");
        rootLogger.setLevel(Level.FINEST);
        rootLogger.addHandler(new ConsoleHandler());
    }

    private @Mock
    Dependency dependency;

    private final Module testModule = new AbstractModule() {
        @Override
        protected void configure() {
            bind(Dependency.class).toInstance(dependency);
        }
    };

    private MyEventBus eventBus;

    @Before
    public void createFactory() {
        eventBus = GuiceEventBusFactory.withMainSegment(MyEventBus.class).withAdditionalSegment(MyOtherEventBus.class).using(testModule).build().create();
    }

    @Test
    public void shouldBuildTheFactoryWithOnlyOneType() {
        assertTrue(GuiceEventBusFactory.withMainSegment(MyEventBus.class).build().create() instanceof MyEventBus);
    }

    @Test
    public void shouldBuildTheFactoryWithAMainTypeAndAnAdditionType() {
        final MyEventBus myEventBus = GuiceEventBusFactory.withMainSegment(MyEventBus.class).withAdditionalSegment(MyOtherEventBus.class).build().create();
        assertTrue(myEventBus instanceof MyEventBus);
        assertTrue(myEventBus instanceof MyOtherEventBus);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldBuildTheFactoryWithAMainTypeAndSeveralAdditionTypes() {
        final MyEventBus myEventBus = GuiceEventBusFactory.withMainSegment(MyEventBus.class).withAdditionalSegments(MyOtherEventBus.class,
                YetAnotherEventBus.class).build().create();
        assertTrue(myEventBus instanceof MyEventBus);
        assertTrue(myEventBus instanceof MyOtherEventBus);
        assertTrue(myEventBus instanceof YetAnotherEventBus);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldBuildTheFactoryWithAMainTypeAndSeveralAdditionTypesAsOneArgument() {
        final MyEventBus myEventBus = GuiceEventBusFactory.withSegments(MyEventBus.class, MyOtherEventBus.class, YetAnotherEventBus.class).build().create();
        assertTrue(myEventBus instanceof MyEventBus);
        assertTrue(myEventBus instanceof MyOtherEventBus);
        assertTrue(myEventBus instanceof YetAnotherEventBus);
    }

    @Test
    public void shouldInjectedDifferentEventbusInterfacesInPresenter() {
        eventBus.event();
        // Sending the event will instantiate the presenter. If injection works, i.e. all types are bound, no exception is thrown and vice versa.
    }

    @Test
    public void shouldCreateInjectedPresenter() {
        eventBus.event();
        verify(dependency).f();
    }

    @Test
    public void shouldInjectedEventbusInPresenter() {
        final BusCapturer capturer = new BusCapturer();
        eventBus.eventBusCall(capturer);
        assertTrue(eventBus == capturer.getCaptured());
    }

}
