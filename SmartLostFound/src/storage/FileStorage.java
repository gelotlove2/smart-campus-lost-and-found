package storage;

import model.FoundItem;
import model.Item;
import model.LostItem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {

    private Path filePath;

    public FileStorage(String fileName) {
        filePath = Paths.get(fileName);
    }

    // Writes every item as one line in the file.
    public void saveItems(List<Item> items) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        for (Item item : items) {
            lines.add(toLine(item));
        }
        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    // Reads the file and rebuilds the items. A missing file gives an empty list.
    public ArrayList<Item> loadItems() throws IOException {
        ArrayList<Item> items = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return items;
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        for (String line : lines) {
            Item item = fromLine(line);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    // Line format: TYPE|name|category|location|date|status
    private String toLine(Item item) {
        String type = (item instanceof LostItem) ? "LOST" : "FOUND";
        return String.join("|",
                type,
                escape(item.getName()),
                escape(item.getCategory()),
                escape(item.getLocation()),
                escape(item.getDate()),
                escape(item.getStatus()));
    }

    // Returns null for a damaged line so one bad line does not stop the loading.
    private Item fromLine(String line) {
        if (line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\|", -1);
        if (parts.length != 6) {
            return null;
        }

        String type = parts[0];
        String name = unescape(parts[1]);
        String category = unescape(parts[2]);
        String location = unescape(parts[3]);
        String date = unescape(parts[4]);
        String status = unescape(parts[5]);

        Item item;
        if (type.equals("LOST")) {
            item = new LostItem(name, category, location, date);
        } else if (type.equals("FOUND")) {
            item = new FoundItem(name, category, location, date);
        } else {
            return null;
        }

        item.setStatus(status);
        return item;
    }

    // Makes text safe to store: the separator and line breaks are replaced by codes.
    private String escape(String text) {
        return text.replace("\\", "\\\\")
                .replace("|", "\\p")
                .replace("\r", "")
                .replace("\n", "\\n");
    }

    // Reverses escape(), reading the text one character at a time.
    private String unescape(String text) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\\' && i + 1 < text.length()) {
                char next = text.charAt(i + 1);
                if (next == 'p') {
                    result.append('|');
                } else if (next == 'n') {
                    result.append('\n');
                } else {
                    result.append(next);
                }
                i++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}