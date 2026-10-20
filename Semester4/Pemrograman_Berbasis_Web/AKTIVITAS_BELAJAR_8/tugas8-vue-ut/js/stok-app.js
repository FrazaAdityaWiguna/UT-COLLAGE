// =====================================================================
// stok-app.js - Logika Vue.js untuk halaman Stok Bahan Ajar (stok.html)
// Data dummy diambil dari file dataBahanAjar.js
// =====================================================================

var app = new Vue({
  el: '#app',

  // -------------------------------------------------------------------
  // DATA: sumber data reaktif aplikasi
  // -------------------------------------------------------------------
  data: {
    judulHalaman: 'Stok Bahan Ajar',
    upbjjList: ["Jakarta", "Surabaya", "Makassar", "Padang", "Denpasar"],
    kategoriList: ["MK Wajib", "MK Pilihan", "Praktikum", "Problem-Based"],
    stok: [
      { kode: "EKMA4116", judul: "Pengantar Manajemen", kategori: "MK Wajib", upbjj: "Jakarta",
        lokasiRak: "R1-A3", harga: 65000, qty: 28, safety: 20, catatanHTML: "<em>Edisi 2024, cetak ulang</em>" },
      { kode: "EKMA4115", judul: "Pengantar Akuntansi", kategori: "MK Wajib", upbjj: "Jakarta",
        lokasiRak: "R1-A4", harga: 60000, qty: 7, safety: 15, catatanHTML: "<strong>Cover baru</strong>" },
      { kode: "BIOL4201", judul: "Biologi Umum (Praktikum)", kategori: "Praktikum", upbjj: "Surabaya",
        lokasiRak: "R3-B2", harga: 80000, qty: 12, safety: 10, catatanHTML: "Butuh <u>pendingin</u> untuk kit basah" },
      { kode: "FISIP4001", judul: "Dasar-Dasar Sosiologi", kategori: "MK Pilihan", upbjj: "Makassar",
        lokasiRak: "R2-C1", harga: 55000, qty: 2, safety: 8, catatanHTML: "Stok <i>menipis</i>, prioritaskan reorder" }
    ],

    // state filter & sort
    filter: { upbjj: '', kategori: '', reorder: false },
    sort: { kolom: '', arah: 'asc' },

    // state form tambah
    tampilFormBaru: true,
    formBaru: { kode: '', judul: '', kategori: '', upbjj: '', lokasiRak: '', harga: null, qty: null, safety: null, catatanHTML: '' },
    errorBaru: {},

    // state form edit (null = modal tertutup)
    formEdit: null,
    errorEdit: {},

    notif: { pesan: '', tipe: 'sukses' },
    terakhirUpdate: '-'
  },

  // -------------------------------------------------------------------
  // COMPUTED: hasil olahan data yang di-cache oleh Vue.
  // Hanya dihitung ulang bila data yang menjadi dependensinya berubah,
  // sehingga filter tidak perlu recompute setiap kali halaman dirender.
  // -------------------------------------------------------------------
  computed: {
    // Tahap 1: filter berdasarkan UT-Daerah, kategori, dan kebutuhan re-order
    stokTersaring: function () {
      var f = this.filter;
      var self = this;
      return this.stok.filter(function (item) {
        if (f.upbjj && item.upbjj !== f.upbjj) return false;
        if (f.kategori && item.kategori !== f.kategori) return false;
        if (f.reorder && !self.perluReorder(item)) return false;
        return true;
      });
    },

    // Tahap 2: urutkan hasil filter (tidak mengubah array asli)
    stokTampil: function () {
      var data = this.stokTersaring.slice();
      var kolom = this.sort.kolom;
      if (!kolom) return data;
      var faktor = this.sort.arah === 'asc' ? 1 : -1;
      data.sort(function (a, b) {
        if (kolom === 'judul') return a.judul.localeCompare(b.judul, 'id') * faktor;
        return (a[kolom] - b[kolom]) * faktor;
      });
      return data;
    },

    // Jumlah data untuk setiap status (ringkasan)
    jumlahStatus: function () {
      var hasil = { aman: 0, menipis: 0, kosong: 0 };
      var self = this;
      this.stok.forEach(function (item) { hasil[self.statusStok(item)]++; });
      return hasil;
    },

    adaFilterAktif: function () {
      return !!(this.filter.upbjj || this.filter.kategori || this.filter.reorder || this.sort.kolom);
    }
  },

  // -------------------------------------------------------------------
  // WATCH: memantau perubahan data tertentu
  // -------------------------------------------------------------------
  watch: {
    // Watcher 1: jika UT-Daerah diganti / dikosongkan, filter kategori di-reset
    // (dependent option: kategori hanya berlaku di dalam UT-Daerah terpilih)
    'filter.upbjj': function (baru, lama) {
      this.filter.kategori = '';
      if (baru) this.tampilkanNotif('Menampilkan stok UT-Daerah ' + baru + '. Silakan pilih kategori bila perlu.', 'sukses');
    },

    // Watcher 2: memberi peringatan saat mode re-order aktif
    'filter.reorder': function (aktif) {
      if (aktif) {
        var n = this.stok.filter(this.perluReorder).length;
        this.tampilkanNotif(n + ' bahan ajar perlu segera di-re-order.', 'gagal');
      }
    },

    // Watcher 3: kode mata kuliah otomatis huruf besar & cek duplikat secara langsung
    'formBaru.kode': function (nilai) {
      var besar = nilai.toUpperCase();
      if (besar !== nilai) { this.formBaru.kode = besar; return; }
      if (this.cariKode(besar)) {
        this.$set(this.errorBaru, 'kode', 'Kode ' + besar + ' sudah terdaftar.');
      } else if (this.errorBaru.kode && this.errorBaru.kode.indexOf('sudah terdaftar') > -1) {
        this.$delete(this.errorBaru, 'kode');
      }
    },

    // Watcher (deep): setelah pernah gagal validasi, form divalidasi ulang setiap kali
    // isian berubah sehingga pesan error langsung hilang begitu data diperbaiki
    formBaru: {
      handler: function () { if (Object.keys(this.errorBaru).length) this.validasiBaru(); },
      deep: true
    },
    formEdit: {
      handler: function (f) { if (f && Object.keys(this.errorEdit).length) this.errorEdit = this.validasiAngka(f, {}); },
      deep: true
    },

    // Watcher 5 (deep): setiap ada perubahan pada array stok, catat waktu update
    stok: {
      handler: function () { this.terakhirUpdate = this.waktuSekarang(); },
      deep: true
    }
  },

  // -------------------------------------------------------------------
  // METHODS: fungsi yang dipanggil dari template / event
  // -------------------------------------------------------------------
  methods: {
    statusStok: function (item) {
      if (item.qty === 0) return 'kosong';
      if (item.qty < item.safety) return 'menipis';
      return 'aman';
    },
    perluReorder: function (item) {
      return item.qty === 0 || item.qty < item.safety;
    },
    hitungKategori: function (kategori) {
      var u = this.filter.upbjj;
      return this.stok.filter(function (s) { return s.upbjj === u && s.kategori === kategori; }).length;
    },
    cariKode: function (kode) {
      return this.stok.find(function (s) { return s.kode === kode; });
    },
    formatRupiah: function (angka) {
      return 'Rp ' + Number(angka).toLocaleString('id-ID');
    },
    waktuSekarang: function () {
      return new Date().toLocaleString('id-ID', { dateStyle: 'medium', timeStyle: 'short' });
    },
    tampilkanNotif: function (pesan, tipe) {
      var self = this;
      this.notif = { pesan: pesan, tipe: tipe || 'sukses' };
      clearTimeout(this._timerNotif);
      this._timerNotif = setTimeout(function () { self.notif.pesan = ''; }, 3500);
    },
    resetFilter: function () {
      this.filter = { upbjj: '', kategori: '', reorder: false };
      this.sort = { kolom: '', arah: 'asc' };
    },

    // ---------- Validasi bersama untuk form tambah & edit ----------
    validasiAngka: function (data, err) {
      if (data.harga === null || data.harga === '' || isNaN(data.harga)) err.harga = 'Harga wajib diisi.';
      else if (data.harga <= 0) err.harga = 'Harga harus lebih dari 0.';

      if (data.qty === null || data.qty === '' || isNaN(data.qty)) err.qty = 'Jumlah stok wajib diisi.';
      else if (data.qty < 0 || !Number.isInteger(data.qty)) err.qty = 'Stok harus bilangan bulat ≥ 0.';

      if (data.safety === null || data.safety === '' || isNaN(data.safety)) err.safety = 'Safety stock wajib diisi.';
      else if (data.safety < 1 || !Number.isInteger(data.safety)) err.safety = 'Safety stock harus bilangan bulat ≥ 1.';

      if (!data.lokasiRak) err.lokasiRak = 'Lokasi rak wajib diisi.';
      else if (!/^R\d+-[A-Z]\d+$/i.test(data.lokasiRak)) err.lokasiRak = 'Format lokasi rak tidak sesuai (contoh R1-A3).';
      return err;
    },

    // ---------- Tambah data ----------
    validasiBaru: function () {
      var d = this.formBaru;
      var err = {};
      if (!d.kode) err.kode = 'Kode mata kuliah wajib diisi.';
      else if (!/^[A-Z]{4,5}\d{4}$/.test(d.kode)) err.kode = 'Format kode: 4-5 huruf diikuti 4 angka.';
      else if (this.cariKode(d.kode)) err.kode = 'Kode ' + d.kode + ' sudah terdaftar.';

      if (!d.judul) err.judul = 'Judul wajib diisi.';
      else if (d.judul.length < 3) err.judul = 'Judul minimal 3 karakter.';

      if (!d.kategori) err.kategori = 'Pilih kategori.';
      if (!d.upbjj) err.upbjj = 'Pilih UT-Daerah.';
      this.validasiAngka(d, err);
      this.errorBaru = err;
      return Object.keys(err).length === 0;
    },
    simpanBaru: function () {
      if (!this.validasiBaru()) {
        this.tampilkanNotif('Data belum valid, periksa kembali isian yang berwarna merah.', 'gagal');
        return;
      }
      var baru = Object.assign({}, this.formBaru);
      baru.lokasiRak = baru.lokasiRak.toUpperCase();
      if (!baru.catatanHTML) baru.catatanHTML = '<em>Data baru</em>';
      this.stok.push(baru);
      this.tampilkanNotif('Bahan ajar ' + baru.kode + ' berhasil ditambahkan.', 'sukses');
      this.kosongkanFormBaru();
    },
    kosongkanFormBaru: function () {
      this.formBaru = { kode: '', judul: '', kategori: '', upbjj: '', lokasiRak: '', harga: null, qty: null, safety: null, catatanHTML: '' };
      this.errorBaru = {};
    },

    // ---------- Edit data ----------
    bukaEdit: function (item) {
      // salin data agar perubahan baru diterapkan saat tombol simpan ditekan
      this.formEdit = Object.assign({}, item);
      this.errorEdit = {};
    },
    tutupEdit: function () {
      this.formEdit = null;
    },
    simpanEdit: function () {
      var err = this.validasiAngka(this.formEdit, {});
      this.errorEdit = err;
      if (Object.keys(err).length) return;

      var kode = this.formEdit.kode;
      var idx = this.stok.findIndex(function (s) { return s.kode === kode; });
      this.formEdit.lokasiRak = this.formEdit.lokasiRak.toUpperCase();
      this.$set(this.stok, idx, Object.assign({}, this.formEdit));
      this.tampilkanNotif('Stok ' + kode + ' berhasil diperbarui.', 'sukses');
      this.formEdit = null;
    }
  },

  // Lifecycle hook: isi waktu update pertama kali
  created: function () {
    this.terakhirUpdate = this.waktuSekarang();
  }
});
