package net.premereur.mvp.core;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class EventBusFactorySegmentsTest extends EventBusFactoryTestBase {

	@Before
	public void resetMementos() {
		reset(MainBusPresenter.memento);
		reset(ChildBusPresenter.memento);
		super.resetMementos();
	}

	interface MainEventBus extends EventBus {
		@Event(MainBusPresenter.class)
		public void event();

		@Event(MainBusPresenter.class)
		public void masterEvent();
	}

	interface ChainedEventBus extends EventBus {
		@Event(ChildBusPresenter.class)
		void event();

		@Event(ChildBusPresenter.class)
		void child1Event();
	}

	@UsesView(MyView.class)
	public static class MainBusPresenter implements Presenter<MyView, MainEventBus> {
		public static Memento memento = mock(Memento.class);

		@Override
		public void setEventBus(MainEventBus eventBus) {
		}

		@Override
		public void setView(MyView view) {
		}

		public void onEvent() {
			memento.invoke("shared_event");
		}

		public void onMasterEvent() {
			memento.invoke("master_ev");
		}

	}

	@UsesView(MyView.class)
	public static class ChildBusPresenter implements Presenter<MyView, ChainedEventBus> {
		public static Memento memento = mock(Memento.class);

		public void onEvent() {
			memento.invoke("shared_event");
		}

		public void onChild1Event() {
			memento.invoke("child1_ev");
		}

		@Override
		public void setEventBus(ChainedEventBus eventBus) {
		}

		@Override
		public void setView(MyView view) {
		}

	}

	@Test
	public void shouldBeAbleToCreateBusComposedOfSegmentBusses() throws Exception {
		MainEventBus masterBus = EventBusFactory.createEventBus(MainEventBus.class, ChainedEventBus.class);
		assertTrue(masterBus instanceof MainEventBus);
		assertTrue(masterBus instanceof ChainedEventBus);
	}

	@Test
	public void shouldPropagateEventsSentToAttachedBusToMasterAndAttachedBus() throws Exception {
		ChainedEventBus chainedBus = (ChainedEventBus) EventBusFactory.createEventBus(MainEventBus.class, ChainedEventBus.class);
		chainedBus.event();
		verify(MainBusPresenter.memento).invoke("shared_event");
		verify(ChildBusPresenter.memento).invoke("shared_event");
	}

	@Test
	public void shouldPropagateEventsSentToMasterBusToMasterBusaAndAttachedBus() throws Exception {
		MainEventBus masterBus = EventBusFactory.createEventBus(MainEventBus.class, ChainedEventBus.class);
		masterBus.event();
		verify(MainBusPresenter.memento).invoke("shared_event");
		verify(ChildBusPresenter.memento).invoke("shared_event");
	}

}
