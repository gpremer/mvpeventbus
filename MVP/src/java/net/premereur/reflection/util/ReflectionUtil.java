package net.premereur.reflection.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ReflectionUtil {

	public static Type[] getImplementedInterfaceGenericTypes(Class<?> sourceClass, Class<?> interfaceClass) {
		for (Type genericInterface : sourceClass.getGenericInterfaces()) {
			if (genericInterface instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) genericInterface;
				if (interfaceClass.isAssignableFrom((Class<?>) paramType.getRawType())) {
					return paramType.getActualTypeArguments();
				}
			}
		}
		return new Type[] {};
	}

}
