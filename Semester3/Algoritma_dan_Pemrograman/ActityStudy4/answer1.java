import java.util.Scanner;

public class answer1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan golongan karyawan (A/B/C): ");
        String golongan = scanner.nextLine().trim().toUpperCase();

        System.out.print("Masukkan jam lembur (angka): ");
        int jamLembur = 0;
        try {
            jamLembur = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Input jam lembur harus berupa angka.");
            scanner.close();
            return;
        }

        double gajiPokok;
        switch (golongan) {
            case "A":
                gajiPokok = 5000000;
                break;
            case "B":
                gajiPokok = 6500000;
                break;
            case "C":
                gajiPokok = 9500000;
                break;
            default:
                System.out.println("Golongan tidak valid. Gunakan A, B, atau C.");
                scanner.close();
                return;
        }

        double persenLembur;
        if (jamLembur <= 0) {
            persenLembur = 0.0;
        } else if (jamLembur == 1) {
            persenLembur = 0.30;
        } else if (jamLembur == 2) {
            persenLembur = 0.32;
        } else if (jamLembur == 3) {
            persenLembur = 0.34;
        } else if (jamLembur == 4) {
            persenLembur = 0.36;
        } else {
            persenLembur = 0.38;
        }

        double gajiLembur = persenLembur * gajiPokok;
        double totalPenghasilan = gajiPokok + gajiLembur;

        System.out.println();
        System.out.println("===============================");
        System.out.println("Golongan Karyawan : " + golongan);
        System.out.printf("Gaji Pokok        : Rp %.0f%n", gajiPokok);
        System.out.println("Jam Lembur        : " + jamLembur + " jam");
        System.out.printf("Gaji Lembur       : Rp %.0f%n", gajiLembur);
        System.out.println("------------------------------");
        System.out.printf("Total Penghasilan : Rp %.0f%n", totalPenghasilan);
        System.out.println("===============================");

        scanner.close();
    }
}
