package net.premereur.mvp.core.guice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import net.premereur.mvp.core.EventBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Guice;
import com.google.inject.Injector;

@RunWith(MockitoJUnitRunner.class)
public class EventBusModuleTest {

    static interface MyEventBus extends EventBus {
    }

    @Mock
    private MyEventBus eventBus1;
    @Mock
    private MyEventBus eventBus2;

    @Before
    public void cleanModule() {
        while (EventBusModule.booleanHasThreadEventBus()) {
            EventBusModule.popThreadEventBus();
        }
    }

    @Test
    public void shouldTellWhenThereIsAnEventBusAssociatedWithThread() {
        EventBusModule.pushThreadEventBus(eventBus1);
        assertTrue(EventBusModule.booleanHasThreadEventBus());
    }

    @Test
    public void shouldTellWhenThereIsNoEventBusAssociatedWithThread() {
        assertFalse(EventBusModule.booleanHasThreadEventBus());
    }

    @Test
    public void shouldRemoveAssociationWithPop() {
        EventBusModule.pushThreadEventBus(eventBus1);
        EventBusModule.popThreadEventBus();
        assertFalse(EventBusModule.booleanHasThreadEventBus());

    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAcceptANullEventBus() {
        EventBusModule.pushThreadEventBus(null);
    }

    @Test
    public void shouldPoppingShouldReturnBussesInReverseOrderOfPushing() {
        final Injector injector = createInjector();
        EventBusModule.pushThreadEventBus(eventBus1);
        EventBusModule.pushThreadEventBus(eventBus2);
        MyEventBus bus = injector.getInstance(MyEventBus.class);
        assertSame(eventBus2, bus);
        EventBusModule.popThreadEventBus();
        bus = injector.getInstance(MyEventBus.class);
        assertSame(eventBus1, bus);
    }

    @Test
    public void shouldInjectTheBusInstanceThatIsSetToTheModule() {
        final Injector injector = createInjector();
        EventBusModule.pushThreadEventBus(eventBus1);
        final MyEventBus bus = injector.getInstance(MyEventBus.class);
        assertSame(eventBus1, bus);
    }

    @Test(expected=RuntimeException.class)
    public void shouldThrowExceptionIfNoBusIsAssociatedWithThread() {
        final Injector injector = createInjector();
        injector.getInstance(MyEventBus.class);
    }
    
    @SuppressWarnings("serial")
    private Injector createInjector() {
        Collection<Class<? extends EventBus>> cc = new ArrayList<Class<? extends EventBus>>() {
            {
                add(MyEventBus.class);
            }
        };
        final Injector injector = Guice.createInjector(new EventBusModule(cc));
        return injector;
    }
}
