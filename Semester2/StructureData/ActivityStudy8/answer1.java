import java.util.Arrays;

public class answer1 {

  // No1: Merge Sort untuk mengurutkan data dari terbesar ke terkecil
  // Fungsi ini menggunakan algoritma divide and conquer untuk membagi array menjadi subarray kecil
  public static void mergeSort(int[] arr, int left, int right) {
    if (left < right) {
      // Hitung titik tengah array
      int mid = left + (right - left) / 2;

      // Rekursif bagi array menjadi dua bagian
      mergeSort(arr, left, mid);
      mergeSort(arr, mid + 1, right);

      // Gabungkan dua bagian yang sudah diurutkan
      merge(arr, left, mid, right);
    }
  }

  // Fungsi merge untuk menggabungkan dua subarray yang sudah diurutkan
  public static void merge(int[] arr, int left, int mid, int right) {
    // Hitung ukuran subarray kiri dan kanan
    int n1 = mid - left + 1;
    int n2 = right - mid;

    // Buat array sementara untuk subarray kiri dan kanan
    int[] L = new int[n1];
    int[] R = new int[n2];

    // Salin data ke array sementara
    for (int i = 0; i < n1; ++i) L[i] = arr[left + i];
    for (int j = 0; j < n2; ++j) R[j] = arr[mid + 1 + j];

    // Inisialisasi indeks untuk L, R, dan array utama
    int i = 0, j = 0;
    int k = left;

    // Gabungkan array sementara kembali ke array utama
    // Gunakan >= untuk sorting descending (terbesar ke terkecil)
    while (i < n1 && j < n2) {
      if (L[i] >= R[j]) { // Jika elemen kiri lebih besar atau sama, ambil dari kiri
        arr[k] = L[i];
        i++;
      } else { // Jika elemen kanan lebih besar, ambil dari kanan
        arr[k] = R[j];
        j++;
      }
      k++;
    }

    // Salin sisa elemen dari L jika ada
    while (i < n1) {
      arr[k] = L[i];
      i++;
      k++;
    }

    // Salin sisa elemen dari R jika ada
    while (j < n2) {
      arr[k] = R[j];
      j++;
      k++;
    }
  }

  // No2: Counting Sort untuk mengurutkan data dari terbesar ke terkecil
  // Algoritma ini menghitung frekuensi setiap elemen dan menggunakannya untuk sorting
  public static void countingSort(int[] arr) {
    // Cari nilai maksimum dan minimum dalam array
    int max = Arrays.stream(arr).max().getAsInt();
    int min = Arrays.stream(arr).min().getAsInt();
    int range = max - min + 1; // Hitung range nilai

    // Buat array count untuk menghitung frekuensi setiap nilai
    int[] count = new int[range];
    // Buat array output untuk menyimpan hasil sorting
    int[] output = new int[arr.length];

    // Hitung frekuensi setiap elemen
    for (int i = 0; i < arr.length; i++) {
      count[arr[i] - min]++;
    }

    // Untuk descending, mulai dari nilai terbesar (count.length - 1)
    int index = 0; // Index untuk mengisi output dari depan
    for (int i = count.length - 1; i >= 0; i--) { // Loop dari nilai terbesar ke terkecil
      while (count[i] > 0) {
        output[index++] = i + min; // Isi output dengan nilai yang sesuai
        count[i]--; // Kurangi count
      }
    }

    // Salin hasil dari output ke array asli
    for (int i = 0; i < arr.length; i++) {
      arr[i] = output[i];
    }
  }

  public static void main(String[] args) {
    // Data untuk Merge Sort: a1=10, a2=5, a3=20, a4=15, a5=30, a6=25, a7=35, a8=40
    // Minimal 8 elemen seperti yang diminta
    int[] dataMerge = { 10, 5, 20, 15, 30, 25, 35, 40 };
    System.out.println(
      "Data awal untuk Merge Sort: " + Arrays.toString(dataMerge)
    );
    mergeSort(dataMerge, 0, dataMerge.length - 1);
    System.out.println(
      "Hasil Merge Sort (terbesar ke terkecil): " + Arrays.toString(dataMerge)
    );

    // Data untuk Counting Sort: a1=8, a2=3, a3=12, a4=7, a5=18, a6=13, a7=23, a8=28
    // Minimal 8 elemen seperti yang diminta
    int[] dataCount = { 8, 3, 12, 7, 18, 13, 23, 28 };
    System.out.println(
      "\nData awal untuk Counting Sort: " + Arrays.toString(dataCount)
    );
    countingSort(dataCount);
    System.out.println(
      "Hasil Counting Sort (terbesar ke terkecil): " +
      Arrays.toString(dataCount)
    );
  }
}
/*
Analisa Kinerja Algoritma:

No1: Merge Sort
- Time Complexity: O(n log n) dalam kasus terbaik, rata-rata, dan terburuk. Ini karena algoritma membagi array menjadi dua bagian secara rekursif dan menggabungkannya.
- Space Complexity: O(n) karena menggunakan array tambahan untuk menyimpan subarray selama proses merge.
- Kelebihan: Stabil dan efisien untuk data besar. Cocok untuk sorting descending dengan sedikit modifikasi pada kondisi merge.
- Kekurangan: Membutuhkan ruang tambahan, tidak in-place.
- Kesimpulannya, Merge Sort sangat baik untuk dataset besar karena konsistensinya, meskipun space complexity tinggi.

No2: Counting Sort
- Time Complexity: O(n + k), di mana k adalah range nilai (max - min + 1). Efisien jika range kecil.
- Space Complexity: O(n + k), karena array count dan output.
- Kelebihan: Cepat untuk range nilai terbatas, stabil jika diimplementasikan dengan benar.
- Kekurangan: Tidak efisien jika range besar, hanya cocok untuk integer non-negatif atau dengan offset.
- Kesimpulannya, Counting Sort ideal untuk data dengan range kecil, tetapi kurang fleksibel untuk data umum. Untuk descending, perlu modifikasi pada pengisian output.
*/
