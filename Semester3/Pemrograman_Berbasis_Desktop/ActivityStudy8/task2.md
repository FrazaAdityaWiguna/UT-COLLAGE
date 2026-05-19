Anda diminta untuk membuat sebuah program Java mengenai aplikasi sederhana di sebuah restoran makanan. Aplikasi tersebut memiliki beberapa fungsi:

1. Input Menu Restoran: Program harus memiliki data menu makanan dan minuman beserta harganya. Data ini dapat disimpan dalam sebuah array. Setiap item menu harus memiliki nama, harga, kategori (makanan, minuman)
2. Pemesanan: Program harus memungkinkan pelanggan untuk memesan makanan dan minuman berdasarkan daftar menu yang ditampilkan sebelumnya. Pelanggan dapat memasukkan menu-menu yang ingin mereka pesan tidak terbatas, hingga pelanggan memasukkan input ‘selesai’, maka pelanggan telah menyelesaikan pemesanannya. Catatan: jika menginput nilai/teks di luar dari pilihan menu-menu restoran yang ada, sistem akan terus meminta input kembali.
3. Menghitung Total Biaya: Program harus dapat menghitung total biaya pesanan berdasarkan item-menu yang dipilih dan jumlahnya. Total Biaya keseluruhan pesanan ditambahkan dengan biaya pajak 10% dari total biaya keseluruhan dan biaya pelayanan sebesar Rp. 20.000,-. Selain itu, restoran ini juga menerapkan diskon atau penawaran khusus:

a. Diskon 10% jika total biaya keseluruhan pesanan melebihi Rp 100.000,-
b. Penawaran beli satu gratis satu untuk salah satu kategori minuman jika total biaya keseluruhan pesanan melebihi Rp 50.000

4. Mencetak Struk Pesanan: Program harus dapat mencetak struk pesanan yang mencantumkan item-menu yang dipesan, jumlahnya, harga per item, total harga per item, total biaya pemesanan, informasi pajak dan biaya pajak, dan biaya pelayanan. jika ada diskon dan harga setelah diskon / penawaran , tampilkan informasinya ke dalam struk pembayaran.
5. Manajemen Menu Aplikasi: Program dapat memungkinkan terdapat dua menu utama, yaitu menu untuk pelanggan memesan dan menu untuk pengelolaan menu untuk pemilik restoran agar dapat menambahkan menu baru, mengubah harga, dan menghapus menu restoran. Navigasi menu dapat kembali ke menu parent sebelumnya. Pada saat mengubah dan menghapus data, akan memunculkan daftar menu dan kemudian menginput nomor menu tersebut. Sebelum dieksekusi, terdapat konfirmasi terlebih dahulu ke layar monitor. Jika sudah yakin untuk mengubah/menghapus, pemilik restoran dapat meng-input ‘Ya’. Kemudian akan kembali ke menu pengelolaan. Jika memilih ‘Ya’, terdapat perubahan pada daftar menu. Jika tidak, maka tidak akan ada perubahan. Catatan: jika menginput nilai/teks di luar dari pilihan menu yang ada, sistem akan terus meminta input kembali.

Petunjuk Pengerjaan dan Poin untuk Video Penjelasan Tugas Praktik 2:
1. Buatlah sebuah kelas ‘Menu’ untuk merepresentasikan menu makanan dan minuman di restoran dengan atributnya. Silakan mahasiswa menentukan sendiri input menu makanan dan minumannya. Minimal terdapat 4 menu dari masing-masing kategori menu.
2. Buatlah sebuah kelas utama (‘Main’) yang memiliki berbagai method untuk menampilkan daftar menu makanan dan minuman, menerima dan mengolah pesanan, menghitung total biaya pesanan, mencetak struk pesanan untuk tampilkan ke layar, manajemen menu aplikasi, menambah menu restoran baru, mengubah harga menu restoran, dan menghapus pesanan.
3. Format untuk menampilkan data menu adalah dikelompokkan berdasarkan kategori
4. Pergunakan Array dalam mengelola menu restoran dan pesanan pelanggan
5. Pergunakan struktur keputusan dalam mengimplementasikan fungsi perhitungan total biaya, dan mencetak struk pesanan
6. Pergunakan struktur pengulangan untuk menampilkan daftar menu, menginput pesanan pelanggan, menghitung total biaya, dan menambahkan beberapa menu baru sekaligus.
7. Menampilkan skenario-skenario struktur keputusan yang ada pada soal
8. Durasi video penjelasan maksimal 15 menit