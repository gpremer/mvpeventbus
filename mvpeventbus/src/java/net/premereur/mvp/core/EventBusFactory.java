package net.premereur.mvp.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.premereur.mvp.core.impl.EventBusVerifier;
import net.premereur.mvp.core.impl.EventMethodMapper;
import net.premereur.mvp.core.impl.PresenterFactory;
import net.premereur.mvp.util.reflection.ReflectionUtil;

/**
 * A factory for event busses.
 * 
 * @author gpremer
 * 
 */
public class EventBusFactory {

	/**
	 * Creates an event bus that implements all of the supplied event bus
	 * interfaces. The first argument is the called the master event bus. This
	 * naming is quite arbitrary however as all event bus segments are created
	 * equal. The first one just happens to be the one to which the whole event
	 * bus object is casted to.
	 * 
	 * At run-time it doesn't matter if an application use several event buss
	 * segments that are joined with each other, or one large event bus
	 * responding to the union of all events in all separate busses. At
	 * design-time, however, it is often easier to manage several distinct
	 * busses each with their own events and presenters. When there's a need to
	 * send events to event sinks on other, combined, busses, the designer has
	 * the choice of replicating event methods in the attached bus or calling
	 * events directly on the master bus.
	 * 
	 * @param <E>
	 *            The main event bus interface
	 * @param mainEventBusIntf
	 * @param segmentIntfs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static public <E extends EventBus> E createEventBus(Class<E> mainEventBusIntf, Class<?>... segmentIntfs) {
		Class<? extends EventBus>[] eventBusIntfs = (Class<? extends EventBus>[]) new Class<?>[1 + segmentIntfs.length];
		eventBusIntfs[0] = mainEventBusIntf;
		for (int i = 0; i < segmentIntfs.length; ++i) {
			eventBusIntfs[i + 1] = (Class<? extends EventBus>) segmentIntfs[i];
		}
		EventBusInvocationHandler handler = new EventBusInvocationHandler(eventBusIntfs);
		return (E) Proxy.newProxyInstance(mainEventBusIntf.getClassLoader(), eventBusIntfs, handler);
	}

	private static class EventBusInvocationHandler implements InvocationHandler {
		private final EventMethodMapper methodMapper;

		private final PresenterFactory presenterFactory = new PresenterFactory();

		private static final EventBusVerifier VERIFIER = new EventBusVerifier();

		public EventBusInvocationHandler(Class<? extends EventBus>[] eventBusClasses) {
			methodMapper = new EventMethodMapper();
			for (Class<? extends EventBus> eventBusIntf : eventBusClasses) {
				VERIFIER.verify(eventBusIntf);
				methodMapper.addHandlerMethods(ReflectionUtil.annotatedMethods(eventBusIntf, Event.class));
			}
		}

		@Override
		public Object invoke(Object proxy, Method eventMethod, Object[] args) throws Throwable {
			for (EventMethodMapper.HandlerMethodPair handlerMethodPair : methodMapper.getHandlerEvents(eventMethod)) {
				Object handler = presenterFactory.getPresenter(handlerMethodPair.getHandlerClass(), (EventBus) proxy);
				handlerMethodPair.getMethod().invoke(handler, args);
			}
			return null;
		}

	}
}
