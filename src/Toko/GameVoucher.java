package Toko;

import java.util.Date;

public class GameVoucher extends GameItem implements Sellable {
    private String marketPlatform;
    private int quantity;
    private Date validUntil;

    public GameVoucher(String itemId, String itemName, int year, int stock, double price, String marketPlatform, int quantity, Date validUntil) {
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

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
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
        setStock(getStock() + quantity);
    }
}