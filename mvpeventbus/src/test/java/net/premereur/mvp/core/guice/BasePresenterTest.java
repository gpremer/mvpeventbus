package net.premereur.mvp.core.guice;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BasePresenterTest {

    static interface MyEventBus extends EventBus {
    }

    static interface MyOtherEventBus extends EventBus {
    }

    static class MyView implements View {
    }

    static class MyPresenter extends BasePresenter<MyView, MyEventBus> {

        public MyPresenter(final MyEventBus eventBus, final MyView view) {
            super(eventBus, view);
        }

    }

    @Mock
    private MyView view;
    private MyEventBus bus;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        bus = mock(MyEventBus.class, withSettings().extraInterfaces(MyOtherEventBus.class));
    }

    @Test
    public void shouldReturnBusFromPresenter() {
        final MyPresenter myPresenter = new MyPresenter(bus, view);
        assertSame(bus, myPresenter.getEventBus());
    }

    @Test
    public void shouldReturnBusFromPresenterCastToAlternativeInterface() {
        final MyPresenter myPresenter = new MyPresenter(bus, view);
        assertTrue(myPresenter.getEventBus(MyOtherEventBus.class) instanceof MyOtherEventBus);
    }

    @Test
    public void shouldReturnViewFromPresenter() {
        final MyPresenter myPresenter = new MyPresenter(bus, view);
        assertSame(view, myPresenter.getView());
    }
}
