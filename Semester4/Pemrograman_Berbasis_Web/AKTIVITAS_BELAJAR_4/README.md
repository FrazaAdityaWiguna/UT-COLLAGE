# GadgetZa — Toko Online Mini

Tugas Praktik 1 Pemrograman Berbasis Web. Website toko gadget sederhana yang dibuat hanya dengan **HTML, CSS, dan JavaScript** (tanpa framework). Soal lengkapnya ada di [Soal.md](Soal.md).

## Cara Menjalankan

1. Buka file `index.html` langsung di browser (klik dua kali), **atau**
2. Jalankan server lokal di folder ini, lalu buka `http://localhost:8000`:
   ```
   python3 -m http.server 8000
   ```

Data disimpan di `localStorage` browser, jadi tetap ada setelah halaman di-refresh. Tombol **Reset Data** di halaman Admin mengembalikan data ke kondisi awal.

## Struktur File

```
AKTIVITAS_BELAJAR_4/
├── index.html        # Katalog produk
├── keranjang.html    # Keranjang + form checkout
├── admin.html        # Kelola produk & pesanan
├── css/
│   ├── style.css       # Variabel warna, layout, header, footer, tema gelap
│   └── components.css  # Tombol, kartu, form, tabel, modal, toast
└── js/
    ├── storage.js      # Baca/tulis localStorage + data awal
    ├── ui.js           # Format Rupiah, toast, modal konfirmasi, tema, badge
    ├── validation.js   # Aturan validasi form yang bisa dipakai ulang
    ├── katalog.js      # Logika halaman katalog
    ├── keranjang.js    # Logika halaman keranjang & checkout
    └── admin.js        # Logika halaman admin
```

Urutan `<script>` di setiap halaman: `storage.js` → `ui.js` → `validation.js` → script halaman. File umum dimuat dulu karena script halaman memakai fungsinya.

## Fitur

**Katalog (`index.html`)**
- Kartu produk dibuat dari data JavaScript (bukan ditulis manual di HTML)
- Pencarian langsung saat mengetik, filter kategori, dan urutan harga/nama
- Modal detail produk
- Tambah ke keranjang dengan cek stok; produk dengan stok habis tombolnya nonaktif

**Keranjang (`keranjang.html`)**
- Tabel belanja: tambah/kurangi jumlah, hapus item (dengan modal konfirmasi)
- Ringkasan subtotal, ongkir (gratis di atas Rp1.000.000), dan total
- Form checkout dengan validasi: nama, email, nomor HP, alamat, dan metode pembayaran
- Setelah pesanan dibuat: stok berkurang, kode invoice dibuat otomatis, dan muncul modal sukses

**Admin (`admin.html`)**
- Kartu statistik: jumlah produk, total pesanan, pendapatan, dan stok menipis
- Tabel produk dengan pencarian, **tambah / edit / hapus** lewat modal form yang divalidasi
- Tabel pesanan dengan status yang bisa diubah (Diproses → Dikirim → Selesai / Dibatalkan)

**Umum**
- Tema gelap/terang (pilihan disimpan)
- Notifikasi toast untuk setiap aksi berhasil atau gagal
- Responsif untuk layar HP

## Pemetaan ke Kriteria Penilaian

| Kriteria | Di mana |
| --- | --- |
| 1.1 HTML semantik & valid | `header`, `nav`, `main`, `section`, `article`, `aside`, `footer`, `form` + `label` + `fieldset`/`legend`, `table` dengan `caption`/`thead`/`tbody`/`th scope`, `dialog`, `lang="id"`, atribut `aria-*` |
| 1.2 CSS | **External**: `css/style.css` dan `css/components.css`. **Internal**: blok `<style>` di `<head>` setiap halaman. **Inline**: atribut `style` di `admin.html` (paragraf keterangan), warna badge status, dan warna stok menipis di `admin.js`. Juga Flexbox, Grid, variabel CSS, animasi, dan media query |
| 1.3 JavaScript DOM | Render kartu & tabel, cari/filter/urutkan, CRUD produk, ubah jumlah keranjang, ubah status pesanan, statistik otomatis |
| 1.4 Validasi & Alert | `validation.js`: pesan error di bawah field, border merah, validasi saat field ditinggalkan, fokus ke field salah pertama, toast error/sukses, modal konfirmasi hapus, cek stok, cek nama produk ganda |
| 1.5 Modularitas | CSS dipisah global/komponen, JS dipisah data/UI/validasi/per halaman |
| 1.6 Kreativitas | Tema toko gadget, dark mode, toast animasi, kode invoice, gratis ongkir, empty state, dashboard statistik |

## Alur Berpikir (bahan video)

1. **Analisis kebutuhan.** Soal meminta form + validasi, manipulasi tabel, dan modal/alert. Toko online cocok karena secara alami punya ketiganya: form checkout, tabel keranjang/produk/pesanan, dan konfirmasi.
2. **Rancang halaman.** Tiga peran → tiga halaman: pembeli melihat produk (katalog), pembeli membayar (keranjang), dan penjual mengelola toko (admin).
3. **Rancang data.** Tiga data utama: `products`, `cart`, `orders`. Semua disimpan di localStorage lewat `storage.js` supaya data bisa dipakai bersama oleh ketiga halaman.
4. **Struktur HTML.** Pakai tag semantik sesuai fungsinya, misalnya kartu produk = `article`, ringkasan checkout = `aside`, dan modal = `dialog`.
5. **Styling.** Mulai dari variabel warna di `:root` (memudahkan dark mode), lalu komponen umum, lalu gaya khusus tiap halaman di CSS internal.
6. **Logika JavaScript.** Polanya sama di setiap halaman: *ambil data → render ke DOM → pasang event listener → ubah data → simpan → render ulang*.
7. **Validasi.** Dibuat sekali di `validation.js` dengan sistem "aturan" (`Rules.required`, `Rules.email`, …) lalu dipakai oleh form checkout dan form produk.
8. **Pengujian.** Coba alur lengkap: tambah ke keranjang → checkout dengan data salah (muncul error) → data benar (sukses) → cek pesanan di admin → ubah status → lihat pendapatan bertambah.
