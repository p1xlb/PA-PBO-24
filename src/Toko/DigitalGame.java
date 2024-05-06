package Toko;

public class DigitalGame extends GameItem implements Sellable {
    private double downloadSize;
    private String platform;

    public DigitalGame(String itemId, String itemName, int year, int stock, double price, double downloadSize, String platform) {
        super(itemId, itemName, year, stock, price);
        this.downloadSize = downloadSize;
        this.platform = platform;
    }

    public double getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(double downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public void displayItemDetails() {
        System.out.println("Digital Game: " + getItemName());
        System.out.println("Item ID: " + getItemId());
        System.out.println("Year: " + getYear());
        System.out.println("Stock: " + getStock());
        System.out.println("Price: $" + getPrice());
        System.out.println("Download Size: " + downloadSize + " GB");
        System.out.println("Platform: " + platform);
    }

    @Override
    public void updateQuantity(int quantity) {
        setStock(getStock() + quantity);
    }
}
