package net.premereur.mvp.core.base;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

/**
 * Tests the {@link VerificationException} class.
 * 
 * @author gpremer
 * 
 */
public class VerificationExceptionTest {

    @Test
    public void shouldTakeAListOfVerificationFailureMessages() throws Exception {
        Collection<String> failures = Arrays.asList("message 1", "message 2");
        new VerificationException(failures);
    }

    @Test
    public void shouldShowItselfWithAllFailureMessages() throws Exception {
        Collection<String> failures = Arrays.asList("message 1", "message 2");
        VerificationException ve = new VerificationException(failures);
        assertTrue(ve.toString().contains("message 2"));
    }

}
