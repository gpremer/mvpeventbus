package net.premereur.mvp.core;

import static net.premereur.reflection.util.ReflectionUtil.uncheckedNewInstance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import net.premereur.mvp.core.impl.EventBusVerifier;
import net.premereur.mvp.core.impl.EventMethodMapper;
import net.premereur.reflection.util.ReflectionUtil;

public class EventBusFactory {

	@SuppressWarnings("unchecked")
	static public <E extends EventBus> E createEventBus(Class<E> clazz) {
		InvocationHandler handler = new EventBusInvocationHandler(clazz);
		return (E) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, handler);
	}

	private static class EventBusInvocationHandler implements InvocationHandler {
		public Map<String, Method> methods = new HashMap<String, Method>();

		private final Map<Class<?>, Object> handlerInstancesByClass = new HashMap<Class<?>, Object>();

		private final EventMethodMapper methodMapper;
		
		private static final EventBusVerifier VERIFIER = new EventBusVerifier();

		public EventBusInvocationHandler(Class<? extends EventBus> eventBusClass) {
			VERIFIER.verify(eventBusClass);
			methodMapper = new EventMethodMapper(ReflectionUtil.annotatedMethods(eventBusClass, Event.class));			
		}

		@Override
		public Object invoke(Object proxy, Method eventMethod, Object[] args) throws Throwable {
			for (Method handlerMethod : methodMapper.getHandlerEvents(eventMethod)) {
				Object handler = getHandlerInstance(handlerMethod.getDeclaringClass(), (EventBus) proxy);
				handlerMethod.invoke(handler, args);
			}
			return null;
		}

		private Object getHandlerInstance(Class<?> handlerClazz, EventBus bus) {
			synchronized (handlerInstancesByClass) {
				Object handlerInstance = handlerInstancesByClass.get(handlerClazz);
				if (handlerInstance == null) {
					handlerInstance = newHandler(handlerClazz, bus);
					handlerInstancesByClass.put(handlerClazz, handlerInstance);
				}
				return handlerInstance;
			}
		}

		@SuppressWarnings("unchecked")
		private static Presenter newHandler(Class<?> handlerClazz, EventBus bus) {
			Presenter handler = (Presenter) uncheckedNewInstance(handlerClazz);
			handler.setEventBus(bus);
			handler.setView(newView(handlerClazz, bus, handler));
			return handler;
		}

		@SuppressWarnings("unchecked")
		private static View newView(Class<?> handlerClazz, EventBus bus, Presenter presenter) {
			View view = uncheckedNewInstance(getViewClass(handlerClazz));
			setPresenterIfRequested(view, presenter);
			return view;
		}

		@SuppressWarnings("unchecked")
		private static void setPresenterIfRequested(View view, Presenter presenter) {
			if (view instanceof NeedsPresenter<?>) {
				((NeedsPresenter) view).setPresenter(presenter);
			}
		}

		private static Class<? extends View> getViewClass(Class<?> handlerClazz) {
			return handlerClazz.getAnnotation(UsesView.class).value();
		}

	}
}
