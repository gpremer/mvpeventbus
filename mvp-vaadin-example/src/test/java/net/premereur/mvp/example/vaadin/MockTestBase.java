package net.premereur.mvp.example.vaadin;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public abstract class MockTestBase {

    @Before
    public void initMocks() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
}
