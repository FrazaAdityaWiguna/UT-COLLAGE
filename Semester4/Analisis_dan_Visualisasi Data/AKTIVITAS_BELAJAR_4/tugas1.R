# ============================================================
# Tugas Tutorial 1 - STSI4204 Analisis dan Visualisasi Data
# ============================================================

# ---------- Data (Tabel Soal No. 1) ----------
kecepatan <- c(4, 4, 7, 7, 8, 8, 9, 10, 10, 11, 11, 12, 12, 12, 13, 13, 13, 13,
               14, 14, 14, 14, 15, 15, 15, 16, 16, 17, 17, 17, 17, 17, 18, 18,
               18, 19, 19, 20, 20, 20, 20, 20, 21, 22, 23, 24, 24, 24, 25, 25)
jarak <- c(2, 10, 4, 20, 17, 13, 18, 28, 33, 18, 26, 12, 24, 22, 28, 24, 32, 34,
           43, 24, 30, 58, 80, 20, 24, 55, 35, 40, 30, 44, 50, 46, 53, 70, 80,
           36, 46, 68, 34, 48, 50, 56, 60, 64, 56, 72, 90, 92, 110, 85)
data_mobil <- data.frame(No = 1:50, Kecepatan = kecepatan, Jarak = jarak)
str(data_mobil)
head(data_mobil)

# ---------- Soal 1 ----------
# a. Rata-rata kecepatan mobil
rata_kecepatan <- mean(data_mobil$Kecepatan)
cat("1a. Rata-rata kecepatan mobil   :", rata_kecepatan, "km/h\n")

# b. Rata-rata jarak yang ditempuh mobil
rata_jarak <- mean(data_mobil$Jarak)
cat("1b. Rata-rata jarak tempuh      :", rata_jarak, "meter\n")

# c. Standar deviasi data jarak
sd_jarak <- sd(data_mobil$Jarak)
cat("1c. Standar deviasi jarak       :", round(sd_jarak, 4), "meter\n")

# ---------- Soal 2 ----------
# a. Scatter plot kecepatan vs jarak
png("hasil/scatter_plot.png", width = 800, height = 600, res = 110)
plot(data_mobil$Kecepatan, data_mobil$Jarak,
     main = "Scatter Plot Kecepatan vs Jarak Berhenti Mobil",
     xlab = "Kecepatan (km/h)", ylab = "Jarak (meter)",
     pch = 19, col = "steelblue")
abline(lm(Jarak ~ Kecepatan, data = data_mobil), col = "red", lwd = 2)
dev.off()

# Pendukung interpretasi scatter plot
korelasi <- cor(data_mobil$Kecepatan, data_mobil$Jarak)
cat("Koefisien korelasi (r)          :", round(korelasi, 4), "\n")
model <- lm(Jarak ~ Kecepatan, data = data_mobil)
print(coef(model))

# c. Histogram kecepatan mobil
png("hasil/histogram_kecepatan.png", width = 800, height = 600, res = 110)
h <- hist(data_mobil$Kecepatan,
          main = "Histogram Kecepatan Mobil",
          xlab = "Kecepatan (km/h)", ylab = "Frekuensi",
          col = "lightblue", border = "black")
dev.off()

# Pendukung interpretasi histogram
print(data.frame(Kelas = paste0("(", head(h$breaks, -1), ", ", h$breaks[-1], "]"),
                 Frekuensi = h$counts))
summary(data_mobil$Kecepatan)
cat("Median kecepatan                :", median(data_mobil$Kecepatan), "\n")

# ---------- Soal 3 ----------
# Koefisien keragaman (KK) = (s / rata-rata) x 100%
kk_matematika <- 10 / 75 * 100
kk_inggris    <- 8 / 70 * 100
cat("KK Matematika                   :", round(kk_matematika, 2), "%\n")
cat("KK Bahasa Inggris               :", round(kk_inggris, 2), "%\n")

# Tambahan: nilai baku (z-score) Thomas
z_matematika <- (80 - 75) / 10
z_inggris    <- (75 - 70) / 8
cat("Z Matematika                    :", z_matematika, "\n")
cat("Z Bahasa Inggris                :", z_inggris, "\n")
