package net.premereur.mvp.core.guice;

import static org.junit.Assert.assertNotNull;
import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;

import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

    static interface MyEventBus extends EventBus {
        @Event(MyHandler.class)
        void event(final Capturer capturer);
    }

    public static class MyHandler implements EventHandler {

        public void onEvent(final Capturer capturer) {
            capturer.capture(this);
        }

    }

    static class Capturer {
        MyHandler captured;

        void capture(final MyHandler target) {
            this.captured = target;
        }

        public void reset() {
            this.captured = null;
        }

    }


    private MyEventBus eventBus;
    private Capturer capturer;

    @Before
    public void initCapturer() {
        capturer = new Capturer();
    }

    @Before
    public void createEventBus() {
        eventBus = GuiceEventBusFactory.withMainSegment(MyEventBus.class).build().create();
    }

    @Test
    public void shouldReuseEventHandlerWhenSendingSecondEvent() {
        eventBus.event(capturer);
        final MyHandler p1 = capturer.captured;
        assertNotNull("Should receive event", p1);
    }

}
