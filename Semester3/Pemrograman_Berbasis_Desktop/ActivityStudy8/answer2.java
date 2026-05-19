import java.util.Scanner;

class Menu {
    private String name;
    private String category;
    private int price;

    public Menu(String name, String category, int price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

public class answer2 {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Menu[] menus = new Menu[50];
    private static int menuCount = 0;

    public static void main(String[] args) {
        initMenu();
        while (true) {
            printHeader();
            System.out.println("1. Pesan Makanan/Minuman");
            System.out.println("2. Manajemen Menu Restoran");
            System.out.println("3. Keluar");
            System.out.print("Pilih menu utama [1-3]: ");

            int pilihan = readInt(1, 3);
            if (pilihan == 1) {
                pesanMenu();
            } else if (pilihan == 2) {
                manajemenMenu();
            } else {
                System.out.println("Terima kasih sudah menggunakan aplikasi restoran.");
                break;
            }
        }
    }

    private static void initMenu() {
        menus[menuCount++] = new Menu("Nasi Goreng", "makanan", 25000);
        menus[menuCount++] = new Menu("Ayam Bakar", "makanan", 30000);
        menus[menuCount++] = new Menu("Mie Rebus", "makanan", 22000);
        menus[menuCount++] = new Menu("Sate Ayam", "makanan", 28000);
        menus[menuCount++] = new Menu("Teh Manis", "minuman", 8000);
        menus[menuCount++] = new Menu("Es Jeruk", "minuman", 12000);
        menus[menuCount++] = new Menu("Kopi Hitam", "minuman", 15000);
        menus[menuCount++] = new Menu("Jus Alpukat", "minuman", 18000);
    }

    private static void printHeader() {
        System.out.println("============================================");
        System.out.println(" Aplikasi Restoran Sederhana");
        System.out.println("============================================");
    }

    private static void pesanMenu() {
        if (menuCount == 0) {
            System.out.println("Daftar menu kosong. Silakan tambahkan menu terlebih dahulu.");
            return;
        }

        int[] orderIndexes = new int[50];
        int[] orderQuantities = new int[50];
        int[] displayToMenuIndex = new int[menuCount];
        int orderCount = 0;

        System.out.println("\n--- Daftar Menu Restoran ---");
        int displayCount = printMenuByCategory(displayToMenuIndex);
        System.out.println("Ketik 'selesai' jika pesanan telah selesai.");

        while (true) {
            System.out.print("Masukkan nomor menu yang ingin dipesan atau 'selesai': ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("selesai")) {
                break;
            }

            int nomor;
            try {
                nomor = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Silakan masukkan nomor menu yang benar atau 'selesai'.");
                continue;
            }

            if (nomor < 1 || nomor > displayCount) {
                System.out.println("Pilihan menu tidak ditemukan. Silakan coba lagi.");
                continue;
            }

            System.out.print("Jumlah pesanan: ");
            int jumlah = readInt(1, Integer.MAX_VALUE);

            int menuIndex = displayToMenuIndex[nomor - 1];
            boolean found = false;
            for (int i = 0; i < orderCount; i++) {
                if (orderIndexes[i] == menuIndex) {
                    orderQuantities[i] += jumlah;
                    found = true;
                    break;
                }
            }
            if (!found) {
                orderIndexes[orderCount] = menuIndex;
                orderQuantities[orderCount] = jumlah;
                orderCount++;
            }
            System.out.println("Pesanan ditambahkan: " + menus[menuIndex].getName() + " x" + jumlah + "\n");
        }

        if (orderCount == 0) {
            System.out.println("Belum ada pesanan.");
            return;
        }

        cetakStruk(orderIndexes, orderQuantities, orderCount);
    }

    private static void cetakStruk(int[] orderIndexes, int[] orderQuantities, int orderCount) {
        System.out.println("\n===== STRUK PEMESANAN =====");
        System.out.printf("%-3s %-20s %-10s %-10s %-10s\n", "No", "Nama Menu", "Harga", "Jumlah", "Total");

        int subtotal = 0;
        int drinkDiscount = 0;
        int lowestDrinkPrice = Integer.MAX_VALUE;
        boolean hasDrink = false;

        for (int i = 0; i < orderCount; i++) {
            Menu menu = menus[orderIndexes[i]];
            int qty = orderQuantities[i];
            int totalMenu = menu.getPrice() * qty;
            subtotal += totalMenu;
            System.out.printf("%-3d %-20s Rp%,10d %-10d Rp%,10d\n", i + 1, menu.getName(), menu.getPrice(), qty, totalMenu);

            if (menu.getCategory().equalsIgnoreCase("minuman")) {
                hasDrink = true;
                int drinkPrice = menu.getPrice();
                if (drinkPrice < lowestDrinkPrice) {
                    lowestDrinkPrice = drinkPrice;
                }
            }
        }

        if (hasDrink && subtotal > 50000) {
            drinkDiscount = lowestDrinkPrice;
        }

        int discount10 = 0;
        if (subtotal > 100000) {
            discount10 = (int) Math.round((subtotal - drinkDiscount) * 0.10);
        }

        int totalAfterDiscount = subtotal - drinkDiscount - discount10;
        int tax = (int) Math.round(totalAfterDiscount * 0.10);
        int serviceFee = 20000;
        int grandTotal = totalAfterDiscount + tax + serviceFee;

        System.out.println("--------------------------------------------");
        System.out.printf("%-30s Rp%,10d\n", "Subtotal", subtotal);
        if (drinkDiscount > 0) {
            System.out.printf("%-30s Rp%,10d\n", "Diskon Beli 1 Gratis 1 Minuman", drinkDiscount);
        } else {
            System.out.printf("%-30s Rp%,10d\n", "Diskon Beli 1 Gratis 1 Minuman", 0);
        }
        if (discount10 > 0) {
            System.out.printf("%-30s Rp%,10d\n", "Diskon 10% (Total > Rp100.000)", discount10);
        } else {
            System.out.printf("%-30s Rp%,10d\n", "Diskon 10% (Total > Rp100.000)", 0);
        }
        System.out.printf("%-30s Rp%,10d\n", "Total setelah diskon", totalAfterDiscount);
        System.out.printf("%-30s Rp%,10d\n", "Pajak 10%", tax);
        System.out.printf("%-30s Rp%,10d\n", "Biaya pelayanan", serviceFee);
        System.out.printf("%-30s Rp%,10d\n", "Total Bayar", grandTotal);
        System.out.println("============================================\n");
    }

    private static void manajemenMenu() {
        while (true) {
            System.out.println("\n--- Menu Pengelolaan Menu Restoran ---");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.print("Pilih tindakan [1-4]: ");

            int pilihan = readInt(1, 4);
            if (pilihan == 1) {
                tambahMenu();
            } else if (pilihan == 2) {
                ubahHargaMenu();
            } else if (pilihan == 3) {
                hapusMenu();
            } else {
                break;
            }
        }
    }

    private static void tambahMenu() {
        System.out.print("Berapa menu baru yang ingin ditambahkan? ");
        int jumlah = readInt(1, 20);

        for (int i = 0; i < jumlah; i++) {
            System.out.println("\nTambah menu baru ke-" + (i + 1));
            System.out.print("Nama menu: ");
            String nama = readNonEmptyLine();

            String kategori;
            while (true) {
                System.out.print("Kategori (makanan/minuman): ");
                kategori = scanner.nextLine().trim().toLowerCase();
                if (kategori.equals("makanan") || kategori.equals("minuman")) {
                    break;
                }
                System.out.println("Kategori tidak valid. Masukkan 'makanan' atau 'minuman'.");
            }

            System.out.print("Harga menu: ");
            int harga = readInt(1, Integer.MAX_VALUE);
            menus[menuCount++] = new Menu(nama, kategori, harga);
            System.out.println("Menu berhasil ditambahkan: " + nama + " (" + kategori + ") Rp" + harga);
        }
    }

    private static void ubahHargaMenu() {
        if (menuCount == 0) {
            System.out.println("Tidak ada menu untuk diubah.");
            return;
        }
        printMenuList();
        System.out.print("Pilih nomor menu yang akan diubah harganya: ");
        int pilihan = readInt(1, menuCount);

        Menu menu = menus[pilihan - 1];
        System.out.println("Menu yang dipilih: " + menu.getName() + " (" + menu.getCategory() + ") Rp" + menu.getPrice());
        System.out.print("Apakah Anda yakin ingin mengubah harga? (Ya/Tidak): ");
        if (confirmAction()) {
            System.out.print("Masukkan harga baru: ");
            int hargaBaru = readInt(1, Integer.MAX_VALUE);
            menu.setPrice(hargaBaru);
            System.out.println("Harga berhasil diubah menjadi Rp" + hargaBaru + ".");
        } else {
            System.out.println("Perubahan dibatalkan.");
        }
    }

    private static void hapusMenu() {
        if (menuCount == 0) {
            System.out.println("Tidak ada menu untuk dihapus.");
            return;
        }
        printMenuList();
        System.out.print("Pilih nomor menu yang akan dihapus: ");
        int pilihan = readInt(1, menuCount);

        Menu menu = menus[pilihan - 1];
        System.out.println("Menu yang dipilih: " + menu.getName() + " (" + menu.getCategory() + ") Rp" + menu.getPrice());
        System.out.print("Apakah Anda yakin ingin menghapus menu ini? (Ya/Tidak): ");
        if (confirmAction()) {
            for (int i = pilihan - 1; i < menuCount - 1; i++) {
                menus[i] = menus[i + 1];
            }
            menus[--menuCount] = null;
            System.out.println("Menu berhasil dihapus.");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    private static int printMenuByCategory(int[] mapping) {
        System.out.println("--- Makanan ---");
        int nomor = 1;
        for (int i = 0; i < menuCount; i++) {
            if (menus[i].getCategory().equalsIgnoreCase("makanan")) {
                mapping[nomor - 1] = i;
                System.out.printf("%2d. %-20s Rp%,10d\n", nomor++, menus[i].getName(), menus[i].getPrice());
            }
        }
        System.out.println("--- Minuman ---");
        for (int i = 0; i < menuCount; i++) {
            if (menus[i].getCategory().equalsIgnoreCase("minuman")) {
                mapping[nomor - 1] = i;
                System.out.printf("%2d. %-20s Rp%,10d\n", nomor++, menus[i].getName(), menus[i].getPrice());
            }
        }
        System.out.println();
        return nomor - 1;
    }

    private static void printMenuList() {
        System.out.println("\nDaftar Menu Saat Ini:");
        for (int i = 0; i < menuCount; i++) {
            System.out.printf("%2d. %-20s %-10s Rp%,10d\n", i + 1, menus[i].getName(), menus[i].getCategory(), menus[i].getPrice());
        }
    }

    private static int readInt(int min, int max) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.print("Input harus antara " + min + " sampai " + max + ". Silakan ulangi: ");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Input tidak valid. Masukkan angka: ");
            }
        }
    }

    private static String readNonEmptyLine() {
        while (true) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.print("Input tidak boleh kosong. Silakan masukkan kembali: ");
        }
    }

    private static boolean confirmAction() {
        while (true) {
            String jawab = scanner.nextLine().trim();
            if (jawab.equalsIgnoreCase("ya")) {
                return true;
            }
            if (jawab.equalsIgnoreCase("tidak")) {
                return false;
            }
            System.out.print("Masukkan 'Ya' atau 'Tidak': ");
        }
    }
}
