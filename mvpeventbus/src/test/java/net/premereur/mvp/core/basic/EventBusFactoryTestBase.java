package net.premereur.mvp.core.basic;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.EventBusFactory;
import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.core.View;
import net.premereur.mvp.core.basic.BasicEventBusFactory;

import org.junit.Before;

public abstract class EventBusFactoryTestBase {

	protected EventBusFactory eventBusFactory;

	@Before
	public void setUpFactory() {
		eventBusFactory = new BasicEventBusFactory();
	}

	interface Memento {
		void invoke(String arg);
	}

	@Before
	public void resetMementos() {
		reset(MyPresenter.busSets);
		reset(MyPresenter.viewSets);
		reset(MyPresenter.eventCalls);
	}

	public static class MyView implements View {
		static int instantiations = 0;

		public MyView() {
			instantiations++;
		}

	}

	@UsesView(MyView.class)
	public static class MyPresenter implements Presenter<MyView, MyEventBus> {
		static Memento eventCalls = mock(Memento.class);
		static Memento viewSets = mock(Memento.class);
		static Memento busSets = mock(Memento.class);

		public void onEvent() {
			eventCalls.invoke("event");
		}

		public void onEventWithArg(Integer i) {
			eventCalls.invoke(i.toString());
		}

		public void setView(MyView view) {
			viewSets.invoke("setView");
		}

		public void setEventBus(MyEventBus eventBus) {
			busSets.invoke("setEventBus");
		}
	}

	static interface MyEventBus extends EventBus {
		@Event( { MyPresenter.class })
		void event();

		@Event( { MyPresenter.class })
		void eventWithArg(Integer i);

		int nonAnnotatedMethod(int i); // Here to show that non-annotated
		// methods are just ignored
	}

}
