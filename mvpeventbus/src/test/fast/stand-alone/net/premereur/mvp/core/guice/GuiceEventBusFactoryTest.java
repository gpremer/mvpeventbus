package net.premereur.mvp.core.guice;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.premereur.mvp.core.Event;
import net.premereur.mvp.core.EventBus;
import net.premereur.mvp.core.Presenter;
import net.premereur.mvp.core.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;

public class GuiceEventBusFactoryTest {

	static interface MyEventBus extends EventBus {
		@Event(MyPresenter.class)
		void event();

		@Event(MyPresenter.class)
		void eventBusCall();

	}

	static interface Dependency {
		void f();

		void receiveEventBus(MyEventBus eventBus);
	}

	static public class MyPresenter implements Presenter<MyView, MyEventBus> {

		private MyEventBus eventBus;
		final private Dependency dependency;

		@Override
		public void setEventBus(MyEventBus eventBus) {
		}

		public MyEventBus getEventBus() {
			return eventBus;
		}
		
		@Override
		public void setView(MyView view) {
		}

		@Inject
		public MyPresenter(EventBus eventBus, MyView view, Dependency dependency) {
			this.eventBus = (MyEventBus) eventBus;
			this.dependency = dependency;
		}

		public void onEvent() {
			this.dependency.f();
		}
		
		public void onEventBusCall() {
			this.dependency.receiveEventBus(this.eventBus);
		}
	}

	static public class MyView implements View {

	}

	@Before
	public void setUpLogging() {
		LogManager logManager = LogManager.getLogManager();
		Logger rootLogger = logManager.getLogger("");
		rootLogger.setLevel(Level.FINEST);
		rootLogger.addHandler(new ConsoleHandler());
	}
	
	@Mock Dependency dependency;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	Module testModule = new AbstractModule() {
		@Override
		protected void configure() {
			bind(Dependency.class).toInstance(dependency);
		}
	};

	@Test
	public void shouldCreateInjectedPresenter() {
		MyEventBus eventBus = new GuiceEventBusFactory(testModule).createEventBus(MyEventBus.class);
		eventBus.event();
		verify(dependency).f();
	}

	@Test
	public void shouldInjectedEventbusInPresenter() {
		MyEventBus eventBus = new GuiceEventBusFactory(testModule).createEventBus(MyEventBus.class);
		eventBus.eventBusCall();
		verify(dependency).receiveEventBus(any(MyEventBus.class)); // It could still be null
	}

}
