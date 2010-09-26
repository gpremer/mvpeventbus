package net.premereur.mvp.example.swing.application;

import javax.swing.JComponent;

import net.premereur.mvp.core.BasePresenter;
import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.swing.categorymgt.CategoryMgtBus;

@UsesView(ApplicationFrame.class)
public class ApplicationPresenter extends BasePresenter<ApplicationFrame, ApplicationBus> {

	public void onApplicationStarted() {
		ApplicationFrame view = getView();

		getEventBus(CategoryMgtBus.class).categoryListActivated();
		view.pack();
		view.setVisible(true);
	}

	public void onSetLeftComponent(JComponent component) {
		getView().setLeftComponent(component);
	}

	public void onSetCenterComponent(JComponent component) {
		getView().setCentralComponent(component);
	}

	public void onSetFeedback(String text) {
		getView().setFeedback(text);
		(new Thread() {
			@Override
			public void run() {
				try {
					sleep(3000);
				} catch (InterruptedException e) {

				} finally {
					getView().setFeedback("");
				}
			}
		}).start();
	}

}
