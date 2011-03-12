package net.premereur.mvp.core.guice;

import static net.premereur.mvp.core.Event.Invocation.DELAYED;
import static net.premereur.mvp.core.Event.Invocation.IMMEDIATE;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventHandler;
import net.premereur.mvp.core.Event.Invocation;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;

public class DelayedInvocationTest {

    public static interface MyEventBus extends EventBus {

        @Event(value = MyPresenter.class, invocation = DELAYED)
        void twoDelayedEvents(StringBuilder b);

        @Event(value = MyPresenter.class)
        void event(StringBuilder b, Invocation... spec);

        @Event(value = MyPresenter.class, invocation = DELAYED)
        void delayedEvent(StringBuilder b, Invocation... invocations);
    }

    public static class MyPresenter implements EventHandler {
        private final MyEventBus eventBus;

        @Inject
        public MyPresenter(final MyEventBus bus) {
            this.eventBus = bus;
        }

        public void onEvent(final StringBuilder b, Invocation... invocations) {
            b.append("bi ");
            dispatch(b, invocations);
            b.append("ai ");
        }

        public void onDelayedEvent(final StringBuilder b, final Invocation... invocations) {
            b.append("bd ");
            dispatch(b, invocations);
            b.append("ad ");
        }

        private void dispatch(final StringBuilder b, Invocation... invocations) {
            if (invocations != null && invocations.length > 0) {
                Invocation[] nextInvocations = Arrays.copyOfRange(invocations, 1, invocations.length);
                switch (invocations[0]) {
                case DELAYED:
                    eventBus.delayedEvent(b, nextInvocations);
                    break;
                case IMMEDIATE:
                    eventBus.event(b, nextInvocations);
                    break;
                }
            }
        }

        public void onExplicitImmediateEvent(final StringBuilder b) {
            b.append("b");
            b.append("a");
        }

        public void onTwoDelayedEvents(final StringBuilder b) {
            b.append("c ");
            eventBus.delayedEvent(b);
            eventBus.delayedEvent(b);
            b.append("d ");
        }

    }

    private MyEventBus eventBus;

    @Before
    public void createEventBus() {
        eventBus = GuiceEventBusFactory.withMainSegment(MyEventBus.class).build().create();
    }

    @Test
    public void shouldInvokeNestedEventsImmediatelyByDefault() throws Exception {
        final StringBuilder b = new StringBuilder();
        eventBus.event(b, IMMEDIATE);
        assertEquals("bi bi ai ai ", b.toString());
    }

    @Test
    public void shouldInvokeNestedEventsImmediatelyIfImmediateAnnotionIsPresent() throws Exception {
        final StringBuilder b = new StringBuilder();
        eventBus.event(b, IMMEDIATE);
        assertEquals("bi bi ai ai ", b.toString());
    }

    @Test
    public void shouldInvokeNestedEventsAfterEventIsHandledIfDelayedAnnotationIsPresent() throws Exception {
        final StringBuilder b = new StringBuilder();
        eventBus.event(b, DELAYED);
        assertEquals("bi ai bd ad ", b.toString());
    }

    @Test
    public void shouldInvokeTopLevelEventsImmediatelyEvenIfDelayedAnnotationIsPresent() throws Exception {
        final StringBuilder b = new StringBuilder();
        eventBus.delayedEvent(b);
        assertEquals("bd ad ", b.toString());
    }

    @Test
    public void shouldInvokeNestedDelayedEventsConsecutively() throws Exception {
        final StringBuilder b = new StringBuilder();
        eventBus.twoDelayedEvents(b);
        assertEquals("c d bd ad bd ad ", b.toString());
    }

    @Test
    public void shouldInvokeImmediateEventWithinNestedDelayedEventsAfterMainEventAndWithinDelayedEvent() throws Exception {
        final StringBuilder b = new StringBuilder();
        eventBus.event(b, IMMEDIATE, DELAYED, IMMEDIATE);
        assertEquals("bi bi ai bd bi ai ad ai ", b.toString());
    }

    @Test
    public void shouldInvokeDelayedEventWithinNestedDelayedEventsAfterMainEventAndAfterFirstDelayedEvent() throws Exception {
        final StringBuilder b = new StringBuilder();
        eventBus.event(b, DELAYED, DELAYED);
        assertEquals("bi ai bd ad bd ad ", b.toString());
    }

}
