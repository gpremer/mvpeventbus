package net.premereur.mvp.example.vaadin.app;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class SampleApplication extends Application {

	@Override
	public void init() {
		Window mainWindow = new Window("Sample Vaadin MVP Application");
		Label label = new Label("Hello Vaadin user");
		mainWindow.addComponent(label);
		setMainWindow(mainWindow);
	}
}
