package net.premereur.mvp.core;

import org.junit.Test;

public class EventBusFactoryVerificationTest extends EventBusFactoryTestBase {

	static interface EventBusWithNonPrimitiveEventMethods extends EventBus {
		@Event( {})
		void event(int i);
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithNonPrimitiveEventArguments() {
		EventBusFactory.createEventBus(EventBusWithNonPrimitiveEventMethods.class);
	}

	static interface EventBusWithNonVoidEventMethods extends EventBus {
		@Event( {})
		int event();
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithNonVoidEventMethods() {
		EventBusFactory.createEventBus(EventBusWithNonVoidEventMethods.class);
	}

	static interface EventBusWithBadPresenter extends EventBus {
		@Event( { PresenterWithoutMatchingEvent.class })
		void event(Integer i);
	}

	public static class MyBadView1 implements View {

	}

	@UsesView(MyBadView1.class)
	public static class PresenterWithoutMatchingEvent extends BasePresenter<MyBadView1, EventBusWithBadPresenter> {

		public void onEvent(String s) { // Type does not match
		}

	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithPresenterWithoutMatchingMethod() {
		EventBusFactory.createEventBus(EventBusWithBadPresenter.class);
	}

	public static class MyBadView2 implements View {

	}

	static class MyPresenterWithoutAnnotation extends BasePresenter<MyBadView2, MyOtherEventBus> {
		public void onEvent() {
		}
	}

	static interface MyOtherEventBus extends EventBus {
		@Event( { MyPresenterWithoutAnnotation.class })
		void event();
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithPresenterWithoutUseViewAnnotation() {
		EventBusFactory.createEventBus(MyOtherEventBus.class);
	}

	class ClassEventBus implements EventBus {

	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusThatIsClass() {
		EventBusFactory.createEventBus(ClassEventBus.class);
	}

	static interface EventBusWithAbstractHandler extends EventBus {
		@Event(AbstractHandler.class)
		void abstractClassEvent();
	}

	@UsesView(MyView.class)
	static abstract class AbstractHandler extends BasePresenter<MyView, EventBusWithAbstractHandler> {
		public void onEvent() {
		}
		
	}

	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllowAbstractHandlers() throws Exception {
		EventBusFactory.createEventBus(EventBusWithAbstractHandler.class);
	}

}
