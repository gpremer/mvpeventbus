package net.premereur.mvp;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class TestBase {

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

}
