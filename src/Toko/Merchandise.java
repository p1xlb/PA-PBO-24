package Toko;

public class Merchandise extends GameItem implements Sellable {
    private String game;
    private String type;
    private String dimension;

    public Merchandise(String itemId, String itemName, int year, int stock, double price, String game, String type, String dimension) {
        super(itemId, itemName, year, stock, price);
        this.game = game;
        this.type = type;
        this.dimension = dimension;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    @Override
    public void displayItemDetails() {
        System.out.println("Merchandise: " + getItemName());
        System.out.println("Item ID: " + getItemId());
        System.out.println("Year: " + getYear());
        System.out.println("Stock: " + getStock());
        System.out.println("Price: $" + getPrice());
        System.out.println("Game: " + game);
        System.out.println("Type: " + type);
        System.out.println("Dimension: " + dimension);
    }

    @Override
    public void updateQuantity(int quantity) {
        setStock(getStock() + quantity);
    }
}
