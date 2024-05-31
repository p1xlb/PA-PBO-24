package Toko;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private List<CartItem> cart;

    public TransactionManager() {
        cart = new ArrayList<>();
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public void clearCart(){
        cart.clear();
    }

    public static class CartItem {
        private Sellable item;
        private int quantity;

        public CartItem(Sellable item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public Sellable getItem() {
            return item;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public void addToCart(Sellable item, int quantity) {
        if (item.getStock() >= quantity) {
            cart.add(new CartItem(item, quantity));
        } else {
            System.out.println("Stok tidak mencukupi untuk " + item.getItemName());
        }
    }

    public void removeFromCart(String itemId) {
        cart.removeIf(cartItem -> cartItem.getItem().getItemId().equals(itemId));
    }

    public void checkout(Inventory inventory) {
        double totalAmount = 0;
        for (CartItem cartItem : cart) {
            Sellable item = cartItem.getItem();
            int quantity = cartItem.getQuantity();
            totalAmount += item.getPrice() * quantity;
            item.updateQuantity(quantity);
            inventory.updateItem(item);
        }
        System.out.println("Total Amount: $" + totalAmount);
    }

    public void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Keranjang kosong.");
        } else {
            System.out.println("Isi Keranjang:");
            double totalAmount = 0;
            for (CartItem cartItem : cart) {
                Sellable item = cartItem.getItem();
                int quantity = cartItem.getQuantity();
                double itemTotal = item.getPrice() * quantity;
                totalAmount += itemTotal;

                System.out.println("Item: " + item.getItemName());
                System.out.println("Jumlah: " + quantity);
                System.out.println("Harga: $" + item.getPrice());
                System.out.println("Total: $" + itemTotal);
                System.out.println("---------------------");
            }
            System.out.println("Total Belanja: $" + totalAmount);
        }
    }

}