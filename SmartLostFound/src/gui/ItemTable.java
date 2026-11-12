package gui;

import model.Item;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class ItemTable extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Item> shownItems;

    public ItemTable() {
        shownItems = new ArrayList<>();
        setLayout(new BorderLayout());

        String[] columns = {"Type", "Name", "Category", "Location", "Date", "Status"};

        // Cells are read-only: users change records through buttons, not by typing in the table.
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // Replaces everything in the table with the given items.
    public void setItems(List<Item> items) {
        shownItems = new ArrayList<>(items);
        tableModel.setRowCount(0);

        for (Item item : items) {
            tableModel.addRow(new Object[]{
                    item.getItemType(),
                    item.getName(),
                    item.getCategory(),
                    item.getLocation(),
                    item.getDate(),
                    item.getStatus()
            });
        }
    }

    // Returns the selected Item, or null if no row is selected.
    public Item getSelectedItem() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return null;
        }
        return shownItems.get(row);
    }
}