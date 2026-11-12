package service;

import model.Item;

import java.util.ArrayList;

public class ItemManager {

    private ArrayList<Item> items;

    public ItemManager() {
        items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public ArrayList<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    public int getItemCount() {
        return items.size();
    }

    // Finds items whose name contains the keyword (ignores upper/lower case).
    public ArrayList<Item> searchByName(String keyword) {
        ArrayList<Item> results = new ArrayList<>();
        String lowerKeyword = keyword.trim().toLowerCase();

        for (Item item : items) {
            if (item.getName().toLowerCase().contains(lowerKeyword)) {
                results.add(item);
            }
        }
        return results;
    }

    public ArrayList<Item> filterByCategory(String category) {
        ArrayList<Item> results = new ArrayList<>();

        for (Item item : items) {
            if (item.getCategory().equalsIgnoreCase(category.trim())) {
                results.add(item);
            }
        }
        return results;
    }

    public ArrayList<Item> filterByLocation(String location) {
        ArrayList<Item> results = new ArrayList<>();

        for (Item item : items) {
            if (item.getLocation().equalsIgnoreCase(location.trim())) {
                results.add(item);
            }
        }
        return results;
    }

    public ArrayList<Item> filterByStatus(String status) {
        ArrayList<Item> results = new ArrayList<>();

        for (Item item : items) {
            if (item.getStatus().equalsIgnoreCase(status.trim())) {
                results.add(item);
            }
        }
        return results;
    }

    // Applies all four conditions together. An empty keyword or location,
    // or the value "All" for category/status, means "do not filter on this".
    public ArrayList<Item> searchAndFilter(String keyword, String category,
                                           String location, String status) {
        ArrayList<Item> results = new ArrayList<>();
        String lowerKeyword = keyword.trim().toLowerCase();
        String lowerLocation = location.trim().toLowerCase();

        for (Item item : items) {
            boolean nameMatches = item.getName().toLowerCase().contains(lowerKeyword);
            boolean categoryMatches = category.equals("All")
                    || item.getCategory().equalsIgnoreCase(category);
            boolean locationMatches = item.getLocation().toLowerCase().contains(lowerLocation);
            boolean statusMatches = status.equals("All")
                    || item.getStatus().equalsIgnoreCase(status);

            if (nameMatches && categoryMatches && locationMatches && statusMatches) {
                results.add(item);
            }
        }
        return results;
    }

    // Changes an item's status. Only Lost, Found, or Returned are allowed.
    public void updateStatus(Item item, String newStatus) {
        if (!isValidStatus(newStatus)) {
            throw new IllegalArgumentException("Status must be Lost, Found, or Returned.");
        }
        item.setStatus(newStatus);
    }

    private boolean isValidStatus(String status) {
        return status.equals("Lost")
                || status.equals("Found")
                || status.equals("Returned");
    }
}