package net.premereur.mvp.example.swing.productmgt;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import net.premereur.mvp.core.View;
import net.premereur.mvp.example.domain.model.Product;

//NOCS MAGIC 400
public class ProductSearchPanel extends JPanel implements View {

    private final List<Product> products = new ArrayList<Product>();

    private class ProductListTabelModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public int getRowCount() {
            return products.size();
        }

        @Override
        public String getColumnName(final int column) {
            return "name";
        }

        @Override
        public Object getValueAt(final int rowIndex, final int columnIndex) {
            return products.get(rowIndex).getName();
        }

    }

    private static final long serialVersionUID = 1L;

    private ProductListTabelModel productListTableModel = new ProductListTabelModel();

    public ProductSearchPanel() {
        initComponents();
    }

    public void setNameChangeListener(final ProductSearchPresenter presenter) {
        nameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(final java.awt.event.KeyEvent evt) {
                presenter.searchForName(nameField.getText());
            }
        });

    }

    public void setProducts(final List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        productListTableModel.fireTableDataChanged();
    }

    private void initComponents() {
        titleLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        resultLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();

        titleLabel.setText("Search for products");
        titleLabel.setName("titleLabel"); // NOI18N

        nameLabel.setText("Name");
        nameLabel.setName("nameLabel"); // NOI18N

        nameField.setName("nameField"); // NOI18N

        resultLabel.setText("Found products");
        resultLabel.setName("jLabel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        resultTable.setModel(productListTableModel);
        resultTable.setName("resultTable"); // NOI18N
        jScrollPane1.setViewportView(resultTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                                layout.createSequentialGroup().addContainerGap().addGroup(
                                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                                                layout.createSequentialGroup().addGap(12, 12, 12).addComponent(nameLabel).addPreferredGap(
                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(nameField,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(
                                                titleLabel).addComponent(resultLabel))).addGroup(
                                layout.createSequentialGroup().addGap(24, 24, 24).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364,
                                        Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addContainerGap().addComponent(titleLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18).addComponent(resultLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                                jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE).addContainerGap()));
    }

    private javax.swing.JLabel resultLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTable resultTable;
    private javax.swing.JLabel titleLabel;

}
