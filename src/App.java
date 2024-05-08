import Toko.*;

public class App {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        TransactionManager transactionManager = new TransactionManager();

        // Menambahkan beberapa item ke dalam inventory
        inventory.addItem(new DigitalGame("DG001", "Game A", 2022, 100, 59.99, 10.5, "PC"));
        inventory.addItem(new PhysicalGame("PG001", "Game B", 2021, 50, 49.99, "Deluxe Edition", "PlayStation 5"));
        inventory.addItem(new Merchandise("MC001", "T-Shirt", 2023, 200, 19.99, "Game A", "Clothing", "M"));
        inventory.addItem(new GameVoucher("GV001", "Steam Wallet Code", 2023, 75, 25.00, "Steam", 25, "25/12/2024" )); // Tanggal valid sampai 31 Desember 2024

        boolean exit = false;
        while (!exit) {
            System.out.println("\nMenu Utama:");
            System.out.println("1. Lihat Inventory");
            System.out.println("2. Tambah Item");
            System.out.println("3. Ubah Item");
            System.out.println("4. Lakukan Transaksi");
            System.out.println("5. Keluar");
            System.out.print("Masukkan pilihan: ");

            int choice = Integer.parseInt(System.console().readLine());

            switch (choice) {
                case 1:
                    viewInventory(inventory);
                    break;
                case 2:
                    addItem(inventory);
                    break;
                case 3:
                    updateItem(inventory);
                    break;
                case 4:
                    performTransaction(inventory, transactionManager);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }



    private static void performTransaction(Inventory inventory, TransactionManager transactionManager) {
        boolean checkout = false;
        while (!checkout) {
            System.out.println("\nMenu Transaksi:");
            System.out.println("1. Tambah Item ke Keranjang");
            System.out.println("2. Hapus Item dari Keranjang");
            System.out.println("3. Lihat Isi Keranjang");
            System.out.println("4. Checkout");
            System.out.println("5. Kembali");
            System.out.print("Masukkan pilihan: ");

            int choice = Integer.parseInt(System.console().readLine());

            switch (choice) {
                case 1:
                    addItemToCart(inventory, transactionManager);
                    break;
                case 2:
                    removeItemFromCart(transactionManager);
                    break;
                case 3:
                    transactionManager.viewCart();
                    break;
                case 4:
                    transactionManager.checkout(inventory);
                    checkout = true;
                    break;
                case 5:
                    checkout = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }



    private static void viewInventory(Inventory inventory) {
        System.out.println("\nPilih kategori item yang ingin ditampilkan:");
        System.out.println("1. Semua Kategori");
        System.out.println("2. DigitalGame");
        System.out.println("3. PhysicalGame");
        System.out.println("4. Merchandise");
        System.out.println("5. GameVoucher");
        System.out.print("Masukkan pilihan: ");

        int choice = Integer.parseInt(System.console().readLine());
        String category;

        switch (choice) {
            case 1:
                category = "all";
                break;
            case 2:
                category = "DigitalGame";
                break;
            case 3:
                category = "PhysicalGame";
                break;
            case 4:
                category = "Merchandise";
                break;
            case 5:
                category = "GameVoucher";
                break;
            default:
                System.out.println("Pilihan tidak valid!");
                return;
        }

        inventory.displayInventory(category);
    }
    
    private static void addItem(Inventory inventory){
        System.out.print("Masukkan kategori item (DigitalGame, PhysicalGame, Merchandise, GameVoucher): ");
        String category = System.console().readLine();

        switch (category.toLowerCase()) {
            case "digitalgame":
                addDigitalGame(inventory);
                break;
            case "physicalgame":
                addPhysicalGame(inventory);
                break;
            case "merchandise":
                addMerchandise(inventory);
                break;
            case "gamevoucher":
                addGameVoucher(inventory);
                break;
            default:
                System.out.println("Kategori tidak valid!");
        }
    }

    private static void addDigitalGame(Inventory inventory) {
        System.out.print("Masukkan ID item: ");
        String itemId = System.console().readLine();
        System.out.print("Masukkan nama item: ");
        String itemName = System.console().readLine();
        System.out.print("Masukkan tahun rilis: ");
        int year = Integer.parseInt(System.console().readLine());
        System.out.print("Masukkan stok: ");
        int stock = Integer.parseInt(System.console().readLine());
        System.out.print("Masukkan harga: ");
        double price = Double.parseDouble(System.console().readLine());
        System.out.print("Masukkan ukuran download (GB): ");
        double downloadSize = Double.parseDouble(System.console().readLine());
        System.out.print("Masukkan platform: ");
        String platform = System.console().readLine();
    
        DigitalGame game = new DigitalGame(itemId, itemName, year, stock, price, downloadSize, platform);
        inventory.addItem(game);
        System.out.println("Item berhasil ditambahkan.");
    }

    private static void addPhysicalGame(Inventory inventory) {
        System.out.print("Masukkan ID item: ");
        String itemId = System.console().readLine();
        System.out.print("Masukkan nama item: ");
        String itemName = System.console().readLine();
        System.out.print("Masukkan tahun rilis: ");
        int year = Integer.parseInt(System.console().readLine());
        System.out.print("Masukkan stok: ");
        int stock = Integer.parseInt(System.console().readLine());
        System.out.print("Masukkan harga: ");
        double price = Double.parseDouble(System.console().readLine());
        System.out.print("Masukkan edisi: ");
        String edition = System.console().readLine();
        System.out.print("Masukkan platform: ");
        String platform = System.console().readLine();
    
        PhysicalGame game = new PhysicalGame(itemId, itemName, year, stock, price, edition, platform);
        inventory.addItem(game);
        System.out.println("Item berhasil ditambahkan.");
    }

    private static void addMerchandise(Inventory inventory) {
        System.out.print("Masukkan ID item: ");
        String itemId = System.console().readLine();
        System.out.print("Masukkan nama item: ");
        String itemName = System.console().readLine();
        System.out.print("Masukkan tahun rilis: ");
        int year = Integer.parseInt(System.console().readLine());
        System.out.print("Masukkan stok: ");
        int stock = Integer.parseInt(System.console().readLine());
        System.out.print("Masukkan harga: ");
        double price = Double.parseDouble(System.console().readLine());
        System.out.print("Masukkan game: ");
        String game = System.console().readLine();
        System.out.print("Masukkan tipe: ");
        String type = System.console().readLine();
        System.out.print("Masukkan dimensi: ");
        String dimension = System.console().readLine();
    
        Merchandise merchandise = new Merchandise(itemId, itemName, year, stock, price, game, type, dimension);
        inventory.addItem(merchandise);
        System.out.println("Item berhasil ditambahkan.");
    }

    private static void addGameVoucher(Inventory inventory) {
        System.out.print("Masukkan ID item: ");
        String itemId = System.console().readLine();
        System.out.print("Masukkan nama item: ");
        String itemName = System.console().readLine();
        System.out.print("Masukkan tahun rilis: ");
        int year = Integer.parseInt(System.console().readLine());
        System.out.print("Masukkan stok: ");
        int stock = Integer.parseInt(System.console().readLine());
        System.out.print("Masukkan harga: ");
        double price = Double.parseDouble(System.console().readLine());
        System.out.print("Masukkan platform: ");
        String platform = System.console().readLine();
        System.out.print("Masukkan jumlah: ");
        int quantity = Integer.parseInt(System.console().readLine());
        System.out.print("Masukkan tanggal valid sampai: ");
        String validUntil = System.console().readLine();
    
        GameVoucher voucher = new GameVoucher(itemId, itemName, year, stock, price, platform, quantity, validUntil);
        inventory.addItem(voucher);
        System.out.println("Item berhasil ditambahkan.");
    }
    

    private static void updateItem(Inventory inventory){
        System.out.print("Masukkan ID item yang ingin diubah: ");
        String itemId = System.console().readLine();
    
        GameItem item = inventory.searchItem(itemId);
        if (item == null) {
            System.out.println("Item tidak ditemukan dalam inventory.");
            return;
        }
    
        System.out.print("Masukkan nama item baru (Enter untuk tidak mengubah): ");
        String newItemName = System.console().readLine();
        if (!newItemName.isEmpty()) {
            item.setItemName(newItemName);
        }
    
        System.out.print("Masukkan tahun rilis baru (Enter untuk tidak mengubah): ");
        String newYearInput = System.console().readLine();
        if (!newYearInput.isEmpty()) {
            int newYear = Integer.parseInt(newYearInput);
            item.setYear(newYear);
        }
    
        System.out.print("Masukkan stok baru (Enter untuk tidak mengubah): ");
        String newStockInput = System.console().readLine();
        if (!newStockInput.isEmpty()) {
            int newStock = Integer.parseInt(newStockInput);
            item.setStock(newStock);
        }
    
        System.out.print("Masukkan harga baru (Enter untuk tidak mengubah): ");
        String newPriceInput = System.console().readLine();
        if (!newPriceInput.isEmpty()) {
            double newPrice = Double.parseDouble(newPriceInput);
            item.setPrice(newPrice);
        }
    
        // Tambahkan prompt untuk mengubah detail spesifik sesuai kategori item
        if (item instanceof DigitalGame) {
            DigitalGame game = (DigitalGame) item;
            System.out.print("Masukkan ukuran download baru (Enter untuk tidak mengubah): ");
            String newDownloadSizeInput = System.console().readLine();
            if (!newDownloadSizeInput.isEmpty()) {
                double newDownloadSize = Double.parseDouble(newDownloadSizeInput);
                game.setDownloadSize(newDownloadSize);
            }
    
            System.out.print("Masukkan platform baru (Enter untuk tidak mengubah): ");
            String newPlatform = System.console().readLine();
            if (!newPlatform.isEmpty()) {
                game.setPlatform(newPlatform);
            }
        } else if (item instanceof PhysicalGame) {
            PhysicalGame game = (PhysicalGame) item;
            System.out.print("Masukkan edisi baru (Enter untuk tidak mengubah): ");
            String newEdition = System.console().readLine();
            if (!newEdition.isEmpty()) {
                game.setEdition(newEdition);
            }
    
            System.out.print("Masukkan platform baru (Enter untuk tidak mengubah): ");
            String newPlatform = System.console().readLine();
            if (!newPlatform.isEmpty()) {
                game.setPlatform(newPlatform);
            }
        } else if (item instanceof Merchandise) {
            Merchandise merchandise = (Merchandise) item;
            System.out.print("Masukkan game baru (Enter untuk tidak mengubah): ");
            String newGame = System.console().readLine();
            if (!newGame.isEmpty()) {
                merchandise.setGame(newGame);
            }
    
            System.out.print("Masukkan tipe baru (Enter untuk tidak mengubah): ");
            String newType = System.console().readLine();
            if (!newType.isEmpty()) {
                merchandise.setType(newType);
            }
    
            System.out.print("Masukkan dimensi baru (Enter untuk tidak mengubah): ");
            String newDimension = System.console().readLine();
    
        inventory.updateItem((Sellable) item);
        System.out.println("Item berhasil diperbarui.");
        }
    
    }

    private static void addItemToCart(Inventory inventory, TransactionManager transactionManager) {
        System.out.print("Masukkan ID item: ");
        String itemId = System.console().readLine();
        GameItem item = inventory.searchItem(itemId);
        if (item != null) {
            System.out.print("Masukkan jumlah: ");
            int quantity = Integer.parseInt(System.console().readLine());
            transactionManager.addToCart((Sellable) item, quantity);
        } else {
            System.out.println("Item tidak ditemukan dalam inventory.");
        }
    }

    private static void removeItemFromCart(TransactionManager transactionManager) {
        System.out.print("Masukkan ID item yang akan dihapus dari keranjang: ");
        String itemId = System.console().readLine();
        transactionManager.removeFromCart(itemId);
    }
}