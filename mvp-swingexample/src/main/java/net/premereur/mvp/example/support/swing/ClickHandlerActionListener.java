package net.premereur.mvp.example.support.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.premereur.mvp.example.support.ClickHandler;

/**
 * A swing action listener that forwards it's clicks to a non-swing {@link ClickHandler}.
 * 
 * @author gpremer
 * 
 */
public final class ClickHandlerActionListener implements ActionListener {

    private ClickHandler handler;

    public ClickHandlerActionListener(final ClickHandler handler) {
        this.handler = handler;
    }

    @Override
    public final void actionPerformed(final ActionEvent e) {
        handler.click();
    }

}
