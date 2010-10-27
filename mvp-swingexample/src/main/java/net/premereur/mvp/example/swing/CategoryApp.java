package net.premereur.mvp.example.swing;

import net.premereur.mvp.core.guice.GuiceEventBusFactory;
import net.premereur.mvp.example.swing.application.ApplicationBus;
import net.premereur.mvp.example.swing.categorymgt.CategoryMgtBus;
import net.premereur.mvp.example.swing.categorymgt.CategoryMgtModule;
import net.premereur.mvp.example.swing.productmgt.ProductMgtBus;
import net.premereur.mvp.example.swing.productmgt.ProductMgtModule;

/**
 * The demo application itself.
 * 
 * @author gpremer
 * 
 */
public final class CategoryApp {

    public static void main(final String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GuiceEventBusFactory(new CategoryMgtModule(), new ProductMgtModule()).createEventBus(ApplicationBus.class, CategoryMgtBus.class,
                        ProductMgtBus.class).applicationStarted();
            }
        });
    }

    private CategoryApp() {

    }
}
