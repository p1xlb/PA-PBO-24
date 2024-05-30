import java.sql.*;

import Toko.*;

public class App {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/PBO_PA";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static Connection connection;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the database connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            Inventory inventory = new Inventory();
            TransactionManager transactionManager = new TransactionManager();

            // Load items from the database
            loadInventoryFromDatabase(inventory);

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database connection
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
            String query = "SELECT * FROM GameItems";
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
            String query = "SELECT * FROM DigitalGames WHERE ItemId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, itemId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double downloadSize = resultSet.getDouble("DownloadSize");
                String platform = resultSet.getString("Platform");
                return new DigitalGame(itemId, itemName, year, stock, price, downloadSize, platform);
            }

            query = "SELECT * FROM PhysicalGames WHERE ItemId = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, itemId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String edition = resultSet.getString("Edition");
                String platform = resultSet.getString("Platform");
                return new PhysicalGame(itemId, itemName, year, stock, price, edition, platform);
            }

            query = "SELECT * FROM Merchandise WHERE ItemId = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, itemId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String game = resultSet.getString("Game");
                String type = resultSet.getString("Type");
                String dimension = resultSet.getString("Dimension");
                return new Merchandise(itemId, itemName, year, stock, price, game, type, dimension);
            }

            query = "SELECT * FROM GameVouchers WHERE ItemId = ?";
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
                    saveTransaction(transactionManager);
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
        saveItemToDatabase(game);
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
        saveItemToDatabase(game);
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
        saveItemToDatabase(merchandise);
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
        saveItemToDatabase(voucher);
        System.out.println("Item berhasil ditambahkan.");
    }

    private static void updateItem(Inventory inventory) {
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
            // Prepare the INSERT statement for the GameItems table
            String query = "INSERT INTO GameItems (ItemId, ItemName, Year, Stock, Price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, item.getItemId());
            statement.setString(2, item.getItemName());
            statement.setInt(3, item.getYear());
            statement.setInt(4, item.getStock());
            statement.setDouble(5, item.getPrice());
            statement.executeUpdate();

            // Save item-specific details to respective tables
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
            String query = "INSERT INTO DigitalGames (ItemId, DownloadSize, Platform) VALUES (?, ?, ?)";
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
            String query = "INSERT INTO PhysicalGames (ItemId, Edition, Platform) VALUES (?, ?, ?)";
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
            String query = "INSERT INTO Merchandise (ItemId, Game, Type, Dimension) VALUES (?, ?, ?, ?)";
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
            String query = "INSERT INTO GameVouchers (ItemId, MarketPlatform, Quantity, ValidUntil) VALUES (?, ?, ?, ?)";
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
            // Update the GameItems table
            String query = "UPDATE GameItems SET ItemName = ?, Year = ?, Stock = ?, Price = ? WHERE ItemId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, item.getItemName());
            statement.setInt(2, item.getYear());
            statement.setInt(3, item.getStock());
            statement.setDouble(4, item.getPrice());
            statement.setString(5, item.getItemId());
            statement.executeUpdate();

            // Update item-specific details in respective tables
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
            String query = "UPDATE DigitalGames SET DownloadSize = ?, Platform = ? WHERE ItemId = ?";
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
            String query = "UPDATE PhysicalGames SET Edition = ?, Platform = ? WHERE ItemId = ?";
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
            String query = "UPDATE Merchandise SET Game = ?, Type = ?, Dimension = ? WHERE ItemId = ?";
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
            String query = "UPDATE GameVouchers SET MarketPlatform = ?, Quantity = ?, ValidUntil = ? WHERE ItemId = ?";
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
            // Insert a new transaction into the Transactions table
            String query = "INSERT INTO Transactions (TransactionDate) VALUES (CURRENT_DATE())";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
    
            // Get the generated TransactionId
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int transactionId = 0;
            if (generatedKeys.next()) {
                transactionId = generatedKeys.getInt(1);
            }
    
            // Insert transaction items into the TransactionItems table
            for (TransactionManager.CartItem cartItem : transactionManager.getCart()) {
                Sellable item = cartItem.getItem();
                int quantity = cartItem.getQuantity();
                double totalPrice = item.getPrice() * quantity;
    
                query = "INSERT INTO TransactionItems (TransactionId, ItemId, Quantity, TotalPrice) VALUES (?, ?, ?, ?)";
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
}