package net.premereur.mvp.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class EventBusFactoryTest {

	static class MyView implements View {
		static int instantiations = 0; 

		public MyView() {
			instantiations++;
		}
	}

	static class MyPresenter implements Presenter<MyView, MyEventBus> {
		static int eventCalls = 0;
		static int eventWithArgValue = 0;
		static int viewSets = 0;		

		public void onEvent() {
			eventCalls++;
		}

		public void onEventWithArg(Integer i) {
			eventWithArgValue += i;
		}

		public void setView(MyView view) {
			viewSets += view != null ? 1 : 0;
		}
	}

	static interface MyEventBus extends EventBus {
		@Event(handlers = { MyPresenter.class })
		void event();

		@Event(handlers = { MyPresenter.class })
		void eventWithArg(Integer i);

		int nonAnnotatedMethod(int i); // Here to show that non-annotated
		// methods are just ignored
	}

	@Test
	public void shouldCreateEventBus() throws Exception {
		MyEventBus bus = EventBusFactory.createEventBus(MyEventBus.class);
		assertNotNull(bus);
	}

	@Test
	public void shouldCreateEventBusThatImplementsMethodsFromInterface() {
		MyEventBus bus = EventBusFactory.createEventBus(MyEventBus.class);
		bus.event();
	}

	@Test
	public void shouldCreateEventBusThatCallsMethodsOnAnnotatedPresenter() {
		MyEventBus bus = EventBusFactory.createEventBus(MyEventBus.class);
		int calls = MyPresenter.eventCalls;
		bus.event();
		assertEquals(calls + 1, MyPresenter.eventCalls);
	}

	@Test
	public void shouldCreateEventBusThatCallsMethodsHavingArgumentsOnAnnotatedPresenter() {
		MyEventBus bus = EventBusFactory.createEventBus(MyEventBus.class);
		int prevValue = MyPresenter.eventWithArgValue;
		bus.eventWithArg(5);
		assertEquals(prevValue + 5, MyPresenter.eventWithArgValue);
	}

	@Test
	public void shouldAssignViewsToPresenters() {
		int viewSet = MyPresenter.viewSets;
		MyEventBus bus = EventBusFactory.createEventBus(MyEventBus.class);
		bus.event();
		assertEquals(viewSet+1, MyPresenter.viewSets);
	}

	@Test
	public void shouldDeferAssignViewsToPresentersUntilEventInvoked() {
		int viewSet = MyPresenter.viewSets;
		MyEventBus bus = EventBusFactory.createEventBus(MyEventBus.class);
		assertEquals(viewSet, MyPresenter.viewSets);
		bus.event();
		assertEquals(viewSet+1, MyPresenter.viewSets);
	}

	static interface EventBusWithNonPrimitiveEventMethods extends EventBus {
		@Event(handlers = {})
		void event(int i);
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithNonPrimitiveEventArguments() {
		EventBusFactory.createEventBus(EventBusWithNonPrimitiveEventMethods.class);
	}

	static interface EventBusWithNonVoidEventMethods extends EventBus {
		@Event(handlers = {})
		int event();
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithNonVoidEventMethods() {
		EventBusFactory.createEventBus(EventBusWithNonVoidEventMethods.class);
	}

	static interface EventBusWithBadPresenter extends EventBus {
		@Event(handlers = { PresenterWithoutMatchingEvent.class })
		void event(Integer i);
	}

	static class PresenterWithoutMatchingEvent implements Presenter<MyView, EventBusWithBadPresenter> {

		void onEvent(String s) {
		} // Type does not match

		public void setView(MyView view) {

		}
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithPresenterWithoutMatchingMethod() {
		EventBusFactory.createEventBus(EventBusWithBadPresenter.class);
	}

	static interface MyOtherEventBus extends EventBus {
		@Event(handlers = { MyPresenter.class })
		void event();
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithPresenterForDifferentEventBus() {
		EventBusFactory.createEventBus(MyOtherEventBus.class);
	}

}
