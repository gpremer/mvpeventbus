package net.premereur.mvp.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

public class EvenBusFactoryBasicTest extends EventBusFactoryTestBase {

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
		bus.event();
		verify(MyPresenter.eventCalls).invoke("event");
	}

	@Test
	public void shouldCreateEventBusThatCallsMethodsHavingArgumentsOnAnnotatedPresenter() {
		MyEventBus bus = EventBusFactory.createEventBus(MyEventBus.class);
		bus.eventWithArg(5);
		verify(MyPresenter.eventCalls).invoke("5");
	}

	@Test
	public void shouldAssignViewsToPresenters() {
		MyEventBus bus = EventBusFactory.createEventBus(MyEventBus.class);
		bus.event();
		verify(MyPresenter.viewSets).invoke("setView");
	}

	@Test
	public void shouldDeferAssignViewsToPresentersUntilEventInvoked() {
		EventBusFactory.createEventBus(MyEventBus.class);
		verify(MyPresenter.viewSets, never()).invoke("setView");

	}

	@Test
	public void shouldAssignEventBusToPresenters() {
		MyEventBus bus = EventBusFactory.createEventBus(MyEventBus.class);
		bus.event();
		verify(MyPresenter.busSets).invoke("setEventBus");
	}

	@Test
	public void shouldDeferAssignEventBusToPresentersUntilEventInvoked() {
		EventBusFactory.createEventBus(MyEventBus.class);
		verify(MyPresenter.busSets, never()).invoke("setBus");
	}

	static interface EventBusDerivedHandler extends EventBus {
		@Event(DerivedHandler.class)
		void event();

	}

	@UsesView(MyView.class)
	static abstract class BaseHandler extends BasePresenter<MyView, EventBusDerivedHandler> {
		public void onEvent() {
		}

	}

	@UsesView(MyView.class)
	public static class DerivedHandler extends BaseHandler {

	}

	@Test
	public void shouldCopeWithHandlerMethodsDefinedOnSuperClass() throws Exception {
		EventBusFactory.createEventBus(EventBusDerivedHandler.class).event();
	}

	@UsesPresenter(MyPresenterForView.class)
	public static class MyViewWithPresenter implements View, NeedsPresenter<MyPresenterForView> {
		static int presenterSets = 0;

		public MyViewWithPresenter() {
		}

		@Override
		public void setPresenter(MyPresenterForView p) {
			presenterSets += p != null ? 1 : 0;
		}

	}

	@UsesView(MyViewWithPresenter.class)
	public static class MyPresenterForView implements Presenter<MyViewWithPresenter, MyEventbusWithViewAndPresenterThatKnowEachOther> {

		@Override
		public void setEventBus(MyEventbusWithViewAndPresenterThatKnowEachOther eventBus) {
		}

		@Override
		public void setView(MyViewWithPresenter view) {
		}

		public void onEvent() {
		}

	}

	public static interface MyEventbusWithViewAndPresenterThatKnowEachOther extends EventBus {
		@Event(MyPresenterForView.class)
		public void event();
	}

	@Test
	public void shouldAssignPresenterToViewIfViewImplementsNeedsPresenterInterface() {
		int presenterSets = MyViewWithPresenter.presenterSets;
		MyEventbusWithViewAndPresenterThatKnowEachOther eventBus = EventBusFactory.createEventBus(MyEventbusWithViewAndPresenterThatKnowEachOther.class);
		eventBus.event();
		assertEquals(presenterSets + 1, MyViewWithPresenter.presenterSets);
	}

}
