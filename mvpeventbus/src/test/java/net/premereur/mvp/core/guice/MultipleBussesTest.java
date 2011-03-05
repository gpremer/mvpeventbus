package net.premereur.mvp.core.guice;

import static org.junit.Assert.*;

import net.premereur.mvp.UniCapturer;
import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class MultipleBussesTest {

    static interface MyEventBus extends EventBus {
        @Event(MyPresenter.class)
        void event(final Capturer capturer);

        @Event(MyPresenter.class)
        void simpleEvent();

        @Event(MyPresenter.class)
        void nestedEvent(final MyEventBus bus2, final Capturer capturer);

        @Event(MyPresenter.class)
        void injectingEvent(final MyEventBus bus2, final Capturer capturer);

        @Event(MyPresenter.class)
        void injectingInNestedBusEvent(final MyEventBus bus2, final Capturer capturer);
    }

    public static class MyPresenter implements EventHandler {

        private final MyEventBus bus;
        private final Injector injector;

        @Inject
        public MyPresenter(final MyEventBus bus, final Injector injector) {
            this.bus = bus;
            this.injector = injector;
        }

        public void onEvent(final Capturer capturer) {
            capturer.capture(bus);
        }

        public void onSimpleEvent() {
        }

        public void onNestedEvent(final MyEventBus bus2, final Capturer capturer) {
            bus2.event(capturer);
        }

        public void onInjectingEvent(final MyEventBus bus2, final Capturer capturer) {
            bus2.simpleEvent();
            capturer.capture(injector.getInstance(MyEventBus.class));
            // In practice an injector won't probably be used directly. But instantiating an object that has an event bus dependency has the same effect
        }

        public void onInjectingInNestedBusEvent(final MyEventBus bus2, final Capturer capturer) {
            bus2.injectingEvent(bus2, capturer);
        }
    }

    static class Capturer extends UniCapturer<MyEventBus> {
    }

    private Capturer capturer;
    private MyEventBus eventBus1;
    private MyEventBus eventBus2;

    @Before
    public void setupCapturer() {
        capturer = new Capturer();
    }

    @Before
    public void setupBus() {
        eventBus1 = GuiceEventBusFactory.withMainSegment(MyEventBus.class).build().create();
        eventBus2 = GuiceEventBusFactory.withMainSegment(MyEventBus.class).build().create();
    }

    @Test
    public void shouldNotHaveThreadAssociationAfterCreation() throws Exception {
        assertFalse(EventBusModule.booleanHasThreadEventBus());
    }

    @Test
    public void shouldNotHaveThreadAssociationAfterEventSending() throws Exception {
        eventBus1.event(capturer);
        assertFalse(EventBusModule.booleanHasThreadEventBus());
    }

    @Test
    public void shouldBeAbleToUseMultipleBussesSequentially() throws Exception {
        eventBus1.event(capturer);
        assertTrue(eventBus1 == capturer.getCaptured());
        eventBus2.event(capturer);
        assertSame(eventBus2, capturer.getCaptured());
    }

    @Test
    public void shouldBeAbleToCallOnANestedBus() throws Exception {
        eventBus1.nestedEvent(eventBus2, capturer);
        assertSame(eventBus2, capturer.getCaptured());
    }

    @Test
    public void shouldInjectNestedBusWhenInjectingInNestedBus() throws Exception {
        eventBus1.injectingInNestedBusEvent(eventBus2, capturer); // capturing the current event bus
        assertSame(eventBus2, capturer.getCaptured());
    }

    @Test
    public void shouldInjectOuterBusWhenInjectingInOuterBusEvenAfterCallToNested() throws Exception {
        eventBus1.injectingEvent(eventBus2, capturer); // capturing the current event bus
        assertSame(eventBus1, capturer.getCaptured());
    }
}
