package Toko;

public abstract class GameItem {
    private final String itemId;
    private String itemName;
    private int year;
    private int stock;
    private double price;

    public GameItem(String itemId, String itemName, int year, int stock, double price) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.year = year;
        this.stock = stock;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public abstract void displayItemDetails();
}
