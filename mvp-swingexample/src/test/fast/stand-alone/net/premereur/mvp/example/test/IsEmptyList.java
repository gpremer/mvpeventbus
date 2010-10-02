package net.premereur.mvp.example.test;

import java.util.List;

import org.mockito.ArgumentMatcher;

public class IsEmptyList<T> extends ArgumentMatcher<List<T>> {

	@Override
	public boolean matches(Object collection) {
		return ((List<?>) collection).isEmpty();
	}

}
