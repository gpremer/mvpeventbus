package net.premereur.mvp.example.swing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import net.premereur.mvp.core.View;

public class CategoryList extends JPanel implements View {
	/**
	 * Class version.
	 */
	private static final long serialVersionUID = 1L;
	private TableModel dataModel;

	public CategoryList() {
		init();
	}

	private void init() {
		dataModel = new TableModel();
		JTable table = new JTable(dataModel);
        table.setPreferredScrollableViewportSize(new Dimension(150, 270));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
		setOpaque(true); // Otherwise it doesn't show
	}
	
	public void showInFrame(JFrame frame) {
		frame.getContentPane().add(this, BorderLayout.LINE_START);
	}

	public void bind(List<String> data) {
		dataModel.setData(data);
	}
	
	static class TableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		
		private List<String> data;
		
		public void setData(List<String> data) {
			this.data = new ArrayList<String>();
			this.data.addAll(data);
			fireTableDataChanged();
		}
		
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
			return data.size();
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data.get(rowIndex);
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	}
}
