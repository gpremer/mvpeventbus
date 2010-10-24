package net.premereur.mvp.util.reflection;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class ReflectionUtilTest {
	
	@Test
	public void shouldReturnNewInstanceWithoutCheckedExceptions() {
		assertTrue(ReflectionUtil.uncheckedNewInstance(ArrayList.class) instanceof ArrayList<?>);
	}
	
	@Test(expected=RuntimeException.class)
	public void shouldThrowUncheckedExceptionInCaseOfInstantiationProblems() {
		ReflectionUtil.uncheckedNewInstance(Boolean.class);
	}
	
	static class MySingleton {
		private MySingleton() { }
	}

	@Test(expected=RuntimeException.class)
	public void shouldThrowUncheckedExceptionInCaseOfIllegalAccessProblems() {
		ReflectionUtil.uncheckedNewInstance(MySingleton.class);
	}


}
