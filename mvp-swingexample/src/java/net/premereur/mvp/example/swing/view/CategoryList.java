package net.premereur.mvp.example.swing.view;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.premereur.mvp.core.View;

public class CategoryList extends JPanel implements View {
	/**
	 * Class version.
	 */
	private static final long serialVersionUID = 1L;

	public CategoryList() {
		init();
	}

	private void init() {
		final String[] data = { "Category 1", "Category 2", "Category 3" };
		TableModel dm = new AbstractTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public String getColumnName(int column) {
				return "Name";
			}
			
			@Override
			public int getColumnCount() {
				return 1;
			}

			@Override
			public int getRowCount() {
				return data.length;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return data[rowIndex];
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}

		};
		JTable table = new JTable(dm);
        table.setPreferredScrollableViewportSize(new Dimension(150, 270));
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
		setOpaque(true);
	}
}
