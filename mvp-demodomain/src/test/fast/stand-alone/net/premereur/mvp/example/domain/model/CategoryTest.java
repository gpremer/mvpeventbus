package net.premereur.mvp.example.domain.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CategoryTest {

	@Test
	public void shouldAcceptAName() throws Exception {
		new Category("a name");
	}
	
	@Test
	public void shouldAssignAnId() throws Exception {
		Category category = new Category("a name");
		assertNotNull(category.getId());
	}
}
