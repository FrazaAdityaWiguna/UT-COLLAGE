import java.util.*;
import java.io.*;

// Abstract class MenuItem sebagai kelas dasar semua item menu
abstract class MenuItem {
    private String nama;
    private double harga;
    private String kategori;

    public MenuItem(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public String getKategori() { return kategori; }

    public void setNama(String nama) { this.nama = nama; }
    public void setHarga(double harga) { this.harga = harga; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public abstract void tampilMenu();

    public abstract String toFileString();
}

// Kelas Makanan - subkelas dari MenuItem
class Makanan extends MenuItem {
    private String jenisMakanan;

    public Makanan(String nama, double harga, String jenisMakanan) {
        super(nama, harga, "Makanan");
        this.jenisMakanan = jenisMakanan;
    }

    public String getJenisMakanan() { return jenisMakanan; }
    public void setJenisMakanan(String jenisMakanan) { this.jenisMakanan = jenisMakanan; }

    @Override
    public void tampilMenu() {
        System.out.printf("[Makanan] %-20s | Jenis: %-15s | Harga: Rp%.2f%n",
                getNama(), jenisMakanan, getHarga());
    }

    @Override
    public String toFileString() {
        return "MAKANAN," + getNama() + "," + getHarga() + "," + jenisMakanan;
    }
}

// Kelas Minuman - subkelas dari MenuItem
class Minuman extends MenuItem {
    private String jenisMinuman;

    public Minuman(String nama, double harga, String jenisMinuman) {
        super(nama, harga, "Minuman");
        this.jenisMinuman = jenisMinuman;
    }

    public String getJenisMinuman() { return jenisMinuman; }
    public void setJenisMinuman(String jenisMinuman) { this.jenisMinuman = jenisMinuman; }

    @Override
    public void tampilMenu() {
        System.out.printf("[Minuman] %-20s | Jenis: %-15s | Harga: Rp%.2f%n",
                getNama(), jenisMinuman, getHarga());
    }

    @Override
    public String toFileString() {
        return "MINUMAN," + getNama() + "," + getHarga() + "," + jenisMinuman;
    }
}

// Kelas Diskon - subkelas dari MenuItem
class Diskon extends MenuItem {
    private double diskon; // dalam persen (0-100)

    public Diskon(String nama, double harga, double diskon) {
        super(nama, harga, "Diskon");
        this.diskon = diskon;
    }

    public double getDiskon() { return diskon; }
    public void setDiskon(double diskon) { this.diskon = diskon; }

    public double getHargaSetelahDiskon() {
        return getHarga() * (1 - diskon / 100);
    }

    @Override
    public void tampilMenu() {
        System.out.printf("[Diskon]  %-20s | Diskon: %5.1f%%       | Harga: Rp%.2f -> Rp%.2f%n",
                getNama(), diskon, getHarga(), getHargaSetelahDiskon());
    }

    @Override
    public String toFileString() {
        return "DISKON," + getNama() + "," + getHarga() + "," + diskon;
    }
}

// Custom Exception untuk item yang tidak ditemukan
class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String message) {
        super(message);
    }
}

// Kelas Menu untuk mengelola semua item menu restoran
class Menu {
    private ArrayList<MenuItem> daftarMenu;
    private static final String FILE_MENU = "menu.txt";

    public Menu() {
        daftarMenu = new ArrayList<>();
        muatDariFile();
    }

    public void tambahItem(MenuItem item) {
        daftarMenu.add(item);
        simpanKeFile();
    }

    public MenuItem cariItemByIndex(int index) throws ItemNotFoundException {
        if (index < 0 || index >= daftarMenu.size()) {
            throw new ItemNotFoundException("Nomor item tidak valid! Pilih antara 1 - " + daftarMenu.size());
        }
        return daftarMenu.get(index);
    }

    public void tampilkanMenu() {
        if (daftarMenu.isEmpty()) {
            System.out.println("  Menu masih kosong.");
            return;
        }
        System.out.println("  =================================================================");
        for (int i = 0; i < daftarMenu.size(); i++) {
            System.out.printf("  [%2d] ", i + 1);
            daftarMenu.get(i).tampilMenu();
        }
        System.out.println("  =================================================================");
    }

    public int getJumlahItem() {
        return daftarMenu.size();
    }

    private void simpanKeFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_MENU))) {
            for (MenuItem item : daftarMenu) {
                pw.println(item.toFileString());
            }
        } catch (IOException e) {
            System.out.println("  Gagal menyimpan menu: " + e.getMessage());
        }
    }

    private void muatDariFile() {
        File file = new File(FILE_MENU);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_MENU))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
                String tipe = parts[0];
                String nama = parts[1];
                double harga = Double.parseDouble(parts[2]);
                String ekstra = parts[3];

                switch (tipe) {
                    case "MAKANAN":
                        daftarMenu.add(new Makanan(nama, harga, ekstra));
                        break;
                    case "MINUMAN":
                        daftarMenu.add(new Minuman(nama, harga, ekstra));
                        break;
                    case "DISKON":
                        daftarMenu.add(new Diskon(nama, harga, Double.parseDouble(ekstra)));
                        break;
                }
            }
            if (!daftarMenu.isEmpty()) {
                System.out.println("  Menu berhasil dimuat dari file (" + daftarMenu.size() + " item).");
            }
        } catch (IOException e) {
            System.out.println("  Gagal memuat menu: " + e.getMessage());
        }
    }
}

// Kelas Pesanan untuk mencatat pesanan pelanggan
class Pesanan {
    private String namaPelanggan;
    private ArrayList<MenuItem> itemPesanan;
    private ArrayList<Integer> jumlahPesanan;

    public Pesanan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
        this.itemPesanan = new ArrayList<>();
        this.jumlahPesanan = new ArrayList<>();
    }

    public void tambahPesanan(MenuItem item, int jumlah) {
        itemPesanan.add(item);
        jumlahPesanan.add(jumlah);
    }

    public double hitungTotal() {
        double total = 0;
        for (int i = 0; i < itemPesanan.size(); i++) {
            MenuItem item = itemPesanan.get(i);
            int jumlah = jumlahPesanan.get(i);
            if (item instanceof Diskon) {
                total += ((Diskon) item).getHargaSetelahDiskon() * jumlah;
            } else {
                total += item.getHarga() * jumlah;
            }
        }
        return total;
    }

    public void tampilkanStruk() {
        System.out.println("\n  ==========================================");
        System.out.println("         STRUK PESANAN RESTORAN");
        System.out.println("  ==========================================");
        System.out.println("  Pelanggan : " + namaPelanggan);
        System.out.println("  ------------------------------------------");
        for (int i = 0; i < itemPesanan.size(); i++) {
            MenuItem item = itemPesanan.get(i);
            int jumlah = jumlahPesanan.get(i);
            double hargaSatuan = (item instanceof Diskon)
                    ? ((Diskon) item).getHargaSetelahDiskon()
                    : item.getHarga();
            System.out.printf("  %-20s x%-3d  Rp%.2f%n",
                    item.getNama(), jumlah, hargaSatuan * jumlah);
        }
        System.out.println("  ------------------------------------------");
        System.out.printf("  TOTAL                        Rp%.2f%n", hitungTotal());
        System.out.println("  ==========================================");
        System.out.println("       Terima kasih telah memesan!");
        System.out.println("  ==========================================\n");
    }

    public void simpanStrukKeFile() {
        String safeNama = namaPelanggan.replaceAll("\\s+", "_");
        String fileName = "struk_" + safeNama + "_" + System.currentTimeMillis() + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.println("==========================================");
            pw.println("       STRUK PESANAN RESTORAN");
            pw.println("==========================================");
            pw.println("Pelanggan : " + namaPelanggan);
            pw.println("------------------------------------------");
            for (int i = 0; i < itemPesanan.size(); i++) {
                MenuItem item = itemPesanan.get(i);
                int jumlah = jumlahPesanan.get(i);
                double hargaSatuan = (item instanceof Diskon)
                        ? ((Diskon) item).getHargaSetelahDiskon()
                        : item.getHarga();
                pw.printf("%-20s x%-3d  Rp%.2f%n", item.getNama(), jumlah, hargaSatuan * jumlah);
            }
            pw.println("------------------------------------------");
            pw.printf("TOTAL                        Rp%.2f%n", hitungTotal());
            pw.println("==========================================");
            pw.println("     Terima kasih telah memesan!");
            pw.println("==========================================");
            System.out.println("  Struk berhasil disimpan ke file: " + fileName);
        } catch (IOException e) {
            System.out.println("  Gagal menyimpan struk: " + e.getMessage());
        }
    }

    public boolean isEmpty() {
        return itemPesanan.isEmpty();
    }
}

// Kelas utama program
public class answer1 {
    private static Scanner scanner = new Scanner(System.in);
    private static Menu menu = new Menu();

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   SELAMAT DATANG DI SISTEM RESTORAN");
        System.out.println("==========================================");

        boolean running = true;
        while (running) {
            tampilkanMenuUtama();
            int pilihan = bacaInteger("Pilih menu: ");

            switch (pilihan) {
                case 1:
                    tambahItemMenu();
                    break;
                case 2:
                    tampilkanMenu();
                    break;
                case 3:
                    buatPesanan();
                    break;
                case 4:
                    System.out.println("\n  Terima kasih! Sampai jumpa.");
                    running = false;
                    break;
                default:
                    System.out.println("  Pilihan tidak valid. Silakan coba lagi.\n");
            }
        }
        scanner.close();
    }

    private static void tampilkanMenuUtama() {
        System.out.println("\n============= MENU UTAMA =============");
        System.out.println("  1. Tambah Item Menu");
        System.out.println("  2. Tampilkan Menu Restoran");
        System.out.println("  3. Buat Pesanan");
        System.out.println("  4. Keluar");
        System.out.println("======================================");
    }

    private static void tambahItemMenu() {
        System.out.println("\n--- Tambah Item Menu ---");
        System.out.println("  1. Makanan");
        System.out.println("  2. Minuman");
        System.out.println("  3. Diskon");
        int tipe = bacaInteger("  Pilih jenis item: ");

        System.out.print("  Nama item       : ");
        String nama = scanner.nextLine();
        double harga = bacaDouble("  Harga (Rp)      : ");

        switch (tipe) {
            case 1:
                System.out.print("  Jenis makanan   : ");
                String jenisMakanan = scanner.nextLine();
                menu.tambahItem(new Makanan(nama, harga, jenisMakanan));
                System.out.println("  Makanan '" + nama + "' berhasil ditambahkan!");
                break;
            case 2:
                System.out.print("  Jenis minuman   : ");
                String jenisMinuman = scanner.nextLine();
                menu.tambahItem(new Minuman(nama, harga, jenisMinuman));
                System.out.println("  Minuman '" + nama + "' berhasil ditambahkan!");
                break;
            case 3:
                double diskon = bacaDouble("  Diskon (%)      : ");
                menu.tambahItem(new Diskon(nama, harga, diskon));
                System.out.println("  Item diskon '" + nama + "' berhasil ditambahkan!");
                break;
            default:
                System.out.println("  Pilihan tidak valid.");
        }
    }

    private static void tampilkanMenu() {
        System.out.println("\n======== DAFTAR MENU RESTORAN ========");
        menu.tampilkanMenu();
    }

    private static void buatPesanan() {
        if (menu.getJumlahItem() == 0) {
            System.out.println("  Menu masih kosong. Tambahkan item terlebih dahulu.");
            return;
        }

        System.out.print("\n  Nama pelanggan  : ");
        String namaPelanggan = scanner.nextLine();
        Pesanan pesanan = new Pesanan(namaPelanggan);

        boolean tambahItem = true;
        while (tambahItem) {
            System.out.println("\n  --- Menu Tersedia ---");
            menu.tampilkanMenu();
            System.out.println("  [ 0] Selesai memesan");

            int nomor = bacaInteger("  Pilih nomor item (0 untuk selesai): ");

            if (nomor == 0) {
                tambahItem = false;
            } else {
                try {
                    MenuItem item = menu.cariItemByIndex(nomor - 1);
                    int jumlah = bacaInteger("  Jumlah pesanan  : ");
                    if (jumlah <= 0) {
                        System.out.println("  Jumlah harus lebih dari 0.");
                    } else {
                        pesanan.tambahPesanan(item, jumlah);
                        System.out.println("  '" + item.getNama() + "' x" + jumlah + " ditambahkan ke pesanan.");
                    }
                } catch (ItemNotFoundException e) {
                    System.out.println("  Error: " + e.getMessage());
                }
            }
        }

        if (pesanan.isEmpty()) {
            System.out.println("  Tidak ada item yang dipesan.");
        } else {
            pesanan.tampilkanStruk();
            System.out.print("  Simpan struk ke file? (y/n): ");
            String jawab = scanner.nextLine().trim();
            if (jawab.equalsIgnoreCase("y")) {
                pesanan.simpanStrukKeFile();
            }
        }
    }

    private static int bacaInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Input tidak valid. Masukkan angka bulat.");
            }
        }
    }

    private static double bacaDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Input tidak valid. Masukkan angka.");
            }
        }
    }
}
