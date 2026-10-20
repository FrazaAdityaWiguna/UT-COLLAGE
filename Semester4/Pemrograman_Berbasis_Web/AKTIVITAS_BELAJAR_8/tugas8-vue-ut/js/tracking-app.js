// =====================================================================
// tracking-app.js - Logika Vue.js untuk halaman Tracking DO (tracking.html)
// Data dummy diambil dari file dataBahanAjar.js
// =====================================================================

var app = new Vue({
  el: '#app',

  data: {
    judulHalaman: 'Tracking Delivery Order (DO)',
    upbjjList: ["Jakarta", "Surabaya", "Makassar", "Padang", "Denpasar"],
    pengirimanList: [
      { kode: "REG", nama: "Reguler (3-5 hari)" },
      { kode: "EXP", nama: "Ekspres (1-2 hari)" }
    ],
    paket: [
      { kode: "PAKET-UT-001", nama: "PAKET IPS Dasar", isi: ["EKMA4116", "EKMA4115"], harga: 120000 },
      { kode: "PAKET-UT-002", nama: "PAKET IPA Dasar", isi: ["BIOL4201", "FISIP4001"], harga: 140000 }
    ],
    // dipakai untuk menampilkan judul bahan ajar di detail paket
    stok: [
      { kode: "EKMA4116", judul: "Pengantar Manajemen" },
      { kode: "EKMA4115", judul: "Pengantar Akuntansi" },
      { kode: "BIOL4201", judul: "Biologi Umum (Praktikum)" },
      { kode: "FISIP4001", judul: "Dasar-Dasar Sosiologi" }
    ],
    tracking: {
      "DO2025-0001": {
        nim: "123456789",
        nama: "Rina Wulandari",
        status: "Dalam Perjalanan",
        ekspedisi: "JNE",
        tanggalKirim: "2025-08-25",
        paket: "PAKET-UT-001",
        total: 120000,
        perjalanan: [
          { waktu: "2025-08-25 10:12:20", keterangan: "Penerimaan di Loket: TANGSEL" },
          { waktu: "2025-08-25 14:07:56", keterangan: "Tiba di Hub: JAKSEL" },
          { waktu: "2025-08-26 08:44:01", keterangan: "Diteruskan ke Kantor Tujuan" }
        ]
      }
    },

    // state pencarian
    kataKunci: '',
    kataKunciTerakhir: '',
    sudahCari: false,
    hasilCari: null,

    // state form DO baru
    form: { nim: '', nama: '', ekspedisi: '', paket: '', tanggalKirim: '', total: 0 },
    error: {},
    notif: { pesan: '', tipe: 'sukses' }
  },

  computed: {
    // Nomor DO otomatis: DO + tahun berjalan + nomor urut (3 digit).
    // Nomor urut = nomor terbesar pada tahun yang sama + 1.
    nomorDOBaru: function () {
      var tahun = new Date().getFullYear();
      var awalan = 'DO' + tahun + '-';
      var maks = 0;
      Object.keys(this.tracking).forEach(function (no) {
        if (no.indexOf(awalan) === 0) {
          var urut = parseInt(no.substring(awalan.length), 10);
          if (urut > maks) maks = urut;
        }
      });
      return awalan + String(maks + 1).padStart(3, '0');
    },
    paketTerpilih: function () {
      var kode = this.form.paket;
      return this.paket.find(function (p) { return p.kode === kode; }) || null;
    },
    jumlahDO: function () {
      return Object.keys(this.tracking).length;
    },
    hariIni: function () {
      return this.formatISO(new Date());
    },
    estimasiTiba: function () {
      if (!this.form.ekspedisi || !this.form.tanggalKirim) return '';
      var tambah = this.form.ekspedisi === 'EXP' ? [1, 2] : [3, 5];
      var dasar = new Date(this.form.tanggalKirim + 'T00:00:00');
      var self = this;
      var hasil = tambah.map(function (h) {
        var d = new Date(dasar); d.setDate(d.getDate() + h);
        return self.formatTanggal(self.formatISO(d));
      });
      return hasil[0] + ' – ' + hasil[1];
    }
  },

  watch: {
    // Watcher 1: saat paket dipilih, total harga otomatis diisi dari data paket
    'form.paket': function (kode) {
      this.form.total = this.paketTerpilih ? this.paketTerpilih.harga : 0;
      if (kode) this.$delete(this.error, 'paket');
    },

    // Watcher 2: NIM hanya boleh angka, maksimal 9 digit, divalidasi langsung saat diketik
    'form.nim': function (nilai) {
      var bersih = nilai.replace(/\D/g, '').slice(0, 9);
      if (bersih !== nilai) { this.form.nim = bersih; return; }
      if (bersih.length === 9) this.$delete(this.error, 'nim');
    },

    // Watcher 3: nama otomatis dirapikan (huruf kapital di awal kata)
    'form.nama': function (nilai) {
      var rapi = nilai.replace(/\b\w/g, function (h) { return h.toUpperCase(); });
      if (rapi !== nilai) this.form.nama = rapi;
    },

    // Watcher (deep): validasi ulang otomatis setelah pengguna pernah menekan Simpan
    form: {
      handler: function () { if (Object.keys(this.error).length) this.validasi(); },
      deep: true
    },

    // Watcher 5: memantau kata kunci; bila dikosongkan, hasil pencarian ikut hilang
    kataKunci: function (nilai) {
      if (!nilai) this.resetCari();
    }
  },

  methods: {
    formatISO: function (d) {
      var bln = String(d.getMonth() + 1).padStart(2, '0');
      var tgl = String(d.getDate()).padStart(2, '0');
      return d.getFullYear() + '-' + bln + '-' + tgl;
    },
    formatTanggal: function (iso) {
      if (!iso) return '-';
      return new Date(iso + 'T00:00:00').toLocaleDateString('id-ID', { day: 'numeric', month: 'long', year: 'numeric' });
    },
    formatRupiah: function (angka) {
      return 'Rp ' + Number(angka || 0).toLocaleString('id-ID');
    },
    waktuLengkap: function () {
      var d = new Date();
      var jam = [d.getHours(), d.getMinutes(), d.getSeconds()].map(function (x) { return String(x).padStart(2, '0'); }).join(':');
      return this.formatISO(d) + ' ' + jam;
    },
    namaPaket: function (kode) {
      var p = this.paket.find(function (x) { return x.kode === kode; });
      return p ? p.nama : '-';
    },
    judulBahanAjar: function (kode) {
      var s = this.stok.find(function (x) { return x.kode === kode; });
      return s ? s.judul : '(judul tidak ditemukan)';
    },
    kelasStatus: function (status) {
      if (status === 'Selesai' || status === 'Diterima') return 'selesai';
      if (status === 'Diproses') return 'proses';
      return '';
    },
    tampilkanNotif: function (pesan, tipe) {
      var self = this;
      this.notif = { pesan: pesan, tipe: tipe || 'sukses' };
      clearTimeout(this._timer);
      this._timer = setTimeout(function () { self.notif.pesan = ''; }, 3500);
    },

    // ---------- Pencarian ----------
    cariDO: function () {
      var kunci = this.kataKunci.toUpperCase();
      var data = this.tracking;
      var nomor = Object.keys(data).find(function (no) {
        return no.toUpperCase() === kunci || data[no].nim === kunci;
      });
      this.sudahCari = true;
      this.kataKunciTerakhir = this.kataKunci;
      this.hasilCari = nomor ? { nomor: nomor, data: data[nomor] } : null;
    },
    lihatDO: function (nomor) {
      this.kataKunci = nomor;
      this.cariDO();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    resetCari: function () {
      this.kataKunci = '';
      this.sudahCari = false;
      this.hasilCari = null;
    },

    // ---------- Form DO baru ----------
    validasi: function () {
      var f = this.form;
      var err = {};
      if (!f.nim) err.nim = 'NIM wajib diisi.';
      else if (!/^\d{9}$/.test(f.nim)) err.nim = 'NIM harus 9 digit angka.';

      if (!f.nama.trim()) err.nama = 'Nama wajib diisi.';
      else if (!/^[A-Za-z .']{3,}$/.test(f.nama.trim())) err.nama = 'Nama minimal 3 huruf dan hanya berisi huruf.';

      if (!f.ekspedisi) err.ekspedisi = 'Pilih jenis ekspedisi.';
      if (!f.paket) err.paket = 'Pilih paket bahan ajar.';
      if (!f.tanggalKirim) err.tanggalKirim = 'Tanggal kirim wajib diisi.';
      else if (f.tanggalKirim < this.hariIni) err.tanggalKirim = 'Tanggal kirim tidak boleh sebelum hari ini.';

      this.error = err;
      return Object.keys(err).length === 0;
    },
    simpanDO: function () {
      if (!this.validasi()) {
        this.tampilkanNotif('Data DO belum lengkap / belum valid.', 'gagal');
        return;
      }
      var nomor = this.nomorDOBaru;
      var eksp = this.pengirimanList.find(function (p) { return p.kode === this.form.ekspedisi; }, this);
      var dataBaru = {
        nim: this.form.nim,
        nama: this.form.nama.trim(),
        status: 'Diproses',
        ekspedisi: 'JNE ' + (eksp.kode === 'EXP' ? 'Express' : 'Regular'),
        tanggalKirim: this.form.tanggalKirim,
        paket: this.form.paket,
        total: this.form.total,
        perjalanan: [
          { waktu: this.waktuLengkap(), keterangan: 'DO dibuat, menunggu penjemputan oleh ekspedisi' }
        ]
      };
      // $set agar properti baru pada objek tracking tetap reaktif
      this.$set(this.tracking, nomor, dataBaru);
      this.tampilkanNotif('Delivery Order ' + nomor + ' berhasil disimpan.', 'sukses');
      this.resetForm();
      this.lihatDO(nomor);
    },
    resetForm: function () {
      this.form = { nim: '', nama: '', ekspedisi: '', paket: '', tanggalKirim: this.hariIni, total: 0 };
      this.error = {};
    }
  },

  created: function () {
    // tanggal kirim default = tanggal lokal hari ini (fungsi Date)
    this.form.tanggalKirim = this.hariIni;
  }
});
