package Toko;

public class PhysicalGame extends GameItem implements Sellable {
    private String edition;
    private String platform;

    public PhysicalGame(String itemId, String itemName, int year, int stock, double price, String edition, String platform) {
        super(itemId, itemName, year, stock, price);
        this.edition = edition;
        this.platform = platform;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public void displayItemDetails() {
        System.out.println("Physical Game: " + getItemName());
        System.out.println("Item ID: " + getItemId());
        System.out.println("Year: " + getYear());
        System.out.println("Stock: " + getStock());
        System.out.println("Price: $" + getPrice());
        System.out.println("Edition: " + edition);
        System.out.println("Platform: " + platform);
    }

    @Override
    public void updateQuantity(int quantity) {
        setStock(getStock() - quantity);
    }

    @Override
    public int getStock() {
        return super.getStock();
    }

    @Override
    public String getItemId() {
        return super.getItemId();
    }

    @Override
    public String getItemName() {
        return super.getItemName();
    }

}
