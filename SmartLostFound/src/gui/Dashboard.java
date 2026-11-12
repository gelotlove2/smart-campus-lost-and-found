package gui;

import model.Item;
import service.ItemManager;
import service.MatchService;
import storage.FileStorage;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;

public class Dashboard extends JFrame {

    private ItemManager manager;
    private FileStorage storage;
    private ItemTable itemTable;
    private MatchService matchService;

    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JTextField locationFilter;
    private JComboBox<String> statusFilter;
    private JLabel countLabel;
    private JLabel statsLabel;

    public Dashboard() {
        manager = new ItemManager();
        storage = new FileStorage("items.txt");
        itemTable = new ItemTable();
        matchService = new MatchService();
        countLabel = new JLabel();
        statsLabel = new JLabel();

        setTitle("Smart Campus Lost & Found");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildTopPanel(), BorderLayout.NORTH);
        add(itemTable, BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        loadSavedItems();
        refreshTable();
    }

    // Heading on top, filter bar below it.
    private JPanel buildTopPanel() {
        JLabel heading = new JLabel("Smart Campus Lost & Found");
        heading.setFont(new Font("SansSerif", Font.BOLD, 22));
        heading.setBorder(BorderFactory.createEmptyBorder(12, 16, 4, 16));

        searchField = new JTextField(10);
        locationFilter = new JTextField(8);

        String[] categories = {"All", "Electronics", "Accessories", "Documents", "Books",
                "Stationery", "Clothing", "Keys", "Other"};
        String[] statuses = {"All", "Lost", "Found", "Returned"};
        categoryFilter = new JComboBox<>(categories);
        statusFilter = new JComboBox<>(statuses);

        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchButton.addActionListener(e -> refreshTable());
        searchField.addActionListener(e -> refreshTable());
        locationFilter.addActionListener(e -> refreshTable());
        categoryFilter.addActionListener(e -> refreshTable());
        statusFilter.addActionListener(e -> refreshTable());
        clearButton.addActionListener(e -> clearFilters());

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filterBar.add(new JLabel("Name:"));
        filterBar.add(searchField);
        filterBar.add(new JLabel("Category:"));
        filterBar.add(categoryFilter);
        filterBar.add(new JLabel("Location:"));
        filterBar.add(locationFilter);
        filterBar.add(new JLabel("Status:"));
        filterBar.add(statusFilter);
        filterBar.add(searchButton);
        filterBar.add(clearButton);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        heading.setAlignmentX(0.0f);
        filterBar.setAlignmentX(0.0f);
        top.add(heading);
        top.add(filterBar);
        return top;
    }

    // Action buttons and the "Showing X of Y records" label.
    private JPanel buildBottomPanel() {
        JButton registerButton = new JButton("Register Item");
        JButton updateButton = new JButton("Update Status");
        JButton matchButton = new JButton("Find Matches");

        registerButton.addActionListener(e -> registerItem());
        updateButton.addActionListener(e -> updateSelectedStatus());
        matchButton.addActionListener(e -> showMatches());

        countLabel.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(registerButton);
        bottom.add(updateButton);
        bottom.add(matchButton);
        bottom.add(countLabel);
        bottom.add(statsLabel);
        return bottom;
    }

    private void loadSavedItems() {
        try {
            ArrayList<Item> loaded = storage.loadItems();
            for (Item item : loaded) {
                manager.addItem(item);
            }
        } catch (IOException e) {
            showFileError("Could not read saved records: " + e.getMessage());
        }
    }

    // Reads the current filter values, asks the manager, and shows the result.
    private void refreshTable() {
        ArrayList<Item> shown = manager.searchAndFilter(
                searchField.getText(),
                (String) categoryFilter.getSelectedItem(),
                locationFilter.getText(),
                (String) statusFilter.getSelectedItem());

        itemTable.setItems(shown);
        countLabel.setText("Showing " + shown.size() + " of "
                + manager.getItemCount() + " records");
        updateStatistics();
    }

    private void clearFilters() {
        searchField.setText("");
        locationFilter.setText("");
        categoryFilter.setSelectedIndex(0);
        statusFilter.setSelectedIndex(0);
        refreshTable();
    }

    // Opens the form. If the user saved a new item, add it, save the file, refresh the table.
    private void registerItem() {
        ItemForm form = new ItemForm(this);
        form.setVisible(true);

        Item newItem = form.getCreatedItem();
        if (newItem == null) {
            return;
        }

        manager.addItem(newItem);
        saveAllItems();
        refreshTable();
    }

    // Asks for a new status for the selected row, then saves and refreshes.
    private void updateSelectedStatus() {
        Item selected = itemTable.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a row first.",
                    "No Row Selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options = {"Lost", "Found", "Returned"};
        String choice = (String) JOptionPane.showInputDialog(this,
                "New status for \"" + selected.getName() + "\":",
                "Update Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                selected.getStatus());

        if (choice == null) {
            return;
        }

        try {
            manager.updateStatus(selected, choice);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Invalid Status", JOptionPane.WARNING_MESSAGE);
            return;
        }

        saveAllItems();
        refreshTable();
    }


    // Displays rule-based suggestions for the selected report.
    private void showMatches() {
        Item selected = itemTable.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a row first.",
                    "No Row Selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ArrayList<MatchService.MatchSuggestion> matches =
                matchService.findMatches(selected, manager.getAllItems());
        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No potential matches found for this item.",
                    "Potential Matches", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder message = new StringBuilder("Possible matches for \"")
                .append(selected.getName()).append("\":\n\n");
        for (MatchService.MatchSuggestion suggestion : matches) {
            Item match = suggestion.getItem();
            message.append(suggestion.getScore()).append("% match — ")
                    .append(match.getItemType()).append(": ")
                    .append(match.getName()).append(" | ")
                    .append(match.getCategory()).append(" | ")
                    .append(match.getLocation()).append(" | Status: ")
                    .append(match.getStatus()).append("\n");
        }
        message.append("\nThese are suggestions only. Please verify a match manually.");
        JOptionPane.showMessageDialog(this, message.toString(),
                "Potential Matches", JOptionPane.INFORMATION_MESSAGE);
    }

    // Counts report statuses across all records, not just currently visible rows.
    private void updateStatistics() {
        int lost = 0;
        int found = 0;
        int returned = 0;
        for (Item item : manager.getAllItems()) {
            if (item.getStatus().equalsIgnoreCase("Lost")) {
                lost++;
            } else if (item.getStatus().equalsIgnoreCase("Found")) {
                found++;
            } else if (item.getStatus().equalsIgnoreCase("Returned")) {
                returned++;
            }
        }
        statsLabel.setText("Lost: " + lost + " | Found: " + found
                + " | Returned: " + returned);
    }

    private void saveAllItems() {
        try {
            storage.saveItems(manager.getAllItems());
        } catch (IOException e) {
            showFileError("Could not save records: " + e.getMessage());
        }
    }

    private void showFileError(String message) {
        JOptionPane.showMessageDialog(this, message,
                "File Error", JOptionPane.ERROR_MESSAGE);
    }
}