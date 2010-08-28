package net.premereur.reflection.util;

import static org.junit.Assert.*;

import java.lang.reflect.Type;

import org.junit.Test;

public class ReflectionUtilTest {

	static interface MyInterface<A, B> {
	}

	static class MyClass implements MyInterface<Integer, String> {
	}

	@Test
	public void shouldGetImplementedInterfaceGenericTypes() {
		Type[] interfaceTypes = ReflectionUtil.getImplementedInterfaceGenericTypes(MyClass.class, MyInterface.class);
		assertEquals(Integer.class, interfaceTypes[0]);
		assertEquals(String.class, interfaceTypes[1]);
	}

	static class MyNonImplementingClass {
		
	}
	
	static class MyImplementingNonGenericClass implements Cloneable {
		
	}
	
	@Test
	public void shouldReturnEmptyArrayWhenImplementedInterfaceHasNoGenericTypes() {
		Type[] interfaceTypes = ReflectionUtil.getImplementedInterfaceGenericTypes(MyNonImplementingClass.class, MyInterface.class);
		assertEquals(0, interfaceTypes.length);
		interfaceTypes = ReflectionUtil.getImplementedInterfaceGenericTypes(MyImplementingNonGenericClass.class, MyInterface.class);
		assertEquals(0, interfaceTypes.length);
	}

}
