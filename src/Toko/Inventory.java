package Toko;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<GameItem> itemList;

    public Inventory() {
        itemList = new ArrayList<>();
    }

    public void addItem(GameItem item) {
        itemList.add(item);
    }

    public void removeItem(String itemId) {
        itemList.removeIf(item -> item.getItemId().equals(itemId));
    }

    public void updateItem(Sellable item) {
        GameItem gameItem = (GameItem) item;
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getItemId().equals(gameItem.getItemId())) {
                itemList.set(i, gameItem);
                break;
            }
        }
    }


    public GameItem searchItem(String itemId) {
        for (GameItem item : itemList) {
            if (item.getItemId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    public void displayInventory(String category) {
        if (category.equalsIgnoreCase("all")) {
            for (GameItem item : itemList) {
                item.displayItemDetails();
                System.out.println("---------------------");
            }
        } else {
            for (GameItem item : itemList) {
                if (item.getClass().getSimpleName().equalsIgnoreCase(category)) {
                    item.displayItemDetails();
                    System.out.println("---------------------");
                }
            }
        }
    }
}