package net.premereur.mvp.example.swing.categorymgt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import net.premereur.mvp.core.View;
import net.premereur.mvp.example.domain.model.Category;

/**
 * Base class for views that update single Categories.
 * 
 * @author gpremer
 * 
 */
// NOCS MAGIC 400
public abstract class CategoryPanelBase extends JPanel implements View {
    private Category category;
    private JButton cancelButton = new JButton("Cancel");
    private JButton saveButton = new JButton("Save");
    private JLabel operationLabel = new JLabel();
    private JTextField nameField = new JTextField();
    private JLabel prodSelLabel = new JLabel();
    private JLabel nameLabel;

    public CategoryPanelBase(final String operationText) {
        init(operationText);
    }

    private void init(final String operationText) {
        saveButton = new JButton();
        cancelButton = new JButton();
        operationLabel = new JLabel();
        nameLabel = new JLabel();
        nameField = new JTextField();
        prodSelLabel = new JLabel();

        saveButton.setText("Save");
        cancelButton.setText("Cancel");
        operationLabel.setText(operationText);
        nameLabel.setText("name");
        nameLabel.setFocusable(false);
        prodSelLabel.setText("Product selection");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addContainerGap().addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                                layout.createSequentialGroup().addGap(12, 12, 12).addComponent(nameField, GroupLayout.PREFERRED_SIZE, 223,
                                        GroupLayout.PREFERRED_SIZE)).addComponent(operationLabel).addComponent(nameLabel).addGroup(
                                layout.createSequentialGroup().addComponent(saveButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(
                                        cancelButton))).addContainerGap(153, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addContainerGap().addComponent(operationLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(nameField, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(192, 192, 192).addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(saveButton).addComponent(cancelButton))
                        .addContainerGap()));
    }

    public void bind(final Category selectedCategory) {
        this.category = selectedCategory;
        nameField.setText(this.category.getName());
    }

    public void setSaveButtonListener(final SingleCategoryPresenterBase<? extends CategoryPanelBase> presenter) {
        this.saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
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
            public void actionPerformed(final ActionEvent e) {
                presenter.cancelClicked();
            }
        });
    }

}
