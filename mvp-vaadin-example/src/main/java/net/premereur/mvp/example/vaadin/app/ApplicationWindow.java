package net.premereur.mvp.example.vaadin.app;

import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import net.premereur.mvp.core.View;

@SuppressWarnings("serial")
public class ApplicationWindow extends Window implements View {

	public ApplicationWindow() {
		super("Sample Vaadin MVP Application");
		final Label label = new Label("Hello Vaadin user");
		addComponent(label);
	}
}
