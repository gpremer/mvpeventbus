package net.premereur.mvp.example.domain.model;

import org.junit.Test;

public class CategoryTest {

	@Test
	public void shouldAcceptAName() throws Exception {
		new Category("a name");
	}
}
