import java.util.LinkedList;
import java.util.Arrays;

public class Answer1 {
    // 1. Deklarasi variabel integer bernama ‘StrukturBaris’
    int StrukturBaris;

    // 2. Deklarasi variabel string bernama ‘KataBaru’ dengan isi ‘Deklarasi tipe data String’
    String KataBaru = "Deklarasi tipe data String";

    // 3. Pendeklarasian array satu dimensi bernama ‘empatAngka’, tipe integer, isi (07, 10, 20, 23)
    int[] empatAngka = {7, 10, 20, 23};

    // 4. Pendeklarasian array dua dimensi bernama ‘Angka’, tipe String, 3x3, isi (1, 3, 5, 14, 19, 20, 22, 27, 29)
    String[][] Angka = {
        {"1", "3", "5"},
        {"14", "19", "20"},
        {"22", "27", "29"}
    };

    // 5. Deklarasi linked list bernama 'listAngka' dengan isi (22, 19, 44, 60, 72) / Gunakan List.of
    LinkedList<Integer> listAngka = new LinkedList<>(Arrays.asList(22, 19, 44, 60, 72));
}
