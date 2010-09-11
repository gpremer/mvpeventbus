package net.premereur.mvp.example.swing.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.premereur.mvp.core.View;
import net.premereur.mvp.example.swing.presenter.DefaultCategoryPanelPresenter;

public class DefaultCategoryPanel extends JPanel implements View {

	private static final long serialVersionUID = 1L;

	JButton createBtn = new JButton("create");

	public DefaultCategoryPanel() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		add(createBtn);
	}

	public void setCreateButtonListener(final DefaultCategoryPanelPresenter presenter) {
		for (ActionListener listener : createBtn.getActionListeners()) {
			createBtn.removeActionListener(listener);
		}
		createBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				presenter.createNewCategory();
			}

		});
	}

	protected void init() {
	}
}
