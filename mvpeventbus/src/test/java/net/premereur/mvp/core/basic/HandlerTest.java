package net.premereur.mvp.core.basic;

import static org.junit.Assert.assertNotNull;
import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.core.View;

import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

    static interface MyEventBus extends EventBus {
        @Event(MyHandler.class)
        void event(final Capturer capturer);
    }

    @UsesView(MyView.class)
    public static class MyHandler implements EventHandler {

        public void onEvent(final Capturer capturer) {
            capturer.capture(this);
        }

    }

    public static class MyView implements View {
        
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
        eventBus = BasicEventBusFactory.withMainSegment(MyEventBus.class).build().create();
    }

    @Test
    public void shouldReuseEventHandlerWhenSendingSecondEvent() {
        eventBus.event(capturer);
        final MyHandler p1 = capturer.captured;
        assertNotNull("Should receive event", p1);
    }

}
