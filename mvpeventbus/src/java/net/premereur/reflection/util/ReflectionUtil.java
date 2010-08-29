package net.premereur.reflection.util;

public final class ReflectionUtil {

	public static <T> T uncheckedNewInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private ReflectionUtil() {
	}
}