package net.premereur.mvp.example.swing.presenter;

import javax.swing.JComponent;

import net.premereur.mvp.core.BasePresenter;
import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.swing.eventbus.DemoEventBus;
import net.premereur.mvp.example.swing.view.ApplicationFrame;

@UsesView(ApplicationFrame.class)
public class ApplicationPresenter extends BasePresenter<ApplicationFrame, DemoEventBus> {

	public void onApplicationStarted() {
		ApplicationFrame view = getView();

		getEventBus().categoryListActivated();
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
