package model;

public class LostItem extends Item {

    public LostItem(String name, String category, String location,
                    String date) {
        super(name, category, location, date, "Lost");
    }

    @Override
    public String getItemType() {
        return "Lost Item";
    }
}