package net.premereur.mvp.example.swing.view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import net.premereur.mvp.core.View;
import net.premereur.mvp.example.domain.model.Category;
import net.premereur.mvp.example.swing.eventbus.DemoEventBus;

public class CategoryList extends JPanel implements View<DemoEventBus> {
	/**
	 * Class version.
	 */
	private static final long serialVersionUID = 1L;
	private TableModel dataModel;
	private JTable table;
	private List<Category> data = new ArrayList<Category>();
	private DemoEventBus eventBus;

	public CategoryList() {
		init();
	}

	private void init() {
		dataModel = new TableModel();
		table = new JTable(dataModel);
		table.setPreferredScrollableViewportSize(new Dimension(150, 270));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
		setOpaque(true); // Otherwise it doesn't show
		addSelectionListener();
	}

	@Override
	public void setEventBus(DemoEventBus eventBus) {
		this.eventBus = eventBus;
	}

	public DemoEventBus getEventBus() {
		return eventBus;
	}

	public void bind(List<Category> list) {
		this.data = new ArrayList<Category>(list);
		dataModel.fireTableDataChanged();
	}

	class TableModel extends AbstractTableModel {
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
			return data.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data.get(rowIndex).getName();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	}

	private void addSelectionListener() {
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					Category selectedCategory = getSelectedCategory();
					if (selectedCategory != null) {
						getEventBus().categorySelected(selectedCategory);
					}
				}
			}

		});

	}

	private Category getSelectedCategory() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			return null;
		}
		return data.get(selectedRow);
	}

	public void refreshList() {
		dataModel.fireTableDataChanged();
	}
}
