package net.premereur.mvp.core.guice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import net.premereur.mvp.TestBase;
import net.premereur.mvp.UniCapturer;
import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;
import net.premereur.mvp.core.Event.Policy;

import org.junit.Before;
import org.junit.Test;

public class LifeCycleTest extends TestBase {

    static interface MyEventBus extends EventBus {
        @Event(MyPresenter.class)
        void event(final Capturer capturer);

        @Event(value = {MyPresenter.class}, instantiation = Policy.TO_NEW_INSTANCE)
        void createEvent(final MultiCapturer capturer);

        @Event(MyPresenter.class)
        void normalEvent(final MultiCapturer capturer);

        @Event(value = {MyPresenter.class}, instantiation = Policy.TO_EXISTING_INSTANCES)
        void existingEvent(final MultiCapturer capturer);
    }

    static class MyView implements View {

    }

    public static class MyPresenter implements Presenter<MyView, MyEventBus> {

        public int count = 0;

        public void onEvent(final Capturer capturer) {
            capturer.capture(this);
            count += 1;
        }

        public void onCreateEvent(final MultiCapturer capturer) {
            capturer.captureAll(this);
        }

        public void onNormalEvent(final MultiCapturer capturer) {
            capturer.captureAll(this);
        }

        public void onExistingEvent(final MultiCapturer capturer) {
            capturer.captureAll(this);
        }
    }

    static class Capturer extends UniCapturer<MyPresenter> {
    }

    static class MultiCapturer {
        Set<MyPresenter> allCaptured = new HashSet<MyPresenter>();

        void captureAll(final MyPresenter target) {
            this.allCaptured.add(target);
        }

        void reset() {
            allCaptured.clear();
        }

        int numberCaptured() {
            return allCaptured.size();
        }

        MyPresenter firstCaptured() {
            return allCaptured.iterator().next();
        }

        MyPresenter secondCaptured() {
            allCaptured.iterator().next();
            return allCaptured.iterator().next();
        }
    }

    private MyEventBus eventBus;
    private Capturer capturer;
    private MultiCapturer multiCapturer;

    @Before
    public void initCapturer() {
        capturer = new Capturer();
        multiCapturer = new MultiCapturer();
    }

    @Before
    public void createEventBus() {
        eventBus = GuiceEventBusFactory.withMainSegment(MyEventBus.class).build().create();
    }

    @Test
    public void shouldReuseEventHandlerWhenSendingSecondEvent() {
        eventBus.event(capturer);
        MyPresenter p1 = capturer.getCaptured();
        eventBus.event(capturer);
        MyPresenter p2 = capturer.getCaptured();
        assertTrue("Both presenter references should point to same presenter", p1 == p2);
    }

    @Test
    public void shouldNotSendEventsToDetachedEventHandlers() {
        eventBus.event(capturer);
        MyPresenter p1 = capturer.getCaptured();
        assertEquals(1, p1.count);
        eventBus.detach(p1);
        eventBus.event(capturer);
        assertEquals(1, p1.count);
    }

    @Test
    public void shouldCreateNewEventHandlerWhenFirstOneRemovedFromEventBus() {
        eventBus.event(capturer);
        MyPresenter p1 = capturer.getCaptured();
        eventBus.detach(p1);
        eventBus.event(capturer);
        MyPresenter p2 = capturer.getCaptured();
        assertTrue("The presenter references should point to different presenters", p1 != p2);
    }

    @Test
    public void shouldSendEventsToDetachedEventHandlers() {
        eventBus.event(capturer);
        MyPresenter p1 = capturer.getCaptured();
        assertEquals(1, p1.count);
        eventBus.detach(p1);
        eventBus.event(capturer);
        assertEquals(1, p1.count);
    }

    @Test
    public void shouldSendEventsToAttachedEventHandler() {
        MyPresenter p = new MyPresenter();
        eventBus.attach(p);
        eventBus.event(capturer);
        MyPresenter p1 = capturer.getCaptured();
        assertEquals(p, p1);
    }

    @Test
    public void shouldSendEventsToExistingAndAttachedEventHandler() {
        eventBus.event(capturer);
        MyPresenter p = new MyPresenter();
        eventBus.attach(p);
        eventBus.normalEvent(multiCapturer);        
        assertEquals(2, multiCapturer.numberCaptured());
    }

    @Test
    public void shouldDispatchEventOnceTheFirstTimeAHandlerIsReferenced() {
        eventBus.normalEvent(multiCapturer);
        assertEquals(1, multiCapturer.numberCaptured());
    }

    @Test
    public void shouldCreateAdditionalEventHandlerWhenEvenMethodAnnotatedWithToNewInstance() {
        eventBus.normalEvent(multiCapturer);
        eventBus.createEvent(multiCapturer);
        assertEquals(2, multiCapturer.numberCaptured()); // set makes sure there are 2 different presenters
    }

    @Test
    public void shouldKeepAdditionalEventHandlerWhenEventDispatchedAfterEvenMethodAnnotatedWithToNewInstance() {
        eventBus.normalEvent(multiCapturer);
        eventBus.createEvent(multiCapturer);
        multiCapturer.reset();
        eventBus.normalEvent(multiCapturer);
        assertEquals(2, multiCapturer.numberCaptured()); // set makes sure there are 2 different presenters
    }

    @Test
    public void shouldNotInstantiateHandlerForEventAnnotatedWithToExistingInstancesTheFirstTimeAnEventToTheHandlerIsSent() {
        eventBus.existingEvent(multiCapturer);
        assertEquals(0, multiCapturer.numberCaptured());
    }

    @Test
    public void shouldUseExistingHandlerForEventAnnotatedWithToExistingInstancesIfOneWasAlreadyInstantiated() {
        eventBus.event(capturer);
        eventBus.existingEvent(multiCapturer);
        assertEquals(1, multiCapturer.numberCaptured());
    }

}
