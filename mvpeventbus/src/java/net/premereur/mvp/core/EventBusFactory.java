package net.premereur.mvp.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import net.premereur.mvp.core.impl.EventBusVerifier;
import net.premereur.mvp.core.impl.EventMethodMapper;
import net.premereur.mvp.core.impl.PresenterFactory;
import net.premereur.mvp.util.reflection.ReflectionUtil;

public class EventBusFactory {

	@SuppressWarnings("unchecked")
	static public <E extends EventBus> E createEventBus(Class<E> clazz) {
		EventBusInvocationHandler handler = new EventBusInvocationHandler(clazz);
		return (E) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, handler);
	}

	private static class EventBusInvocationHandler implements InvocationHandler {
		public Map<String, Method> methods = new HashMap<String, Method>();

		private final EventMethodMapper methodMapper;

		private final PresenterFactory presenterFactory = new PresenterFactory();

		private static final EventBusVerifier VERIFIER = new EventBusVerifier();

		public EventBusInvocationHandler(Class<? extends EventBus> eventBusClass) {
			VERIFIER.verify(eventBusClass);
			methodMapper = new EventMethodMapper(ReflectionUtil.annotatedMethods(eventBusClass, Event.class));
		}

		@Override
		public Object invoke(Object proxy, Method eventMethod, Object[] args) throws Throwable {
			for (Method handlerMethod : methodMapper.getHandlerEvents(eventMethod)) {
				Object handler = presenterFactory.getPresenter(handlerMethod.getDeclaringClass(), (EventBus) proxy);
				handlerMethod.invoke(handler, args);
			}
			return null;
		}

	}
}
