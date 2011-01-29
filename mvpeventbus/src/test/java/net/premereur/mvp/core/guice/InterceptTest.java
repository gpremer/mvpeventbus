package net.premereur.mvp.core.guice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.premereur.mvp.TestBase;
import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;
import net.premereur.mvp.core.base.EventInterceptor;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class InterceptTest extends TestBase {

    static class Capturer {
        Object captured;

        void capture(final Object target) {
            this.captured = target;
        }
    }

    static interface MyEventBus extends EventBus {
        @Event(MyPresenter.class)
        void event(Capturer capturer);

    }

    static public class MyPresenter implements Presenter<MyView, MyEventBus> {

        private final MyEventBus eventBus;

        public MyEventBus getEventBus() {
            return eventBus;
        }

        @Inject
        public MyPresenter(final MyEventBus myEventBus, final MyView view) {
            this.eventBus = myEventBus;
        }

        public void onEvent(final Capturer capturer) {
            capturer.capture(this);
        }

    }

    static public class MyView implements View {

    }

    static public class MyInterceptor implements EventInterceptor {

        private Method interceptedMethod;
        private boolean shouldContinue = true;
        private Object[] args;

        @Override
        public boolean beforeEvent(final EventBus bus, final Method eventMethod, final Object[] args) {
            interceptedMethod = eventMethod;
            this.args = args;
            return shouldContinue;
        }

        public boolean didIntercept() {
            return interceptedMethod != null;
        }

        public void haltsProcessing() {
            shouldContinue = false;
        }
    }

    @Before
    public void setUpLogging() {
        LogManager logManager = LogManager.getLogManager();
        Logger rootLogger = logManager.getLogger("");
        rootLogger.setLevel(Level.FINEST);
        rootLogger.addHandler(new ConsoleHandler());
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
        eventBus = GuiceEventBusFactory.withMainSegment(MyEventBus.class).interceptedBy(interceptor).build().create();
    }

    @Test
    public void shouldBeCalledOnEvent() {
        eventBus.event(capturer);
        assertTrue("The interceptor should be called", interceptor.didIntercept());
    }

    @Test
    public void shouldPassEventMethodToInterceptor() throws Exception {
        eventBus.event(capturer);
        final Method method = MyEventBus.class.getMethod("event", Capturer.class);
        assertEquals(method, interceptor.interceptedMethod);
    }

    @Test
    public void shouldPassArgumentsToInterceptor() throws Exception {
        eventBus.event(capturer);
        assertEquals(capturer, interceptor.args[0]);
        assertEquals(1, interceptor.args.length);
    }

    @Test
    public void shouldStillCallEvent() {
        eventBus.event(capturer);
        assertNotNull("The event should still be called", capturer.captured);
    }

    @Test
    public void shouldNotDispatchEventIfInterceptorHaltsProcessing() {
        interceptor.haltsProcessing();
        eventBus.event(capturer);
        assertNull("The event should not be dispatched", capturer.captured);
    }

    @Test
    public void shouldCallSecondInterceptorIfFirstInterceptorProceeds() {
        MyInterceptor interceptor1 = interceptor;
        MyInterceptor interceptor2 = new MyInterceptor();
        eventBus = GuiceEventBusFactory.withMainSegment(MyEventBus.class).interceptedBy(interceptor1).interceptedBy(interceptor2).build().create();
        eventBus.event(capturer);
        assertTrue("The 2nd interceptor should be called", interceptor2.didIntercept());
    }

    @Test
    public void shouldNotCallSecondInterceptorIfFirstInterceptorHaltsProcessing() {
        MyInterceptor interceptor1 = interceptor;
        MyInterceptor interceptor2 = new MyInterceptor();
        interceptor1.haltsProcessing();
        eventBus = GuiceEventBusFactory.withMainSegment(MyEventBus.class).interceptedBy(interceptor1).interceptedBy(interceptor2).build().create();
        eventBus.event(capturer);
        assertFalse("The 2nd interceptor should not be called", interceptor2.didIntercept());
    }

    @Test
    public void shouldNotDispatchEventIfSecondInterceptorHaltsProcessing() {
        MyInterceptor interceptor1 = interceptor;
        MyInterceptor interceptor2 = new MyInterceptor();
        interceptor2.haltsProcessing();
        eventBus = GuiceEventBusFactory.withMainSegment(MyEventBus.class).interceptedBy(interceptor1).interceptedBy(interceptor2).build().create();
        eventBus.event(capturer);
        assertNull("The event should not be dispatched", capturer.captured);
    }

    private static class Dependency {
        
    }
    
    private static class MyGuiceInterceptor implements EventInterceptor {

        private final Provider<Dependency> dependencyProvider; // allows for scoped dependencies

        @SuppressWarnings("unused")
        @Inject
        public MyGuiceInterceptor(final Provider<Dependency> dependency ) {
            this.dependencyProvider = dependency;
        }
        
        @Override
        public boolean beforeEvent(final EventBus eventBus, final Method eventMethod,final Object[] args) {
            final Capturer capturer = (Capturer)args[0];
            capturer.capture(dependencyProvider.get());
            return HALT;
        }
        
    }
    
    @Test
    public void shouldUseGuiceToInstantiateInterceptor() {
        eventBus = GuiceEventBusFactory.withMainSegment(MyEventBus.class).interceptedBy(MyGuiceInterceptor.class).build().create();
        eventBus.event(capturer);
        assertTrue("Should be the injected dependency", capturer.captured instanceof Dependency);
    }
}
