package net.premereur.mvp.core.guice;

import static org.junit.Assert.assertTrue;
import net.premereur.mvp.TestBase;
import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;

import org.junit.Before;
import org.junit.Test;

public class LifeCycleTest extends TestBase {

    static interface MyEventBus extends EventBus {
        @Event(MyPresenter.class)
        void event(final Capturer capturer);

        void createEvent(final Capturer capturer);
    }

    static class MyView implements View {

    }

    public static class MyPresenter implements Presenter<MyView, MyEventBus> {

        public void onEvent(final Capturer capturer) {
            capturer.capture(this);
        }

        public void onCreateEvent(final Capturer capturer) {
            capturer.capture(this);
        }
    }

    static class Capturer {
        MyPresenter captured;

        void capture(final MyPresenter target) {
            this.captured = target;
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
        MyPresenter p1 = capturer.captured;
        eventBus.event(capturer);
        MyPresenter p2 = capturer.captured;
        assertTrue("Both presenter references should point to same presenter", p1 == p2);
    }

    @Test
    public void shouldCreateNewEventHandlerWhenFirstOneRemovedFromEventBus() {
        eventBus.event(capturer);
        MyPresenter p1 = capturer.captured;
        eventBus.detach(p1);
        eventBus.event(capturer);
        MyPresenter p2 = capturer.captured;
        assertTrue("The presenter references should point to different presenters", p1 != p2);
    }

    public void shouldCreateNewEventHandlerWhenEvenMethodAnnotatedWithRequiresNew() {

    }
}
