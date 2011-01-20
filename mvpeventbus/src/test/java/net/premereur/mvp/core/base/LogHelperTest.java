package net.premereur.mvp.core.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link LogHelper}.
 * @author gpremer
 *
 */
public class LogHelperTest {

    @Test
    public void shouldHandleNullArray() throws Exception {
        assertEquals("", LogHelper.formatArguments("pre", null));
    }

    @Test
    public void shouldHandleEmptyArray() throws Exception {
        assertEquals("", LogHelper.formatArguments("pre", new String[] {}));
    }

    @Test
    public void shouldHandleArrayWithOneArgument() throws Exception {
        assertEquals("pre(arg1)", LogHelper.formatArguments("pre", new String[] {"arg1"}));
    }

    @Test
    public void shouldHandleArrayWithTwoArguments() throws Exception {
        assertEquals("pre(arg1, arg2)", LogHelper.formatArguments("pre", new String[] {"arg1", "arg2"}));
    }

    @Test
    public void shouldHandleArrayWithNullArguments() throws Exception {
        assertEquals("pre(arg1, null)", LogHelper.formatArguments("pre", new String[] {"arg1", null}));
    }

}
