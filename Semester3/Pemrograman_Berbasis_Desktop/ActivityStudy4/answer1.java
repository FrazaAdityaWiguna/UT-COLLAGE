import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

/**
 * Kelas Menu merepresentasikan satu item menu restoran.
 * Setiap menu memiliki nama, harga (dalam rupiah), dan kategori (Makanan / Minuman).
 */
class Menu {
	// Atribut menu: nama item, harga satuan, dan kategori (Makanan/Minuman)
	private final String nama;
	private final int harga;
	private final String kategori;

	// Konstruktor untuk membuat objek Menu dengan nilai awal
	public Menu(String nama, int harga, String kategori) {
		this.nama = nama;
		this.harga = harga;
		this.kategori = kategori;
	}

	// Getter untuk mengambil nama menu
	public String getNama() {
		return nama;
	}

	// Getter untuk mengambil harga menu
	public int getHarga() {
		return harga;
	}

	// Getter untuk mengambil kategori menu
	public String getKategori() {
		return kategori;
	}
}

/**
 * Kelas utama yang mengelola seluruh alur program restoran:
 * menampilkan menu, menerima pesanan, menghitung biaya, dan mencetak struk.
 */
class Main {
	// Array berisi daftar menu makanan (minimal 4 item sesuai ketentuan tugas)
	static final Menu[] MENU_MAKANAN = {
		new Menu("Nasi Padang", 15000, "Makanan"),
		new Menu("Ayam Bakar", 20000, "Makanan"),
		new Menu("Mie Goreng", 8000, "Makanan"),
		new Menu("Soto", 20000, "Makanan")
	};

	// Array berisi daftar menu minuman (minimal 4 item sesuai ketentuan tugas)
	static final Menu[] MENU_MINUMAN = {
		new Menu("Es Teh", 8000, "Minuman"),
		new Menu("Jus Jeruk", 12000, "Minuman"),
		new Menu("Kopi Susu", 15000, "Minuman"),
		new Menu("Air Mineral", 5000, "Minuman")
	};

	// Biaya pelayanan tetap sebesar Rp20.000 (sesuai soal)
	static final int BIAYA_PELAYANAN = 20000;
	// Batas maksimal item yang dapat dipesan dalam satu transaksi
	static final int MAKS_PESANAN = 4;

	/**
	 * Memformat angka integer menjadi format mata uang Rupiah Indonesia.
	 * Contoh: 15000 -> Rp15.000
	 */
	static String rupiah(int nilai) {
		NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
		return format.format(nilai);
	}

	/**
	 * Menampilkan seluruh daftar menu restoran ke layar,
	 * dikelompokkan berdasarkan kategori (Makanan dan Minuman).
	 */
	static void tampilkanMenu() {
		System.out.println("==============================");
		System.out.println("      DAFTAR MENU RESTORAN    ");
		System.out.println("==============================");

		System.out.println("\nMakanan:");
		System.out.println("1. " + MENU_MAKANAN[0].getNama() + " - " + rupiah(MENU_MAKANAN[0].getHarga()));
		System.out.println("2. " + MENU_MAKANAN[1].getNama() + " - " + rupiah(MENU_MAKANAN[1].getHarga()));
		System.out.println("3. " + MENU_MAKANAN[2].getNama() + " - " + rupiah(MENU_MAKANAN[2].getHarga()));
		System.out.println("4. " + MENU_MAKANAN[3].getNama() + " - " + rupiah(MENU_MAKANAN[3].getHarga()));

		System.out.println("\nMinuman:");
		System.out.println("1. " + MENU_MINUMAN[0].getNama() + " - " + rupiah(MENU_MINUMAN[0].getHarga()));
		System.out.println("2. " + MENU_MINUMAN[1].getNama() + " - " + rupiah(MENU_MINUMAN[1].getHarga()));
		System.out.println("3. " + MENU_MINUMAN[2].getNama() + " - " + rupiah(MENU_MINUMAN[2].getHarga()));
		System.out.println("4. " + MENU_MINUMAN[3].getNama() + " - " + rupiah(MENU_MINUMAN[3].getHarga()));
		System.out.println();
	}

	/**
	 * Mencari objek Menu berdasarkan nama yang dimasukkan pelanggan.
	 * Menggunakan struktur keputusan (if-else) tanpa perulangan sesuai petunjuk tugas.
	 * Mengembalikan null jika nama menu tidak ditemukan.
	 */
	static Menu cariMenu(String namaMenu) {
		if (MENU_MAKANAN[0].getNama().equalsIgnoreCase(namaMenu)) {
			return MENU_MAKANAN[0];
		} else if (MENU_MAKANAN[1].getNama().equalsIgnoreCase(namaMenu)) {
			return MENU_MAKANAN[1];
		} else if (MENU_MAKANAN[2].getNama().equalsIgnoreCase(namaMenu)) {
			return MENU_MAKANAN[2];
		} else if (MENU_MAKANAN[3].getNama().equalsIgnoreCase(namaMenu)) {
			return MENU_MAKANAN[3];
		} else if (MENU_MINUMAN[0].getNama().equalsIgnoreCase(namaMenu)) {
			return MENU_MINUMAN[0];
		} else if (MENU_MINUMAN[1].getNama().equalsIgnoreCase(namaMenu)) {
			return MENU_MINUMAN[1];
		} else if (MENU_MINUMAN[2].getNama().equalsIgnoreCase(namaMenu)) {
			return MENU_MINUMAN[2];
		} else if (MENU_MINUMAN[3].getNama().equalsIgnoreCase(namaMenu)) {
			return MENU_MINUMAN[3];
		}

		return null;
	}

	/**
	 * Mengisi satu slot pesanan berdasarkan input pelanggan.
	 * Jika pelanggan mengetik "0" atau string kosong, slot tersebut dianggap kosong.
	 * Jika nama menu tidak ditemukan atau jumlah tidak valid, slot diabaikan.
	 *
	 * @param slot  indeks slot pesanan (0-3)
	 */
	static void isiPesananSlot(Scanner scanner, int slot, String[] namaPesanan, int[] jumlahPesanan, Menu[] menuPesanan) {
		System.out.print("Masukkan menu pesanan ke-" + (slot + 1) + " (ketik 0 jika selesai): ");
		String inputNama = scanner.nextLine().trim();

		if (inputNama.equals("0") || inputNama.isEmpty()) {
			namaPesanan[slot] = "";
			jumlahPesanan[slot] = 0;
			menuPesanan[slot] = null;
			return;
		}

		Menu menu = cariMenu(inputNama);
		if (menu == null) {
			System.out.println("Menu tidak ditemukan. Pesanan ke-" + (slot + 1) + " diabaikan.");
			namaPesanan[slot] = "";
			jumlahPesanan[slot] = 0;
			menuPesanan[slot] = null;
			return;
		}

		System.out.print("Jumlah: ");
		String inputJumlah = scanner.nextLine().trim();

		try {
			int jumlah = Integer.parseInt(inputJumlah);
			if (jumlah <= 0) {
				System.out.println("Jumlah harus lebih dari 0. Pesanan diabaikan.");
				namaPesanan[slot] = "";
				jumlahPesanan[slot] = 0;
				menuPesanan[slot] = null;
				return;
			}

			namaPesanan[slot] = menu.getNama();
			jumlahPesanan[slot] = jumlah;
			menuPesanan[slot] = menu;
		} catch (NumberFormatException e) {
			System.out.println("Jumlah tidak valid. Pesanan diabaikan.");
			namaPesanan[slot] = "";
			jumlahPesanan[slot] = 0;
			menuPesanan[slot] = null;
		}
	}

	/**
	 * Menghitung total harga untuk satu item pesanan (harga satuan x jumlah).
	 * Mengembalikan 0 jika menu null atau jumlah tidak valid.
	 */
	static int hitungTotalItem(Menu menu, int jumlah) {
		if (menu == null || jumlah <= 0) {
			return 0;
		}

		return menu.getHarga() * jumlah;
	}

	/**
	 * Mencetak struk pesanan lengkap ke layar, meliputi:
	 * - Detail item yang dipesan
	 * - Subtotal sebelum diskon/promo
	 * - Promo beli 1 gratis 1 (jika subtotal > Rp50.000 dan ada minuman dipesan >= 2)
	 * - Diskon 10% (jika subtotal > Rp100.000)
	 * - Pajak 10% dari dasar pajak
	 * - Biaya pelayanan Rp20.000
	 * - Total akhir yang harus dibayar
	 */
	static void cetakStruk(String[] namaPesanan, int[] jumlahPesanan, Menu[] menuPesanan) {
		// Hitung total harga per item pesanan
		int total1 = hitungTotalItem(menuPesanan[0], jumlahPesanan[0]);
		int total2 = hitungTotalItem(menuPesanan[1], jumlahPesanan[1]);
		int total3 = hitungTotalItem(menuPesanan[2], jumlahPesanan[2]);
		int total4 = hitungTotalItem(menuPesanan[3], jumlahPesanan[3]);

		// Jumlahkan semua item menjadi subtotal
		int subtotal = total1 + total2 + total3 + total4;

		// Variabel untuk menyimpan nilai promo dan nama minuman yang mendapat promo
		int promoMinuman = 0;
		String namaPromoMinuman = "";

		// Cek syarat promo: subtotal harus melebihi Rp50.000
		if (subtotal > 50000) {
			// Cek masing-masing slot: jika kategorinya Minuman dan jumlah >= 2,
			// pilih minuman dengan harga tertinggi untuk mendapatkan promo gratis 1 item
			if (menuPesanan[0] != null && "Minuman".equalsIgnoreCase(menuPesanan[0].getKategori()) && jumlahPesanan[0] >= 2) {
				promoMinuman = menuPesanan[0].getHarga();
				namaPromoMinuman = menuPesanan[0].getNama();
			}

			if (menuPesanan[1] != null && "Minuman".equalsIgnoreCase(menuPesanan[1].getKategori()) && jumlahPesanan[1] >= 2 && menuPesanan[1].getHarga() > promoMinuman) {
				promoMinuman = menuPesanan[1].getHarga();
				namaPromoMinuman = menuPesanan[1].getNama();
			}

			if (menuPesanan[2] != null && "Minuman".equalsIgnoreCase(menuPesanan[2].getKategori()) && jumlahPesanan[2] >= 2 && menuPesanan[2].getHarga() > promoMinuman) {
				promoMinuman = menuPesanan[2].getHarga();
				namaPromoMinuman = menuPesanan[2].getNama();
			}

			if (menuPesanan[3] != null && "Minuman".equalsIgnoreCase(menuPesanan[3].getKategori()) && jumlahPesanan[3] >= 2 && menuPesanan[3].getHarga() > promoMinuman) {
				promoMinuman = menuPesanan[3].getHarga();
				namaPromoMinuman = menuPesanan[3].getNama();
			}
		}

		// Kurangi subtotal dengan nilai promo (harga 1 item minuman gratis)
		int subtotalSetelahPromo = subtotal - promoMinuman;
		int diskon10 = 0;

		// Cek syarat diskon 10%: subtotal asli (sebelum promo) harus melebihi Rp100.000
		if (subtotal > 100000) {
			diskon10 = (int) Math.round(subtotalSetelahPromo * 0.10);
		}

		// Dasar pajak = subtotal setelah promo dan setelah diskon
		int dasarPajak = subtotalSetelahPromo - diskon10;
		// Pajak 10% dihitung dari dasar pajak
		int pajak = (int) Math.round(dasarPajak * 0.10);
		// Total akhir = dasar pajak + pajak + biaya pelayanan
		int totalBayar = dasarPajak + pajak + BIAYA_PELAYANAN;

		System.out.println("\n==============================");
		System.out.println("         STRUK PESANAN        ");
		System.out.println("==============================");

		if (menuPesanan[0] != null) {
			System.out.println(namaPesanan[0] + " | " + jumlahPesanan[0] + " x " + rupiah(menuPesanan[0].getHarga()) + " = " + rupiah(total1));
		}
		if (menuPesanan[1] != null) {
			System.out.println(namaPesanan[1] + " | " + jumlahPesanan[1] + " x " + rupiah(menuPesanan[1].getHarga()) + " = " + rupiah(total2));
		}
		if (menuPesanan[2] != null) {
			System.out.println(namaPesanan[2] + " | " + jumlahPesanan[2] + " x " + rupiah(menuPesanan[2].getHarga()) + " = " + rupiah(total3));
		}
		if (menuPesanan[3] != null) {
			System.out.println(namaPesanan[3] + " | " + jumlahPesanan[3] + " x " + rupiah(menuPesanan[3].getHarga()) + " = " + rupiah(total4));
		}

		System.out.println("------------------------------");
		System.out.println("Subtotal                 : " + rupiah(subtotal));

		if (promoMinuman > 0) {
			System.out.println("Promo Beli 1 Gratis 1    : -" + rupiah(promoMinuman) + " (" + namaPromoMinuman + ")");
			System.out.println("Subtotal setelah promo   : " + rupiah(subtotalSetelahPromo));
		}

		if (diskon10 > 0) {
			System.out.println("Diskon 10%               : -" + rupiah(diskon10));
		}

		System.out.println("Pajak 10%                : " + rupiah(pajak));
		System.out.println("Biaya pelayanan          : " + rupiah(BIAYA_PELAYANAN));
		System.out.println("------------------------------");
		System.out.println("TOTAL BAYAR              : " + rupiah(totalBayar));
		System.out.println("==============================");
	}

	/**
	 * Method utama — titik masuk program.
	 * Urutan eksekusi:
	 * 1. Tampilkan daftar menu
	 * 2. Terima input pesanan (maks 4 slot)
	 * 3. Cetak struk pesanan
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Tampilkan menu ke layar
		tampilkanMenu();

		// Array untuk menyimpan nama, jumlah, dan referensi objek Menu dari tiap pesanan
		String[] namaPesanan = new String[MAKS_PESANAN];
		int[] jumlahPesanan = new int[MAKS_PESANAN];
		Menu[] menuPesanan = new Menu[MAKS_PESANAN];

		// Isi setiap slot pesanan satu per satu (tanpa perulangan, sesuai petunjuk tugas)
		System.out.println("Masukkan pesanan maksimal 4 menu.");
		isiPesananSlot(scanner, 0, namaPesanan, jumlahPesanan, menuPesanan);
		isiPesananSlot(scanner, 1, namaPesanan, jumlahPesanan, menuPesanan);
		isiPesananSlot(scanner, 2, namaPesanan, jumlahPesanan, menuPesanan);
		isiPesananSlot(scanner, 3, namaPesanan, jumlahPesanan, menuPesanan);

		// Cetak struk akhir
		cetakStruk(namaPesanan, jumlahPesanan, menuPesanan);

		scanner.close();
	}
}
