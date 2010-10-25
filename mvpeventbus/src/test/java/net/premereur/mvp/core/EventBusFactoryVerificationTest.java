package net.premereur.mvp.core;

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
		eventBusFactory.createEventBus(EventBusWithNonPrimitiveEventMethods.class);
	}

	static interface EventBusWithNonVoidEventMethods extends EventBus {
		@Event( {})
		int event();
	}

	@Test(expected = VerificationException.class)
	public void shouldNotAllowEventBusWithNonVoidEventMethods() {
		eventBusFactory.createEventBus(EventBusWithNonVoidEventMethods.class);
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

	@Test(expected = VerificationException.class)
	public void shouldNotAllowEventBusWithPresenterWithoutMatchingMethod() {
		eventBusFactory.createEventBus(EventBusWithBadPresenter.class);
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

	@Test(expected = VerificationException.class)
	public void shouldNotAllowEventBusWithPresenterWithoutUseViewAnnotation() {
		eventBusFactory.createEventBus(MyOtherEventBus.class);
	}

	class ClassEventBus implements EventBus {

	}

	@Test(expected = VerificationException.class)
	public void shouldNotAllowEventBusThatIsClass() {
		eventBusFactory.createEventBus(ClassEventBus.class);
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
		eventBusFactory.createEventBus(EventBusWithAbstractHandler.class);
	}
	
	public static interface EventBusWithPresenterWithoutDefaultConstructor extends EventBus {
		@Event(PresenterWithoutDefaultConstructor.class)
		void event();
	}
	
	@UsesView(MyView.class)
	public static class PresenterWithoutDefaultConstructor extends BasePresenter<MyView, EventBusWithPresenterWithoutDefaultConstructor> {
		public PresenterWithoutDefaultConstructor(int i) {
			
		}
		
		public void onEvent() { }
	}

	@Test(expected = VerificationException.class)
	public void shouldNotAllowPresentersWithoutDefaultConstructor() throws Exception {
		eventBusFactory.createEventBus(EventBusWithPresenterWithoutDefaultConstructor.class);
	}
	
	
}
