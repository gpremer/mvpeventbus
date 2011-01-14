package net.premereur.mvp.core.guice;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.premereur.mvp.TestBase;
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

    static interface MyEventBus extends EventBus {
        @Event(MyPresenter.class)
        void event();

        @Event(MyPresenter.class)
        void eventBusCall();

    }

    static interface MyOtherEventBus extends EventBus {
    }

    static interface Dependency {
        void f();

        void receiveEventBus(MyEventBus eventBus);
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
            this.myEventBus = myEventBus;
            this.myOtherEventBus = myOtherEventBus;
            this.eventBus = (MyEventBus) eventBus;
            this.dependency = dependency;
        }

        public void onEvent() {
            this.dependency.f();
        }

        public void onEventBusCall() {
            this.dependency.receiveEventBus(this.eventBus);
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
        eventBus.eventBusCall();
        verify(dependency).receiveEventBus(any(MyEventBus.class)); // It could still be null
    }

}
