import java.sql.*;

import Toko.*;

import java.io.IOException;

public class App {
    // Kredensial database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/PBO_PA";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static Connection connection;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Koneksi ke Database
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            Inventory inventory = new Inventory();
            TransactionManager transactionManager = new TransactionManager();

            // Masukkan item-item yang ada di database ke dalam inventory
            loadInventoryFromDatabase(inventory);

            boolean exit = false;
            while (!exit) {
                System.out.println("\nMenu Utama:");
                System.out.println("1. Menu Manajemen Inventory");
                System.out.println("2. Menu Transaksi");
                System.out.println("3. Lihat History ");
                System.out.println("0. Keluar");
                System.out.print("Masukkan pilihan: ");
                try {
                    int choice = Integer.parseInt(System.console().readLine());

                    switch (choice) {
                        case 1:
                            clearConsole();
                            managementItemsMenu(inventory);
                            break;
                        case 2:
                            clearConsole();
                            performTransaction(inventory, transactionManager);
                            break;
                        case 3:
                            printHistory();
                            break;
                        case 0:
                            System.out.println("Selamat Tinggal!!!");
                            exit = true;
                            break;
                        default:
                            System.out.println("Pilihan tidak valid!");
                    }
                } catch (Exception e) {
                    System.out.println("Plihan tidak valid!");
                }


            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Tutup koneksi database
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void loadInventoryFromDatabase(Inventory inventory) {
        try {
            String query = "SELECT * FROM gameitems";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String itemId = resultSet.getString("ItemId");
                String itemName = resultSet.getString("ItemName");
                int year = resultSet.getInt("Year");
                int stock = resultSet.getInt("Stock");
                double price = resultSet.getDouble("Price");

                GameItem item = loadItemDetails(itemId, itemName, year, stock, price);
                if (item != null) {
                    inventory.addItem(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static GameItem loadItemDetails(String itemId, String itemName, int year, int stock, double price) {
        try {
            String query = "SELECT * FROM digitalgames WHERE ItemId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, itemId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double downloadSize = resultSet.getDouble("DownloadSize");
                String platform = resultSet.getString("Platform");
                return new DigitalGame(itemId, itemName, year, stock, price, downloadSize, platform);
            }

            query = "SELECT * FROM physicalgames WHERE ItemId = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, itemId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String edition = resultSet.getString("Edition");
                String platform = resultSet.getString("Platform");
                return new PhysicalGame(itemId, itemName, year, stock, price, edition, platform);
            }

            query = "SELECT * FROM merchandise WHERE ItemId = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, itemId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String game = resultSet.getString("Game");
                String type = resultSet.getString("Type");
                String dimension = resultSet.getString("Dimension");
                return new Merchandise(itemId, itemName, year, stock, price, game, type, dimension);
            }

            query = "SELECT * FROM gamevouchers WHERE ItemId = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, itemId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String platform = resultSet.getString("MarketPlatform");
                int quantity = resultSet.getInt("Quantity");
                String validUntil = resultSet.getString("ValidUntil");
                return new GameVoucher(itemId, itemName, year, stock, price, platform, quantity, validUntil);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void managementItemsMenu(Inventory inventory) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nMenu Manajemen Inventory");
            System.out.println("1. Lihat Inventory");
            System.out.println("2. Tambah Item");
            System.out.println("3. Ubah Item");
            System.out.println("4. Hapus Item");
            System.out.println("0. Kembali");
            System.out.println("Masukkan pilihan: ");
            
            int choice = Integer.parseInt(System.console().readLine());

            switch (choice) {
                case 1:
                    clearConsole();
                    viewInventory(inventory);
                    break;
                case 2:
                    clearConsole();
                    addItem(inventory);
                    break;
                case 3:
                    clearConsole();
                    updateItem(inventory);
                    break;
                case 4:
                    viewInventory(inventory);
                    deleteItem(inventory);
                    break;
                case 0:
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
                    viewInventory(inventory);
                    addItemToCart(inventory, transactionManager);
                    break;
                case 2:
                    transactionManager.viewCart();
                    removeItemFromCart(transactionManager);
                    break;
                case 3:
                    transactionManager.viewCart();
                    break;
                case 4:
                    transactionManager.checkout(inventory);
                    saveTransaction(transactionManager);
                    transactionManager.clearCart();
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

    private static void addItem(Inventory inventory) {
        System.out.println("\nPilih kategori item yang ingin ditambahkan:");
        System.out.println("1. DigitalGame");
        System.out.println("2. PhysicalGame");
        System.out.println("3. Merchandise");
        System.out.println("4. GameVoucher");
        System.out.print("Masukkan pilihan: ");

        int choice = Integer.parseInt(System.console().readLine());
        
        switch (choice) {
            case 1:
                
                addDigitalGame(inventory);
                break;
            case 2:
                
                addPhysicalGame(inventory);
                break;
            case 3:
                
                addMerchandise(inventory);
                break;
            case 4:
                
                addGameVoucher(inventory);
                break;
            default:
                System.out.println("Pilihan tidak valid!");
                return;
        }
    }

    private static void addDigitalGame(Inventory inventory) {
        System.out.print("Masukkan ID item: ");
        String itemId = System.console().readLine();
        boolean isExist = isIdExist(itemId);
        if (isExist) {
            System.out.println("ID " + itemId + " Sudah dipakai");
            return;
        } else {
            System.out.print("Masukkan nama item: ");
            String itemName = System.console().readLine();
            int year = inputYear();
            int stock = inputStock();
            double price = inputPrice();
            double downloadSize = inputDownloadSize();
            System.out.print("Masukkan platform: ");
            String platform = System.console().readLine();
            DigitalGame game = new DigitalGame(itemId, itemName, year, stock, price, downloadSize, platform);
            inventory.addItem(game);
            saveItemToDatabase(game);
            System.out.println("Item berhasil ditambahkan.");
        }
    }

    private static void addPhysicalGame(Inventory inventory) {
        System.out.print("Masukkan ID item: ");
        String itemId = System.console().readLine();
        boolean isExist = isIdExist(itemId);
        if (isExist) {
            System.out.println("ID " + itemId + " sudah dipakai.");
            return;
        } else {
            System.out.print("Masukkan nama item: ");
            String itemName = System.console().readLine();
            int year = inputYear();
            int stock = inputStock();
            double price = inputPrice();
            System.out.print("Masukkan edisi: ");
            String edition = System.console().readLine();
            System.out.print("Masukkan platform: ");
            String platform = System.console().readLine();
    
            PhysicalGame game = new PhysicalGame(itemId, itemName, year, stock, price, edition, platform);
            inventory.addItem(game);
            saveItemToDatabase(game);
            System.out.println("Item berhasil ditambahkan.");
        }
    }

    private static void addMerchandise(Inventory inventory) {
        System.out.print("Masukkan ID item: ");
        String itemId = System.console().readLine();
        boolean isExist = isIdExist(itemId);
        if (isExist) {
            System.out.println("ID " + itemId + " sudah dipakai.");
            return;
        } else {
            System.out.print("Masukkan nama item: ");
            String itemName = System.console().readLine();
            int year = inputYear();
            int stock = inputStock();
            double price = inputPrice();
            System.out.print("Masukkan game: ");
            String game = System.console().readLine();
            System.out.print("Masukkan tipe: ");
            String type = System.console().readLine();
            System.out.print("Masukkan dimensi: ");
            String dimension = System.console().readLine();
    
            Merchandise merchandise = new Merchandise(itemId, itemName, year, stock, price, game, type, dimension);
            inventory.addItem(merchandise);
            saveItemToDatabase(merchandise);
            System.out.println("Item berhasil ditambahkan.");
        }
    }

    private static void addGameVoucher(Inventory inventory) {
        System.out.print("Masukkan ID item: ");
        String itemId = System.console().readLine();
        boolean isExist = isIdExist(itemId);
        if (isExist) {
            System.out.println("ID " + itemId + " sudah dipakai.");
            return;
        } else {
            System.out.print("Masukkan nama item: ");
            String itemName = System.console().readLine();
            int year = inputYear();
            int stock = inputStock();
            double price = inputPrice();
            System.out.print("Masukkan platform: ");
            String platform = System.console().readLine();
            System.out.print("Masukkan jumlah: ");
            int quantity = Integer.parseInt(System.console().readLine());
            System.out.print("Masukkan tanggal valid sampai: ");
            String validUntil = System.console().readLine();
    
            GameVoucher voucher = new GameVoucher(itemId, itemName, year, stock, price, platform, quantity, validUntil);
            inventory.addItem(voucher);
            saveItemToDatabase(voucher);
            System.out.println("Item berhasil ditambahkan.");
        }
    }

    private static void updateItem(Inventory inventory) {
        viewInventory(inventory); //menampilkan isi inventory
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
        String newYearInput;
        boolean validInput = false;
        int newYear = item.getYear(); 
            
        while (!validInput) {
            newYearInput = System.console().readLine();
            if (newYearInput.isEmpty()) {
                validInput = true; 
            } 
            else {
                try {
                    if (Integer.parseInt(newYearInput) <= 0) {
                        System.out.println("Input tahun tidak valid. Tahun tidak boleh negatif.");
                        System.out.print("Masukkan tahun rilis baru (Enter untuk tidak mengubah): ");
                    } else {
                        newYear = Integer.parseInt(newYearInput);
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input tahun tidak valid. Silakan masukkan angka saja.");
                    System.out.print("Masukkan tahun rilis baru (Enter untuk tidak mengubah): ");
                }
            }
        }
        if (validInput) {
            item.setYear(newYear);
        }
        
        System.out.print("Masukkan stok baru (Enter untuk tidak mengubah): ");
        String newStockInput = System.console().readLine();
        int newStock = item.getStock();
        boolean validInputStock = false;
        while (!validInputStock) {
            if (newStockInput.isEmpty()) {
                validInputStock = true;
            } 
                        
            else {
                try {
                    if (Integer.parseInt(newStockInput) < 0) {
                        System.out.println("Input stok tidak valid. Stok tidak boleh negatif.");
                        System.out.print("Masukkan stok baru (Enter untuk tidak mengubah): ");
                    } else {
                        newStock = Integer.parseInt(newStockInput);
                        validInputStock = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input stok tidak valid. Silakan masukkan angka saja.");
                    System.out.print("Masukkan stok baru (Enter untuk tidak mengubah): ");
                }
            }
        }
        if (validInputStock) {
            item.setStock(newStock);
        }

        System.out.print("Masukkan harga baru (Enter untuk tidak mengubah): ");
        String newPriceInput;
        boolean validInputPrice = false;
        double newPrice = item.getPrice(); 

        while (!validInputPrice) {
            newPriceInput = System.console().readLine();
            if (newPriceInput.isEmpty()) {
                validInputPrice = true; 
            } else {
                try {
                    newPrice = Double.parseDouble(newPriceInput);
                    if (newPrice <= 0) {
                        System.out.println("Harga tidak boleh bernilai negatif atau nol. Silakan masukkan harga yang valid.");
                        System.out.print("Masukkan harga baru (Enter untuk tidak mengubah): ");
                    } else {
                        validInputPrice = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input harga tidak valid. Silakan masukkan angka saja.");
                    System.out.print("Masukkan harga baru (Enter untuk tidak mengubah): ");
                }
            }
        }
        if (validInputPrice) {
            item.setPrice(newPrice);
        }

        // penambahan prompt untuk mengubah detail spesifik sesuai kategori item
        if (item instanceof DigitalGame) {
            DigitalGame game = (DigitalGame) item;
            System.out.print("Masukkan ukuran download baru (Enter untuk tidak mengubah): ");
            String newDownloadSizeInput = System.console().readLine();
            boolean validInputDownloadSize = false;
            double newDownloadSize = game.getDownloadSize();

            while (!validInputDownloadSize) {
                if (newDownloadSizeInput.isEmpty()) {
                    validInputDownloadSize = true;
                } else {
                    try {
                        newDownloadSize = Double.parseDouble(newDownloadSizeInput);
                        if (newDownloadSize <= 0) {
                            System.out.println("Ukuran download tidak boleh bernilai negatif atau nol. Silakan masukkan ukuran download yang valid.");
                            System.out.print("Masukkan ukuran download baru (Enter untuk tidak mengubah): ");
                        } else {
                            validInputDownloadSize = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input ukuran download tidak valid. Silakan masukkan angka saja.");
                        System.out.print("Masukkan ukuran download baru (Enter untuk tidak mengubah): ");
                    }
                }
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
            if (!newDimension.isEmpty()) {
                merchandise.setDimension(newDimension);
            }
        } else if (item instanceof GameVoucher) {
            GameVoucher voucher = (GameVoucher) item;
            System.out.print("Masukkan platform baru (Enter untuk tidak mengubah): ");
            String newPlatform = System.console().readLine();
            if (!newPlatform.isEmpty()) {
                voucher.setMarketPlatform(newPlatform);
            }

            System.out.print("Masukkan jumlah baru (Enter untuk tidak mengubah): ");
            String newQuantityInput = System.console().readLine();
            if (!newQuantityInput.isEmpty()) {
                int newQuantity = Integer.parseInt(newQuantityInput);
                voucher.setQuantity(newQuantity);
            }

            System.out.print("Masukkan tanggal valid sampai baru (Enter untuk tidak mengubah): ");
            String newValidUntil = System.console().readLine();
            if (!newValidUntil.isEmpty()) {
                voucher.setValidUntil(newValidUntil);
            }
        }

        inventory.updateItem((Sellable) item);
        updateItemInDatabase(item);
        System.out.println("Item berhasil diperbarui.");
    }

    private static void deleteItem(Inventory inventory){
        System.out.print("Masukkan ID item yang ingin dihapus: ");
        String itemId = System.console().readLine();

        GameItem item = inventory.searchItem(itemId);
        if (item == null) {
            System.out.println("Item tidak ditemukan dalam inventory.");
            return;
        }
        
        //hapus dari inventory
        inventory.removeItem(itemId);

        //hapus dari database
        PreparedStatement statement = null;
        try {
            String[] deleteQueries = {
                "DELETE FROM physicalgames WHERE ItemId = ?",
                "DELETE FROM digitalgames WHERE ItemId = ?",
                "DELETE FROM gamevouchers WHERE ItemId = ?",
                "DELETE FROM merchandise WHERE ItemId = ?",
                "DELETE FROM transactionitems WHERE ItemId = ?"
            };
    
            for (String query : deleteQueries) {
                statement = connection.prepareStatement(query);
                statement.setString(1, itemId);
                statement.executeUpdate();
                statement.close();
            }

            String query = "DELETE FROM gameitems WHERE ItemId = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, itemId);
            statement.executeUpdate();
            
            System.out.println("Item dengan ID " + itemId + " berhasil di hapus");
        } catch (Exception e) {
            e.printStackTrace();
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

    private static void saveItemToDatabase(GameItem item) {
        try {
            // Statement INSERT ke tabel gameitems
            String query = "INSERT INTO gameitems (ItemId, ItemName, Year, Stock, Price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, item.getItemId());
            statement.setString(2, item.getItemName());
            statement.setInt(3, item.getYear());
            statement.setInt(4, item.getStock());
            statement.setDouble(5, item.getPrice());
            statement.executeUpdate();

            // Simpan detail item ke tabel masing masing
            if (item instanceof DigitalGame) {
                saveDigitalGameDetails((DigitalGame) item);
            } else if (item instanceof PhysicalGame) {
                savePhysicalGameDetails((PhysicalGame) item);
            } else if (item instanceof Merchandise) {
                saveMerchandiseDetails((Merchandise) item);
            } else if (item instanceof GameVoucher) {
                saveGameVoucherDetails((GameVoucher) item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveDigitalGameDetails(DigitalGame game) {
        try {
            String query = "INSERT INTO digitalgames (ItemId, DownloadSize, Platform) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, game.getItemId());
            statement.setDouble(2, game.getDownloadSize());
            statement.setString(3, game.getPlatform());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void savePhysicalGameDetails(PhysicalGame game) {
        try {
            String query = "INSERT INTO physicalgames (ItemId, Edition, Platform) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, game.getItemId());
            statement.setString(2, game.getEdition());
            statement.setString(3, game.getPlatform());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveMerchandiseDetails(Merchandise merchandise) {
        try {
            String query = "INSERT INTO merchandise (ItemId, Game, Type, Dimension) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, merchandise.getItemId());
            statement.setString(2, merchandise.getGame());
            statement.setString(3, merchandise.getType());
            statement.setString(4, merchandise.getDimension());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveGameVoucherDetails(GameVoucher voucher) {
        try {
            String query = "INSERT INTO gamevouchers (ItemId, MarketPlatform, Quantity, ValidUntil) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, voucher.getItemId());
            statement.setString(2, voucher.getMarketPlatform());
            statement.setInt(3, voucher.getQuantity());
            statement.setString(4, voucher.getValidUntil());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateItemInDatabase(GameItem item) {
        try {
            // Update tabel gameitems
            String query = "UPDATE gameitems SET ItemName = ?, Year = ?, Stock = ?, Price = ? WHERE ItemId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, item.getItemName());
            statement.setInt(2, item.getYear());
            statement.setInt(3, item.getStock());
            statement.setDouble(4, item.getPrice());
            statement.setString(5, item.getItemId());
            statement.executeUpdate();

            // Update item sesuai tabel masing masing
            if (item instanceof DigitalGame) {
                updateDigitalGameDetails((DigitalGame) item);
            } else if (item instanceof PhysicalGame) {
                updatePhysicalGameDetails((PhysicalGame) item);
            } else if (item instanceof Merchandise) {
                updateMerchandiseDetails((Merchandise) item);
            } else if (item instanceof GameVoucher) {
                updateGameVoucherDetails((GameVoucher) item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateDigitalGameDetails(DigitalGame game) {
        try {
            String query = "UPDATE digitalgames SET DownloadSize = ?, Platform = ? WHERE ItemId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDouble(1, game.getDownloadSize());
            statement.setString(2, game.getPlatform());
            statement.setString(3, game.getItemId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updatePhysicalGameDetails(PhysicalGame game) {
        try {
            String query = "UPDATE physicalgames SET Edition = ?, Platform = ? WHERE ItemId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, game.getEdition());
            statement.setString(2, game.getPlatform());
            statement.setString(3, game.getItemId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateMerchandiseDetails(Merchandise merchandise) {
        try {
            String query = "UPDATE merchandise SET Game = ?, Type = ?, Dimension = ? WHERE ItemId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, merchandise.getGame());
            statement.setString(2, merchandise.getType());
            statement.setString(3, merchandise.getDimension());
            statement.setString(4, merchandise.getItemId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateGameVoucherDetails(GameVoucher voucher) {
        try {
            String query = "UPDATE gamevouchers SET MarketPlatform = ?, Quantity = ?, ValidUntil = ? WHERE ItemId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, voucher.getMarketPlatform());
            statement.setInt(2, voucher.getQuantity());
            statement.setString(3, voucher.getValidUntil());
            statement.setString(4, voucher.getItemId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveTransaction(TransactionManager transactionManager) {
        try {
            // Memasukkan transaksi baru ke tabel Transactions
            String query = "INSERT INTO transactions (TransactionDate) VALUES (CURRENT_DATE())";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
    
            // Mengambil generated id dari transaksi yang baru saja dimasukkan
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int transactionId = 0;
            if (generatedKeys.next()) {
                transactionId = generatedKeys.getInt(1);
            }
            
            // Memasukkan detail item transaksi ke tabel transactionitems
            for (TransactionManager.CartItem cartItem : transactionManager.getCart()) {
                Sellable item = cartItem.getItem();
                int quantity = cartItem.getQuantity();
                double totalPrice = item.getPrice() * quantity;
                
                query = "INSERT INTO transactionitems (TransactionId, ItemId, Quantity, TotalPrice) VALUES (?, ?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setInt(1, transactionId);
                statement.setString(2, item.getItemId());
                statement.setInt(3, quantity);
                statement.setDouble(4, totalPrice);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printHistory(){
        try {
            String query = "SELECT * FROM `transactionitems` join transactions on transactions.TransactionId = transactionitems.TransactionId join gameitems on gameitems.ItemId = transactionitems.ItemId ORDER BY `transactionitems`.`TransactionId` ASC";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int temp = 0;
            while (resultSet.next()) {
                int transactionId = resultSet.getInt("TransactionId");
                String transactionDate = resultSet.getString("TransactionDate");
                if(transactionId != temp){
                    System.out.println("-------------------------------------");
                    System.out.println("Transaction ID." + transactionId + "     Date." + transactionDate);
                    System.out.println("Items:");
                }
                String itemId = resultSet.getString("transactionitems.ItemId");
                String itemName = resultSet.getString("gameitems.ItemName");
                int itemQuantity = resultSet.getInt("transactionitems.Quantity");
                double price = resultSet.getDouble("transactionitems.TotalPrice");
                System.out.println("[" + itemId + "] " + itemName + "(" + itemQuantity + ") : $" + price);
                temp = transactionId;
            }
            System.out.println("-------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int inputYear(){
        while (true) {
            System.out.print("Masukkan tahun rilis: ");
            String yearInput = System.console().readLine();
            if (yearInput.isEmpty()) {
                System.out.println("Data tidak boleh kosong");
            } 
            else {
                try {
                    if (Integer.parseInt(yearInput) <= 0) {
                        System.out.println("Input tahun tidak valid. Tahun tidak boleh negatif atau nol.");
                    } else {
                        return Integer.parseInt(yearInput);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input tahun tidak valid. Silakan masukkan angka saja.");
                }
            }
        }
    }

    private static int inputStock(){
        while (true) {
            System.out.print("Masukkan stok: ");
            String stokInput = System.console().readLine();
            if (stokInput.isEmpty()) {
                System.out.println("Data tidak boleh kosong");
            } 
            else {
                try {
                    if (Integer.parseInt(stokInput) < 0) {
                        System.out.println("Input stok tidak valid. Stok tidak boleh negatif.");
                    } else {
                        return Integer.parseInt(stokInput);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input stok tidak valid. Silakan masukkan angka saja.");
                }
            }
        }
    }    

    private static double inputPrice(){
        while (true) {
            System.out.print("Masukkan harga: ");
            String priceInput = System.console().readLine();
            if (priceInput.isEmpty()) {
                System.out.println("Data tidak boleh kosong");
            } 
            else {
                try {
                    if (Double.parseDouble(priceInput) <= 0) {
                        System.out.println("Input harga tidak valid. Harga tidak boleh negatif atau nol.");
                    } else {
                        return Double.parseDouble(priceInput);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input harga tidak valid. Silakan masukkan angka saja.");
                }
            }
        }
    }    

    private static double inputDownloadSize(){
        while (true) {
            System.out.print("Masukkan ukuran download (GB): ");
            String downloadSizeInput = System.console().readLine();
            if (downloadSizeInput.isEmpty()) {
                System.out.println("Data tidak boleh kosong");
            } 
            else {
                try {
                    if (Double.parseDouble(downloadSizeInput) <= 0) {
                        System.out.println("Input Size tidak valid. Size tidak boleh negatif atau nol.");
                    } else {
                        return Double.parseDouble(downloadSizeInput);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input Size tidak valid. Silakan masukkan angka saja.");
                }
            }
        }
    }    

    //cek apakah id sudah ada di database
    public static boolean isIdExist(String idToCheck) {
        String query = "SELECT COUNT(*) FROM gameitems WHERE itemId = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, idToCheck);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //void untuk membersihkan terminal
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                // Command for Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Command for Unix/Linux/MacOS
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error clearing console: " + ex.getMessage());
        }
    }
}