package net.premereur.mvp.example.swing.productmgt;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.premereur.mvp.core.View;

public class ProductSearchPanel extends JPanel implements View {

	private static final long serialVersionUID = 1L;

	public ProductSearchPanel() {
		initComponents();
	}

	private void initComponents() {
		add(new JLabel("product search"));		
	}
}
