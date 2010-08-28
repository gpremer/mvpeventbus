package net.premereur.mvp.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class EventBusFactoryTest {

	static class MyPresenter implements Presenter<View, MyEventBus> {
		static int eventCalls = 0;
		static int eventWithArgValue = 0;

		public void onEvent() {
			eventCalls++;
		}

		public void onEventWithArg(Integer i) {
			eventWithArgValue += i;
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

	static interface EventBusWithNonPrimitiveEventMethods extends EventBus {
		@Event(handlers = {})
		void event(int i);
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithNonPrimitiveEventArguments() {
		EventBusFactory
				.createEventBus(EventBusWithNonPrimitiveEventMethods.class);
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

	static class PresenterWithoutMatchingEvent implements
			Presenter<View, EventBusWithBadPresenter> {

		void onEvent(String s) {
		} // Type does not match
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithPresenterWithoutMatchingMethod() {
		EventBusFactory.createEventBus(EventBusWithBadPresenter.class);
	}

}
