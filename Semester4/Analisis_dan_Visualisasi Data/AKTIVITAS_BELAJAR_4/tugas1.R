# ============================================================
# Tugas Tutorial 1 - STSI4204 Analisis dan Visualisasi Data
# ============================================================
# Cara menjalankan di RStudio:
#   1. Session -> Set Working Directory -> To Source File Location
#      (agar R bisa menemukan folder "hasil/")
#   2. Jalankan baris per baris dengan Cmd+Enter (Mac) / Ctrl+Enter (Windows),
#      atau seluruh script sekaligus dengan Cmd+Shift+S.
# Tanda "#" adalah komentar: tidak dijalankan oleh R, hanya catatan.

# ---------- Data (Tabel Soal No. 1) ----------
# c(...) = "combine": menggabungkan angka menjadi satu VEKTOR (deretan data).
# "<-"   = menyimpan nilai ke dalam variabel (dibaca: "diisi dengan").
# Urutan data harus sama dengan tabel soal: mobil ke-1 kecepatan 4 & jarak 2,
# mobil ke-2 kecepatan 4 & jarak 10, dst. (total 50 mobil).
kecepatan <- c(4, 4, 7, 7, 8, 8, 9, 10, 10, 11, 11, 12, 12, 12, 13, 13, 13, 13,
               14, 14, 14, 14, 15, 15, 15, 16, 16, 17, 17, 17, 17, 17, 18, 18,
               18, 19, 19, 20, 20, 20, 20, 20, 21, 22, 23, 24, 24, 24, 25, 25)
jarak <- c(2, 10, 4, 20, 17, 13, 18, 28, 33, 18, 26, 12, 24, 22, 28, 24, 32, 34,
           43, 24, 30, 58, 80, 20, 24, 55, 35, 40, 30, 44, 50, 46, 53, 70, 80,
           36, 46, 68, 34, 48, 50, 56, 60, 64, 56, 72, 90, 92, 110, 85)

# data.frame() menyusun vektor-vektor menjadi sebuah TABEL (mirip sheet Excel).
# Nama di kiri "=" menjadi nama kolom, di kanan "=" adalah isinya.
# 1:50 artinya bilangan 1, 2, 3, ..., 50 (untuk kolom nomor).
data_mobil <- data.frame(No = 1:50, Kecepatan = kecepatan, Jarak = jarak)

# Mengecek data sudah benar:
# str()  -> struktur tabel (harus: 50 obs. of 3 variables)
# head() -> menampilkan 6 baris pertama
str(data_mobil)
head(data_mobil)

# ---------- Soal 1 ----------
# Tanda "$" = mengambil satu kolom dari tabel.
# data_mobil$Kecepatan artinya "kolom Kecepatan milik tabel data_mobil".

# a. Rata-rata kecepatan mobil
# mean() = rata-rata = jumlah semua data / banyak data = 775 / 50 = 15.5
rata_kecepatan <- mean(data_mobil$Kecepatan)
# cat() menampilkan teks + nilai ke layar; "\n" = pindah baris (enter).
cat("1a. Rata-rata kecepatan mobil   :", rata_kecepatan, "km/h\n")

# b. Rata-rata jarak yang ditempuh mobil
# 2114 / 50 = 42.28
rata_jarak <- mean(data_mobil$Jarak)
cat("1b. Rata-rata jarak tempuh      :", rata_jarak, "meter\n")

# c. Standar deviasi data jarak
# sd() = standar deviasi SAMPEL: s = akar( jumlah (y - rata2)^2 / (n - 1) )
#      = akar(30052.08 / 49) = 24.77
# Artinya: jarak berhenti rata-rata menyimpang +/- 24.77 m dari 42.28 m.
sd_jarak <- sd(data_mobil$Jarak)
# round(x, 4) = membulatkan menjadi 4 angka di belakang koma.
cat("1c. Standar deviasi jarak       :", round(sd_jarak, 4), "meter\n")

# ---------- Soal 2 ----------
# a. Scatter plot kecepatan vs jarak
# png() = grafik berikutnya DISIMPAN ke file (bukan ditampilkan di layar).
# Ukuran 800x600 piksel, res = resolusi. Hapus/lewati baris png() dan
# dev.off() jika ingin grafik tampil di panel Plots RStudio.
png("hasil/scatter_plot.png", width = 800, height = 600, res = 110)

# plot(X, Y) menggambar satu titik untuk setiap mobil:
# posisi mendatar = kecepatan, posisi tegak = jarak.
plot(data_mobil$Kecepatan, data_mobil$Jarak,
     main = "Scatter Plot Kecepatan vs Jarak Berhenti Mobil",  # judul
     xlab = "Kecepatan (km/h)", ylab = "Jarak (meter)",        # label sumbu
     pch = 19, col = "steelblue")                              # titik bulat penuh, biru

# lm(Jarak ~ Kecepatan) = membuat model garis lurus (regresi linear) yang
# memprediksi Jarak dari Kecepatan ("~" dibaca "dijelaskan oleh").
# abline() menggambar garis itu di atas scatter plot (merah, tebal 2).
abline(lm(Jarak ~ Kecepatan, data = data_mobil), col = "red", lwd = 2)

# dev.off() = menutup file gambar sehingga benar-benar tersimpan.
# (R akan mencetak "null device 1" sebagai tanda berhasil.)
dev.off()

# Pendukung interpretasi scatter plot
# cor() = koefisien korelasi r, nilainya -1 sampai 1.
#   mendekati +1 -> hubungan positif kuat (X naik, Y ikut naik)
#   mendekati  0 -> tidak ada hubungan linear
#   mendekati -1 -> hubungan negatif kuat
# Hasil: 0.8384 -> hubungan positif yang KUAT.
korelasi <- cor(data_mobil$Kecepatan, data_mobil$Jarak)
cat("Koefisien korelasi (r)          :", round(korelasi, 4), "\n")

# coef() menampilkan angka pembentuk persamaan garis regresi:
#   (Intercept) = -17.53, Kecepatan = 3.86
#   -> Jarak = -17.53 + 3.86 x Kecepatan
#   -> setiap kecepatan naik 1 km/h, jarak berhenti bertambah +/- 3.86 meter.
model <- lm(Jarak ~ Kecepatan, data = data_mobil)
print(coef(model))

# c. Histogram kecepatan mobil
png("hasil/histogram_kecepatan.png", width = 800, height = 600, res = 110)

# hist() membagi kecepatan ke dalam beberapa KELAS (interval) dan menghitung
# berapa mobil di tiap kelas (= tinggi batang). R memilih kelasnya sendiri:
# 0-5, 5-10, 10-15, 15-20, 20-25.
# Hasilnya disimpan ke "h" karena isinya (batas kelas & frekuensi) dipakai lagi.
h <- hist(data_mobil$Kecepatan,
          main = "Histogram Kecepatan Mobil",
          xlab = "Kecepatan (km/h)", ylab = "Frekuensi",
          col = "lightblue", border = "black")   # warna batang & garis tepi
dev.off()

# Pendukung interpretasi histogram: membuat tabel frekuensi per kelas.
#   h$breaks            = batas kelas            -> 0 5 10 15 20 25
#   head(h$breaks, -1)  = semua kecuali terakhir -> batas bawah 0 5 10 15 20
#   h$breaks[-1]        = semua kecuali pertama  -> batas atas  5 10 15 20 25
#   paste0()            = menggabungkan teks     -> "(0, 5]", "(5, 10]", ...
#   h$counts            = jumlah mobil per kelas -> 2 7 16 17 8
# "(10, 15]" artinya: lebih dari 10 sampai dengan 15.
print(data.frame(Kelas = paste0("(", head(h$breaks, -1), ", ", h$breaks[-1], "]"),
                 Frekuensi = h$counts))

# summary() = 6 ringkasan sekaligus:
#   Min/Max  = kecepatan terendah/tertinggi
#   1st Qu.  = Q1, 25% mobil kecepatannya <= nilai ini
#   Median   = nilai tengah
#   Mean     = rata-rata
#   3rd Qu.  = Q3, 75% mobil kecepatannya <= nilai ini
# Mean = Median (15.5) -> histogram cenderung simetris.
summary(data_mobil$Kecepatan)
cat("Median kecepatan                :", median(data_mobil$Kecepatan), "\n")

# ---------- Soal 3 ----------
# Di sini R dipakai seperti kalkulator karena data sudah diketahui di soal.
# Koefisien keragaman (KK) = (s / rata-rata) x 100%
# KK mengukur keragaman RELATIF terhadap rata-rata, sehingga dua kelompok
# dengan rata-rata berbeda bisa dibandingkan.
kk_matematika <- 10 / 75 * 100   # s = 10, rata-rata = 75 -> 13.33 %
kk_inggris    <- 8 / 70 * 100    # s = 8,  rata-rata = 70 -> 11.43 %
cat("KK Matematika                   :", round(kk_matematika, 2), "%\n")
cat("KK Bahasa Inggris               :", round(kk_inggris, 2), "%\n")
# Kesimpulan: KK Matematika lebih besar -> nilai Matematika di kelas lebih
# beragam; nilai Bahasa Inggris lebih seragam (homogen).

# Tambahan: nilai baku (z-score) Thomas
# z = (nilai - rata-rata) / s = seberapa jauh nilai Thomas di atas rata-rata
# kelas, diukur dalam satuan standar deviasi.
z_matematika <- (80 - 75) / 10   # 0.5
z_inggris    <- (75 - 70) / 8    # 0.625
cat("Z Matematika                    :", z_matematika, "\n")
cat("Z Bahasa Inggris                :", z_inggris, "\n")
# z Bahasa Inggris lebih besar -> secara relatif Thomas lebih unggul di
# Bahasa Inggris, walaupun nilai mentahnya (75) lebih kecil dari Matematika (80).
