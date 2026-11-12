package gui;

import model.FoundItem;
import model.Item;
import model.LostItem;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ItemForm extends JDialog {

    private JComboBox<String> typeBox;
    private JTextField nameField;
    private JComboBox<String> categoryBox;
    private JTextField locationField;
    private JTextField dateField;

    private Item createdItem;

    public ItemForm(JFrame parent) {
        super(parent, "Register Item", true);

        String[] types = {"Lost", "Found"};
        String[] categories = {"Electronics", "Accessories", "Documents", "Books",
                "Stationery", "Clothing", "Keys", "Other"};

        typeBox = new JComboBox<>(types);
        nameField = new JTextField();
        categoryBox = new JComboBox<>(categories);
        locationField = new JTextField();
        dateField = new JTextField(LocalDate.now().toString());

        JPanel fields = new JPanel(new GridLayout(5, 2, 10, 10));
        fields.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        fields.add(new JLabel("Report type:"));
        fields.add(typeBox);
        fields.add(new JLabel("Item name:"));
        fields.add(nameField);
        fields.add(new JLabel("Category:"));
        fields.add(categoryBox);
        fields.add(new JLabel("Location:"));
        fields.add(locationField);
        fields.add(new JLabel("Date (yyyy-MM-dd):"));
        fields.add(dateField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancelButton);
        buttons.add(saveButton);

        setLayout(new BorderLayout());
        add(fields, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setSize(420, 300);
        setLocationRelativeTo(parent);
    }

    // Returns the new item, or null if the user cancelled.
    public Item getCreatedItem() {
        return createdItem;
    }

    private void onSave() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String dateText = dateField.getText().trim();

        if (name.isEmpty()) {
            showError("Item name is required.");
            return;
        }
        if (location.isEmpty()) {
            showError("Location is required.");
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            showError("Date must be in yyyy-MM-dd format, for example 2026-10-28.");
            return;
        }
        if (date.isAfter(LocalDate.now())) {
            showError("Date cannot be in the future.");
            return;
        }

        String category = (String) categoryBox.getSelectedItem();
        String type = (String) typeBox.getSelectedItem();

        if (type.equals("Lost")) {
            createdItem = new LostItem(name, category, location, date.toString());
        } else {
            createdItem = new FoundItem(name, category, location, date.toString());
        }
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Invalid Input", JOptionPane.WARNING_MESSAGE);
    }
}