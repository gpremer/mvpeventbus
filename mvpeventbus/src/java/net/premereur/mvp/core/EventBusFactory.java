package net.premereur.mvp.core;

import static net.premereur.reflection.util.ReflectionUtil.getImplementedInterfaceGenericTypes;
import static net.premereur.reflection.util.ReflectionUtil.uncheckedNewInstance;

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
		return (E) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, handler);
	}

	static class EventBusInvocationHandler implements InvocationHandler {
		public Map<String, Method> methods = new HashMap<String, Method>();

		private final Map<Class<?>, Object> handlerInstancesByClass = new HashMap<Class<?>, Object>();

		private final Map<Method, List<Method>> handlingMethodsByEventMethod = new HashMap<Method, List<Method>>();

		public EventBusInvocationHandler(Class<? extends EventBus> eventBusClass) {
			for (Method eventMethod : eventMethods(eventBusClass)) {
				Event eventAnt = eventMethod.getAnnotation(Event.class);
				verifyHandlers(eventAnt.handlers(), eventBusClass);
				verifyEventBusMethods(eventMethod);
				List<Method> handlingMethods = findHandlerMethodsForEvent(eventMethod, eventAnt);
				handlingMethodsByEventMethod.put(eventMethod, handlingMethods);
			}
		}

		private List<Method> findHandlerMethodsForEvent(Method eventMethod, Event eventAnt) {
			List<Method> handlingMethods = new ArrayList<Method>();
			for (Class<? extends Presenter<? extends View, ? extends EventBus>> presenter : eventAnt.handlers()) {
				handlingMethods.add(correspondingPresenterMethod(presenter, eventMethod));
			}
			return handlingMethods;
		}

		private void verifyHandlers(Class<? extends Presenter<? extends View, ? extends EventBus>>[] handlers, Class<? extends EventBus> eventBusClass) {
			for (Class<? extends Presenter<? extends View, ? extends EventBus>> handler : handlers) {
				if (!eventBusClass.isAssignableFrom((Class<?>) getPresenterGenerics(handler)[1])) {
					throw new IllegalArgumentException("The requested handlerClass " + handler + " does not use event bus " + eventBusClass.getName());
				}
			}
		}

		private static Type[] getPresenterGenerics(Class<?> presenterClazz) {
			Type[] presenterTypes = getImplementedInterfaceGenericTypes(presenterClazz, Presenter.class);
			if (presenterTypes.length == 0) {
				throw new RuntimeException("This class doesn't implement " + Presenter.class);
			}
			return presenterTypes;
		}

		private static Method correspondingPresenterMethod(Class<? extends Presenter<? extends View, ? extends EventBus>> presenter, Method ebm) {
			for (Method pm : presenter.getMethods()) {
				if (presenterMethodCorrespondsWithEventBusMethod(pm, ebm)) {
					return pm;
				}
			}
			throw new IllegalArgumentException("Did not find corresponding public event handler on " + presenter.getName() + " for event bus method "
					+ ebm.getName());
		}

		private static boolean presenterMethodCorrespondsWithEventBusMethod(Method pm, Method ebm) {
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
			handler.setView(newView(handlerClazz));
			handler.setEventBus(bus);
			return handler;
		}

		private static View newView(Class<?> handlerClazz) {
			return uncheckedNewInstance(getViewClass(handlerClazz));
		}

		@SuppressWarnings("unchecked")
		private static Class<? extends View> getViewClass(Class<?> handlerClazz) {
			return (Class<? extends View>) (getPresenterGenerics(handlerClazz)[0]);
		}

	}
}
