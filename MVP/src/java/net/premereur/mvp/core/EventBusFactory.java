package net.premereur.mvp.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBusFactory {

	@SuppressWarnings("unchecked")
	static public <E extends EventBus> E createEventBus(Class<E> clazz) {
		InvocationHandler handler = new EventBusInvocationHandler(clazz);
		return (E) Proxy.newProxyInstance(clazz.getClassLoader(),
				new Class[] { clazz }, handler);
	}

	static class EventBusInvocationHandler implements InvocationHandler {
		public Map<String, Method> methods = new HashMap<String, Method>();

		private final Map<Class<?>, Object> handlerInstancesByClass = new HashMap<Class<?>, Object>();

		private final Map<Method, List<Method>> handlingMethodsByEventMethod = new HashMap<Method, List<Method>>();

		public EventBusInvocationHandler(Class<? extends EventBus> clazz) {
			for (Method eventMethod : eventMethods(clazz)) {
				verifyEventBusMethods(eventMethod);
				List<Method> handlingMethods = new ArrayList<Method>();
				Event eventAnt = eventMethod.getAnnotation(Event.class);
				for (Class<? extends Presenter<View, ? extends EventBus>> presenter : eventAnt
						.handlers()) {
					handlingMethods.add(correspondingPresenterMethod(presenter,
							eventMethod));
				}
				handlingMethodsByEventMethod.put(eventMethod, handlingMethods);
			}
		}

		private Method correspondingPresenterMethod(
				Class<? extends Presenter<View, ? extends EventBus>> presenter,
				Method ebm) {
			for (Method pm : presenter.getMethods()) {
				if (corresponds(pm, ebm)) {
					return pm;
				}
			}
			throw new IllegalArgumentException(
					"Did not find corresponding public event handler on "
							+ presenter.getName() + " for event bus method "
							+ ebm.getName());
		}

		private boolean corresponds(Method pm, Method ebm) {
			return getEventMethodId(ebm).equals(getEventMethodId(pm));
		}

		private void verifyEventBusMethods(Method m) {
			verifyNoPrimitiveArguments(m);
			verifyOnlyVoidMethod(m);
		}

		private void verifyOnlyVoidMethod(Method m) {
			if (m.getReturnType().getName() != "void") {
				throw new IllegalArgumentException("Found a method "
						+ m.getName() + " with non-void return type");

			}

		}

		private void verifyNoPrimitiveArguments(Method m) {
			for (Type t : m.getGenericParameterTypes()) {
				if (t instanceof Class<?> && ((Class<?>) t).isPrimitive()) {
					throw new IllegalArgumentException("Found a method "
							+ m.getName() + " with primitive argument");
				}
			}
		}

		private List<Method> eventMethods(Class<?> clazz) {
			List<Method> eventMethods = new ArrayList<Method>();
			for (Method m : clazz.getMethods()) {
				Event eventAnt = m.getAnnotation(Event.class);
				if (eventAnt != null) {
					eventMethods.add(m);
				}
			}
			return eventMethods;
		}

		private static String getEventMethodId(final Method m) {
			final StringBuilder sb = new StringBuilder();
			addNameWithoutPrefix(sb, m.getName());
			sb.append('+');
			addArgumentTypes(m, sb);
			return sb.toString();
		}

		private static void addArgumentTypes(final Method m,
				final StringBuilder sb) {
			for (Type t : m.getGenericParameterTypes()) {
				sb.append(t.toString());
			}
		}

		private static void addNameWithoutPrefix(final StringBuilder sb,
				final String name) {
			if (name.startsWith("on") && name.length() > 2) {
				sb.append(name.substring(2, 3).toLowerCase()).append(
						name.substring(3, name.length()));
			} else {
				sb.append(name);
			}
		}

		@Override
		public Object invoke(Object proxy, Method eventMethod, Object[] args)
				throws Throwable {
			for (Method handlerMethod : handlingMethodsByEventMethod
					.get(eventMethod)) {
				Object handler = getHandlerInstance(handlerMethod
						.getDeclaringClass());
				handlerMethod.invoke(handler, args);
			}
			return null;
		}

		private Object getHandlerInstance(Class<?> handlerClazz) {
			synchronized (handlerInstancesByClass) {
				Object handlerInstance = handlerInstancesByClass
						.get(handlerClazz);
				if (handlerInstance == null) {
					handlerInstance = newHandler(handlerClazz);
					handlerInstancesByClass.put(handlerClazz, handlerInstance);
				}
				return newHandler(handlerClazz);
			}
		}

		private Object newHandler(Class<?> handlerClazz) {
			try {
				return handlerClazz.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
