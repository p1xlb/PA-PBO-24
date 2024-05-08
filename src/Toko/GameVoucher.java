package Toko;

public class GameVoucher extends GameItem implements Sellable {
    private String marketPlatform;
    private int quantity;
    private String validUntil;

    public GameVoucher(String itemId, String itemName, int year, int stock, double price, String marketPlatform, int quantity, String validUntil) {
        super(itemId, itemName, year, stock, price);
        this.marketPlatform = marketPlatform;
        this.quantity = quantity;
        this.validUntil = validUntil;
    }

    public String getMarketPlatform() {
        return marketPlatform;
    }

    public void setMarketPlatform(String marketPlatform) {
        this.marketPlatform = marketPlatform;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    @Override
    public void displayItemDetails() {
        System.out.println("Game Voucher: " + getItemName());
        System.out.println("Item ID: " + getItemId());
        System.out.println("Year: " + getYear());
        System.out.println("Stock: " + getStock());
        System.out.println("Price: $" + getPrice());
        System.out.println("Market Platform: " + marketPlatform);
        System.out.println("Quantity: " + quantity);
        System.out.println("Valid Until: " + validUntil);
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