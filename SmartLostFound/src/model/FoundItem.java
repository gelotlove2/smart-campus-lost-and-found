package model;

public class FoundItem extends Item {

    public FoundItem(String name, String category, String location,
                     String date) {
        super(name, category, location, date, "Found");
    }

    @Override
    public String getItemType() {
        return "Found Item";
    }
}