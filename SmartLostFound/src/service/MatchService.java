package service;

import model.FoundItem;
import model.Item;
import model.LostItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Finds possible Lost/Found pairs using a simple, explainable score. */
public class MatchService {

    /** Returns possible matches for the selected item, highest score first. */
    public ArrayList<MatchSuggestion> findMatches(Item target, List<Item> allItems) {
        ArrayList<MatchSuggestion> suggestions = new ArrayList<>();

        for (Item candidate : allItems) {
            if (candidate == target || candidate.getStatus().equalsIgnoreCase("Returned")) {
                continue;
            }
            if (!isOppositeReportType(target, candidate)) {
                continue;
            }

            int score = calculateScore(target, candidate);
            // A name similarity is required to avoid suggesting matches based only on place/category.
            if (score >= 30 && nameScore(target.getName(), candidate.getName()) > 0) {
                suggestions.add(new MatchSuggestion(candidate, score));
            }
        }

        suggestions.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        return suggestions;
    }

    private boolean isOppositeReportType(Item first, Item second) {
        return (first instanceof LostItem && second instanceof FoundItem)
                || (first instanceof FoundItem && second instanceof LostItem);
    }

    private int calculateScore(Item first, Item second) {
        int score = nameScore(first.getName(), second.getName());

        if (first.getCategory().equalsIgnoreCase(second.getCategory())) {
            score += 25;
        }
        if (first.getLocation().trim().equalsIgnoreCase(second.getLocation().trim())) {
            score += 25;
        }
        return Math.min(score, 100);
    }

    // Name: exact match = 50, one contains the other = 35, shared word = 20.
    private int nameScore(String first, String second) {
        String a = normalize(first);
        String b = normalize(second);

        if (a.isEmpty() || b.isEmpty()) {
            return 0;
        }
        if (a.equals(b)) {
            return 50;
        }
        if (a.contains(b) || b.contains(a)) {
            return 35;
        }

        for (String word : a.split(" ")) {
            if (word.length() > 1 && List.of(b.split(" ")).contains(word)) {
                return 20;
            }
        }
        return 0;
    }

    private String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    public static class MatchSuggestion {
        private final Item item;
        private final int score;

        public MatchSuggestion(Item item, int score) {
            this.item = item;
            this.score = score;
        }

        public Item getItem() {
            return item;
        }

        public int getScore() {
            return score;
        }
    }
}
