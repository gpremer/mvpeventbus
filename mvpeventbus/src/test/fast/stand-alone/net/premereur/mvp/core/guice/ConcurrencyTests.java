package net.premereur.mvp.core.guice;

import static org.junit.Assert.*;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;

import org.junit.Test;

import com.google.inject.Singleton;

public class ConcurrencyTests {

	static class Capturer {
		Object target;

		void capture(Object target) {
			this.target = target;
		}
	}

	static interface MyEventBus extends EventBus {
		@Event(MyPresenter.class)
		void event(Capturer capturer);
	}

	@Singleton
	public static class MyPresenter implements Presenter<MyView, MyEventBus> {

		@Override
		public void setEventBus(MyEventBus eventBus) {
		}

		@Override
		public void setView(MyView view) {
		}

		public MyPresenter() {

		}

		public void onEvent(Capturer capturer) {
			capturer.capture(this);
		}
	}

	public static class MyView implements View {

	}

	@Test
	public void shouldCreateDifferentBusses() throws Exception {
		MyEventBus eventBus1 = new GuiceEventBusFactory().createEventBus(MyEventBus.class);
		MyEventBus eventBus2 = new GuiceEventBusFactory().createEventBus(MyEventBus.class);
		assertNotSame(eventBus1, eventBus2);
	}

	@Test
	public void shouldCreateDifferentPresentersForDifferentEventBus() throws Exception {
		MyEventBus eventBus1 = new GuiceEventBusFactory().createEventBus(MyEventBus.class);
		MyEventBus eventBus2 = new GuiceEventBusFactory().createEventBus(MyEventBus.class);
		Capturer capturer1 = new Capturer();
		eventBus1.event(capturer1);
		Capturer capturer2 = new Capturer();
		eventBus2.event(capturer2);
		assertNotSame(capturer1.target, capturer2.target);
	}

	@Test
	public void shouldUseSamePresenterForSameEventBus() throws Exception {
		MyEventBus eventBus1 = new GuiceEventBusFactory().createEventBus(MyEventBus.class);
		Capturer capturer1 = new Capturer();
		eventBus1.event(capturer1);
		Capturer capturer2 = new Capturer();
		eventBus1.event(capturer2);
		assertSame(capturer1.target, capturer2.target);
	}

}
