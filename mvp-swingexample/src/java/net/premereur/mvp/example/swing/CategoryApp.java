package net.premereur.mvp.example.swing;

import net.premereur.mvp.core.basic.BasicEventBusFactory;
import net.premereur.mvp.example.swing.application.ApplicationBus;
import net.premereur.mvp.example.swing.categorymgt.CategoryMgtBus;
import net.premereur.mvp.example.swing.productmgt.ProductMgtBus;

public class CategoryApp {
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new BasicEventBusFactory().createEventBus(ApplicationBus.class, CategoryMgtBus.class, ProductMgtBus.class).applicationStarted();
			}
		});
	}
}
