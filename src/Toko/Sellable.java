package Toko;

public interface Sellable {
    double getPrice();
    void updateQuantity(int quantity);
    int getStock(); 
    String getItemId(); 
    String getItemName(); 

}