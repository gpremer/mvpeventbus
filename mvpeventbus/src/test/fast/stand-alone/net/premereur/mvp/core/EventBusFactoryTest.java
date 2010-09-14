package net.premereur.mvp.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class EventBusFactoryTest {

	public static class MyView implements View {
		static int instantiations = 0;

		public MyView() {
			instantiations++;
		}

	}

	@UsesView(MyView.class)
	public static class MyPresenter implements Presenter<MyView, MyEventBus> {
		static int eventCalls = 0;
		static int eventWithArgValue = 0;
		static int viewSets = 0;
		static int busSets = 0;

		public void onEvent() {
			eventCalls++;
		}

		public void onEventWithArg(Integer i) {
			eventWithArgValue += i;
		}

		public void setView(MyView view) {
			viewSets += view != null ? 1 : 0;
		}

		public void setEventBus(MyEventBus eventBus) {
			busSets += eventBus != null ? 1 : 0;
		}
	}

	static interface MyEventBus extends EventBus {
		@Event({ MyPresenter.class })
		void event();

		@Event({ MyPresenter.class })
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
		assertEquals(viewSet + 1, MyPresenter.viewSets);
	}

	@Test
	public void shouldDeferAssignViewsToPresentersUntilEventInvoked() {
		int viewSet = MyPresenter.viewSets;
		EventBusFactory.createEventBus(MyEventBus.class);
		assertEquals(viewSet, MyPresenter.viewSets);
	}

	@Test
	public void shouldAssignEventBusToPresenters() {
		int busSet = MyPresenter.busSets;
		MyEventBus bus = EventBusFactory.createEventBus(MyEventBus.class);
		bus.event();
		assertEquals(busSet + 1, MyPresenter.busSets);
	}

	@Test
	public void shouldDeferAssignEventBusToPresentersUntilEventInvoked() {
		int busSet = MyPresenter.busSets;
		EventBusFactory.createEventBus(MyEventBus.class);
		assertEquals(busSet, MyPresenter.busSets);
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

	static interface EventBusWithNonPrimitiveEventMethods extends EventBus {
		@Event({})
		void event(int i);
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithNonPrimitiveEventArguments() {
		EventBusFactory.createEventBus(EventBusWithNonPrimitiveEventMethods.class);
	}

	static interface EventBusWithNonVoidEventMethods extends EventBus {
		@Event({})
		int event();
	}

	@Test(expected = RuntimeException.class)
	public void shouldNotAllowEventBusWithNonVoidEventMethods() {
		EventBusFactory.createEventBus(EventBusWithNonVoidEventMethods.class);
	}

	static interface EventBusWithBadPresenter extends EventBus {
		@Event({ PresenterWithoutMatchingEvent.class })
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
		@Event({ MyPresenterWithoutAnnotation.class })
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
	
}
