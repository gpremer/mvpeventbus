package net.premereur.mvp.example.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ProductTest {

	@Test
	public void shouldAcceptAName() throws Exception {
		new Product("a name");
	}
	
	@Test
	public void shouldAssignAnId() throws Exception {
		Product product = new Product("a name");
		assertNotNull(product.getId());
	}
	
	@Test
	public void shouldChangeItsName() throws Exception {
		Product product = new Product("a name");
		product.setName("new name");
		assertEquals("new name", product.getName());
	}
}
