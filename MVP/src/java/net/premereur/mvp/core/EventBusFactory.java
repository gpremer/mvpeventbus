package net.premereur.mvp.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
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
		return (E) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, handler);
	}

	static class EventBusInvocationHandler implements InvocationHandler {
		public Map<String, Method> methods = new HashMap<String, Method>();

		private final Map<Class<?>, Object> handlerInstancesByClass = new HashMap<Class<?>, Object>();

		private final Map<Method, List<Method>> handlingMethodsByEventMethod = new HashMap<Method, List<Method>>();

		public EventBusInvocationHandler(Class<? extends EventBus> clazz) {
			for (Method eventMethod : eventMethods(clazz)) {
				Event eventAnt = eventMethod.getAnnotation(Event.class);
				verifyHandlers(eventAnt.handlers(), clazz);
				verifyEventBusMethods(eventMethod);
				List<Method> handlingMethods = new ArrayList<Method>();
				for (Class<? extends Presenter<? extends View, ? extends EventBus>> presenter : eventAnt.handlers()) {
					handlingMethods.add(correspondingPresenterMethod(presenter, eventMethod));
				}
				handlingMethodsByEventMethod.put(eventMethod, handlingMethods);
			}
		}

		private void verifyHandlers(Class<? extends Presenter<? extends View, ? extends EventBus>>[] handlers, Class<? extends EventBus> eventBusClass) {
			for (Class<? extends Presenter<? extends View, ? extends EventBus>> handler : handlers) {
				if (!eventBusClass.isAssignableFrom((Class<?>) getPresenterGenerics(handler)[1])) {
					throw new IllegalArgumentException("The requested handlerClass " + handler + " does not use event bus " + eventBusClass.getName());
				}
			}
		}

		private static Type[] getPresenterGenerics(Class<?> presenterClazz) {
			for (Type genericInterface : presenterClazz.getGenericInterfaces()) {
				if (genericInterface instanceof ParameterizedType) {
					ParameterizedType paramType = (ParameterizedType) genericInterface;
					if (Presenter.class.isAssignableFrom((Class<?>) paramType.getRawType())) {
						return paramType.getActualTypeArguments();
					}
				}
			}
			throw new RuntimeException("This class doesn't implement " + Presenter.class);
		}

		private static Method correspondingPresenterMethod(Class<? extends Presenter<? extends View, ? extends EventBus>> presenter, Method ebm) {
			for (Method pm : presenter.getMethods()) {
				if (corresponds(pm, ebm)) {
					return pm;
				}
			}
			throw new IllegalArgumentException("Did not find corresponding public event handler on " + presenter.getName() + " for event bus method "
					+ ebm.getName());
		}

		private static boolean corresponds(Method pm, Method ebm) {
			return getEventMethodId(ebm).equals(getEventMethodId(pm));
		}

		private void verifyEventBusMethods(Method m) {
			verifyNoPrimitiveArguments(m);
			verifyOnlyVoidMethod(m);
		}

		private void verifyOnlyVoidMethod(Method m) {
			if (m.getReturnType().getName() != "void") {
				throw new IllegalArgumentException("Found a method " + m.getName() + " with non-void return type");

			}

		}

		private void verifyNoPrimitiveArguments(Method m) {
			for (Type t : m.getGenericParameterTypes()) {
				if (t instanceof Class<?> && ((Class<?>) t).isPrimitive()) {
					throw new IllegalArgumentException("Found a method " + m.getName() + " with primitive argument");
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

		private static void addArgumentTypes(final Method m, final StringBuilder sb) {
			for (Type t : m.getGenericParameterTypes()) {
				sb.append(t.toString());
			}
		}

		private static void addNameWithoutPrefix(final StringBuilder sb, final String name) {
			if (name.startsWith("on") && name.length() > 2) {
				sb.append(name.substring(2, 3).toLowerCase()).append(name.substring(3, name.length()));
			} else {
				sb.append(name);
			}
		}

		@Override
		public Object invoke(Object proxy, Method eventMethod, Object[] args) throws Throwable {
			for (Method handlerMethod : handlingMethodsByEventMethod.get(eventMethod)) {
				Object handler = getHandlerInstance(handlerMethod.getDeclaringClass());
				handlerMethod.invoke(handler, args);
			}
			return null;
		}

		private Object getHandlerInstance(Class<?> handlerClazz) {
			synchronized (handlerInstancesByClass) {
				Object handlerInstance = handlerInstancesByClass.get(handlerClazz);
				if (handlerInstance == null) {
					handlerInstance = newHandler(handlerClazz);
					handlerInstancesByClass.put(handlerClazz, handlerInstance);
				}
				return handlerInstance;
			}
		}

		@SuppressWarnings("unchecked")
		private static Object newHandler(Class<?> handlerClazz) {
			try {
				Presenter handler = (Presenter<? extends View, ? extends EventBus>) handlerClazz.newInstance();
				handler.setView(newView(handlerClazz));
				return handler;
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		private static View newView(Class<?> handlerClazz) {
			try {
				return getViewClass(handlerClazz).newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@SuppressWarnings("unchecked")
		private static Class<? extends View> getViewClass(Class<?> handlerClazz) {
			return (Class<? extends View>) (getPresenterGenerics(handlerClazz)[0]);
		}

	}
}
