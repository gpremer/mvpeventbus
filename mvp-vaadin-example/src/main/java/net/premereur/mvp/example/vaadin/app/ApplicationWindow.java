package net.premereur.mvp.example.vaadin.app;

import net.premereur.mvp.core.View;

import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class ApplicationWindow extends Window implements View {

	public ApplicationWindow() {
		super("Sample Vaadin MVP Application");
	}
}
