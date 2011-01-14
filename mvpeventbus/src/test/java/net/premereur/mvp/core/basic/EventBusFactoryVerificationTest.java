package net.premereur.mvp.core.basic;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.core.View;
import net.premereur.mvp.core.base.VerificationException;
import net.premereur.mvp.core.basic.BasePresenter;

import org.junit.Test;

public class EventBusFactoryVerificationTest extends EventBusFactoryTestBase {

    static interface EventBusWithNonPrimitiveEventMethods extends EventBus {
        @Event( {})
        void event(int i);
    }

    @Test(expected = VerificationException.class)
    public void shouldNotAllowEventBusWithNonPrimitiveEventArguments() {
        createEventBus(EventBusWithNonPrimitiveEventMethods.class);
    }

    static interface EventBusWithNonVoidEventMethods extends EventBus {
        @Event( {})
        int event();
    }

    @Test(expected = VerificationException.class)
    public void shouldNotAllowEventBusWithNonVoidEventMethods() {
        createEventBus(EventBusWithNonVoidEventMethods.class);
    }

    static interface EventBusWithBadPresenter extends EventBus {
        @Event( {PresenterWithoutMatchingEvent.class})
        void event(Integer i);
    }

    public static class MyBadView1 implements View {

    }

    @UsesView(MyBadView1.class)
    public static class PresenterWithoutMatchingEvent extends BasePresenter<MyBadView1, EventBusWithBadPresenter> {

        public void onEvent(String s) { // Type does not match
        }

    }

    @Test(expected = VerificationException.class)
    public void shouldNotAllowEventBusWithPresenterWithoutMatchingMethod() {
        createEventBus(EventBusWithNonVoidEventMethods.class);
    }

    public static class MyBadView2 implements View {

    }

    static class MyPresenterWithoutAnnotation extends BasePresenter<MyBadView2, MyOtherEventBus> {
        public void onEvent() {
        }
    }

    static interface MyOtherEventBus extends EventBus {
        @Event( {MyPresenterWithoutAnnotation.class})
        void event();
    }

    @Test(expected = VerificationException.class)
    public void shouldNotAllowEventBusWithPresenterWithoutUseViewAnnotation() {
        createEventBus(MyOtherEventBus.class);
    }

    class ClassEventBus implements EventBus {

    }

    @Test(expected = VerificationException.class)
    public void shouldNotAllowEventBusThatIsClass() {
        createEventBus(ClassEventBus.class);
    }

    public static interface EventBusWithAbstractHandler extends EventBus {
        @Event(AbstractHandler.class)
        void abstractClassEvent();
    }

    @UsesView(MyView.class)
    public static abstract class AbstractHandler extends BasePresenter<MyView, EventBusWithAbstractHandler> {
        public void onAbstractClassEvent() {
        }

    }

    @Test(expected = VerificationException.class)
    public void shouldNotAllowAbstractHandlers() throws Exception {
        createEventBus(EventBusWithAbstractHandler.class);
    }

    public static interface EventBusWithPresenterWithoutDefaultConstructor extends EventBus {
        @Event(PresenterWithoutDefaultConstructor.class)
        void event();
    }

    @UsesView(MyView.class)
    public static class PresenterWithoutDefaultConstructor extends BasePresenter<MyView, EventBusWithPresenterWithoutDefaultConstructor> {
        public PresenterWithoutDefaultConstructor(int i) {

        }

        public void onEvent() {
        }
    }

    @Test(expected = VerificationException.class)
    public void shouldNotAllowPresentersWithoutDefaultConstructor() throws Exception {
        createEventBus(EventBusWithPresenterWithoutDefaultConstructor.class);
    }

}
