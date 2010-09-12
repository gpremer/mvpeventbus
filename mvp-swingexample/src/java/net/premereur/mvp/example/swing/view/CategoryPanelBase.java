package net.premereur.mvp.example.swing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.premereur.mvp.core.View;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.presenter.SingleCategoryPresenterBase;

public abstract class CategoryPanelBase extends JPanel implements View {
	private final JLabel operationLabel;
	private final JButton cancelButton = new JButton("Cancel");
	private final JButton saveButton = new JButton("Save");
	private final JTextField nameField = new JTextField(20);
	private Category category;

	public CategoryPanelBase(final String operationText) {
		operationLabel = new JLabel(operationText);
		init();
	}

	private void init() {
		JPanel fieldPane = new JPanel();
		fieldPane.add(new JLabel("name"));
		fieldPane.add(nameField);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(saveButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);

		setLayout(new BorderLayout());
		add(operationLabel, BorderLayout.PAGE_START);
		add(fieldPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END);
	}
	
	public void bind(Category selectedCategory) {
		this.category = selectedCategory;
		nameField.setText(this.category.getName());
	}
	public void setSaveButtonListener(final SingleCategoryPresenterBase<? extends CategoryPanelBase> presenter) {
		this.saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveButtonClicked(presenter);
			}
		});
	}
	
	protected void saveButtonClicked(final SingleCategoryPresenterBase<? extends CategoryPanelBase> presenter) {
		category.setName(nameField.getText());
		presenter.saveClicked(category);		
	}

	public void setCancelButtonListener(final SingleCategoryPresenterBase<? extends CategoryPanelBase> presenter) {
		this.cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				presenter.cancelClicked();
			}
		});
	}

}
