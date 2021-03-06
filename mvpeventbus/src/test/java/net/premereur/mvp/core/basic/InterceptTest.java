package net.premereur.mvp.core.basic;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import net.premereur.mvp.TestBase;
import net.premereur.mvp.UniCapturer;
import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.core.View;
import net.premereur.mvp.core.base.EventInterceptor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterceptTest extends TestBase {

    static class Capturer extends UniCapturer<MyPresenter> {
    }

    static interface MyEventBus extends EventBus {
        @Event(MyPresenter.class)
        void event(Capturer capturer);

    }

    @UsesView(MyView.class)
    static public class MyPresenter extends BasePresenter<MyView, MyEventBus> {

        public void onEvent(final Capturer capturer) {
            capturer.capture(this);
        }

    }

    static public class MyView implements View {

    }

    static public class MyInterceptor implements EventInterceptor {

        private boolean intercepted = false;
        private boolean shouldContinue = PROCEED;

        @Override
        public boolean beforeEvent(final EventBus bus, final Method eventMethod, final Object[] args) {
            intercepted = true;
            return shouldContinue;
        }

        public boolean isIntercepted() {
            return intercepted;
        }

        public void haltsProcessing() {
            shouldContinue = HALT;
        }

    }

    @Rule
    public MethodRule watchman = new TestWatchman() {
        public void starting(FrameworkMethod method) {
            logger.info("{} being run...", method.getName());
        }
    };

    final Logger logger = LoggerFactory.getLogger(InterceptTest.class);

    @Before
    public void setUpLogging() {
    }

    @Before
    public void initCapturer() {
        capturer = new Capturer();
    }

    private Capturer capturer;

    private MyEventBus eventBus;

    private MyInterceptor interceptor;

    @Before
    public void createFactory() {
        interceptor = new MyInterceptor();
        eventBus = BasicEventBusFactory.withMainSegment(MyEventBus.class).interceptedBy(interceptor).build().create();
    }

    @Test
    public void shouldBeCalledOnEvent() {
        eventBus.event(capturer);
        assertTrue("The interceptor should be called", interceptor.isIntercepted());
    }

    @Test
    public void shouldStillCallEvent() {
        eventBus.event(capturer);
        assertNotNull("The event should still be called", capturer.getCaptured());
    }

    @Test
    public void shouldNotDispatchEventIfInterceptorHaltsProcessing() {
        interceptor.haltsProcessing();
        eventBus.event(capturer);
        assertNull("The event should not be dispatched", capturer.getCaptured());
    }
}
