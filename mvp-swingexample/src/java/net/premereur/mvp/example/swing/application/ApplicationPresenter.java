package net.premereur.mvp.example.swing.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

import net.premereur.mvp.core.BasePresenter;
import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.support.ClickHandler;
import net.premereur.mvp.example.swing.categorymgt.CategoryMgtBus;
import net.premereur.mvp.example.swing.productmgt.ProductMgtBus;

@UsesView(ApplicationFrame.class)
public class ApplicationPresenter extends BasePresenter<ApplicationFrame, ApplicationBus> {

	public void onApplicationStarted() {
		ApplicationFrame view = getView();

		getEventBus(CategoryMgtBus.class).categoryMgtActivated();
		view.pack();
		view.setVisible(true);
		view.setExitListener(getExitClickHandler());
		view.setCategoryListener(getCategoryClickHandler());
		view.setProductListener(getProductClickHandler());
	}

	public void onSetLeftComponent(JComponent component) {
		getView().setLeftComponent(component);
	}

	public void onClearScreen() {
		getView().clearLeftComponent();
		getView().clearCentralComponent();
	}

	public void onSetCenterComponent(JComponent component) {
		getView().setCentralComponent(component);
	}

	public void onSetFeedback(final String text) {
		getView().setFeedback(text);
		Timer timer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getView().setFeedback("");

			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	public ClickHandler getExitClickHandler() {
		return new ClickHandler() {
			@Override
			public void click() {
				System.exit(0);
			}
		};
	}

	public ClickHandler getCategoryClickHandler() {
		return new ClickHandler() {
			@Override
			public void click() {
				getEventBus(CategoryMgtBus.class).categoryMgtActivated();
			}
		};
	}
	
	public ClickHandler getProductClickHandler() {
		return new ClickHandler() {
			@Override
			public void click() {
				getEventBus(ProductMgtBus.class).productMgtActivated();
			}
		};		
	}
}
