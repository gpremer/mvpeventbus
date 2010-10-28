package net.premereur.mvp.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Some reflection-related utilities.
 * 
 * @author gpremer
 * 
 */
public final class ReflectionUtil {

    /**
     * Creates a new instance of the given class, converting the checked exceptions to run-time exceptions.
     * 
     * @param <T> the type to create an instance of
     * @param clazz the class to create an instance of
     */
    public static <T> T uncheckedNewInstance(final Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all methods of a class that are annotated with the given annotation.
     * 
     * @param clazz the class to introspect
     * @param annot the annotation to look for
     */
    public static Iterable<Method> annotatedMethods(final Class<?> clazz, final Class<? extends Annotation> annot) {
        Method[] methods = clazz.getMethods();
        List<Method> annotatedMethods = new ArrayList<Method>(methods.length);
        for (Method m : methods) {
            if (m.getAnnotation(annot) != null) {
                annotatedMethods.add(m);
            }
        }
        return annotatedMethods;
    }

    private ReflectionUtil() {
    }
}
