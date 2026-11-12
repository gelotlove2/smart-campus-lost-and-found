package model;

public abstract class Item {

    private String name;
    private String category;
    private String location;
    private String date;
    private String status;

    public Item(String name, String category, String location,
                String date, String status) {
        this.name = name;
        this.category = category;
        this.location = location;
        this.date = date;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public abstract String getItemType();
}