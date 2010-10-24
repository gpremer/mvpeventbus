package net.premereur.mvp.example.support.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.premereur.mvp.example.support.ClickHandler;

public class ClickHandlerActionListener implements ActionListener {

	private ClickHandler handler;

	public ClickHandlerActionListener(ClickHandler handler) {
		this.handler = handler;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		handler.click();
	}

}
