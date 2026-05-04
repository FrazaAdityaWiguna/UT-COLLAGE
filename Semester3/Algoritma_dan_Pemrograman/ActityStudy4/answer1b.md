# Rangkuman: Sejarah Java dan Perkembangannya

---

## 1. Latar Belakang Lahirnya Java

Java adalah bahasa pemrograman tingkat tinggi yang berorientasi objek (Object-Oriented Programming). Java lahir dari sebuah proyek penelitian di **Sun Microsystems** pada awal tahun 1990-an. Proyek ini dikenal dengan nama **"Project Green"**, yang dipimpin oleh **James Gosling**, Mike Sheridan, dan Patrick Naughton.

Awalnya, bahasa ini dirancang untuk digunakan pada perangkat elektronik konsumen seperti televisi interaktif. Bahasa aslinya diberi nama **"Oak"** (diambil dari pohon oak yang ada di depan kantor James Gosling). Namun karena nama "Oak" sudah terdaftar oleh perusahaan lain, nama tersebut kemudian diganti menjadi **"Java"** – diinspirasikan dari kopi Java (Jawa), karena para pengembangnya banyak minum kopi saat proses pengembangan.

---

## 2. Sejarah Perkembangan Java

### a. Java 1.0 (1995)
- Java diumumkan secara resmi ke publik pada tanggal **23 Mei 1995** oleh Sun Microsystems.
- Slogan terkenal Java lahir: **"Write Once, Run Anywhere" (WORA)** – kode Java dapat dijalankan di berbagai platform tanpa perlu dikompilasi ulang.
- Java bekerja menggunakan **Java Virtual Machine (JVM)** yang memungkinkan portabilitas lintas platform.
- Browser Netscape Navigator mulai mendukung Java Applet, mempopulerkan Java di dunia web.

### b. Java 1.1 (1997)
- Penambahan fitur Inner Classes, Java Beans, JDBC (Java Database Connectivity), RMI (Remote Method Invocation).
- Perbaikan model event handling pada AWT.

### c. Java 2 / J2SE 1.2 (1998)
- Pengenalan platform Java 2 dengan tiga edisi: **J2SE** (Standard Edition), **J2EE** (Enterprise Edition), dan **J2ME** (Micro Edition).
- Penambahan Swing GUI toolkit, Collections Framework.

### d. J2SE 1.3 – 1.4 (2000–2002)
- Penambahan Java Sound API, Java Naming and Directory Interface (JNDI), Java Web Start.
- J2SE 1.4 memperkenalkan assertions, regular expressions, NIO (New I/O).

### e. Java 5.0 / J2SE 5.0 (2004)
- Salah satu rilis terbesar: penambahan **Generics**, **Annotations**, **Autoboxing**, **Enums**, **Enhanced for-loop**, **Varargs**.

### f. Java SE 6 (2006)
- Sun Microsystems merilis Java SE 6 dengan peningkatan performa dan stabilitas.
- Penambahan scripting support (JSR 223).
- Sun membuka source code Java di bawah lisensi **GPL (GNU General Public License)** – Java menjadi **Open Source**.

### g. Java SE 7 (2011) – Era Oracle
- Pada tahun 2010, **Oracle Corporation mengakuisisi Sun Microsystems**, sehingga Java berada di bawah kendali Oracle.
- Java SE 7 menambahkan: **try-with-resources**, **diamond operator (<>)**, **switch dengan String**, NIO.2.

### h. Java SE 8 (2014)
- Rilis revolusioner: **Lambda Expressions**, **Stream API**, **Optional**, **Default Methods pada Interface**, **Date/Time API** baru (java.time).
- Java 8 masih banyak digunakan hingga saat ini karena stabilitas dan fiturnya.

### i. Java SE 9 – 11 (2017–2018)
- Java SE 9: Pengenalan **Java Platform Module System (JPMS/Project Jigsaw)**, JShell (REPL tool).
- Java SE 10: Pengenalan `var` untuk inferensi tipe lokal.
- Java SE 11 (LTS): Pembaruan HTTP Client API, penghapusan Java EE dan CORBA modules. **Java SE 11 adalah versi LTS (Long-Term Support) pertama setelah 8.**

### j. Java SE 12 – 17 (2019–2021)
- Java SE 14: **Switch Expressions** (resmi stabil), **Records** (preview).
- Java SE 15: **Sealed Classes** (preview), Text Blocks.
- Java SE 16: **Records** resmi stabil, **Pattern Matching** untuk instanceof.
- Java SE 17 (LTS): Sealed Classes resmi, Random Generator API.

### k. Java SE 18 – 21 (2022–2023)
- Java SE 19–20: **Virtual Threads** (Project Loom – preview), Pattern Matching for switch.
- Java SE 21 (LTS): **Virtual Threads** (stabil), **Sequenced Collections**, Pattern Matching for switch (stabil). Ini adalah versi LTS terbaru yang sangat signifikan.

### l. Java SE 22 – 24 (2024–2025)
- Java SE 22–24 melanjutkan pengembangan dengan fitur-fitur seperti **Unnamed Variables & Patterns**, **String Templates** (preview), peningkatan performa Garbage Collector.

---

## 3. Ekosistem dan Platform Java

| Platform | Deskripsi | Kegunaan |
|----------|-----------|----------|
| **Java SE** (Standard Edition) | Platform inti Java | Aplikasi desktop, konsol |
| **Jakarta EE** (dulu Java EE) | Platform enterprise Java | Aplikasi web enterprise, server |
| **Android SDK** | Platform berbasis Java/Kotlin | Pengembangan aplikasi Android |
| **Spring Framework** | Framework populer berbasis Java | Web apps, microservices |

---

## 4. Software Terkenal yang Dibuat dengan Java

### a. IntelliJ IDEA
**IntelliJ IDEA** adalah Integrated Development Environment (IDE) yang dikembangkan oleh **JetBrains**, ditulis menggunakan bahasa Java dan Kotlin. IntelliJ IDEA merupakan salah satu IDE paling populer untuk pengembangan Java, Kotlin, Android, dan banyak bahasa lain. Fitur-fiturnya meliputi smart code completion, built-in tools untuk version control, database management, dan banyak plugin. Versi Community Edition tersedia gratis dan open-source. IntelliJ IDEA digunakan secara luas di kalangan developer profesional dan menjadi basis dari Android Studio yang dikembangkan oleh Google.

### b. Minecraft (Java Edition)
**Minecraft Java Edition** adalah versi asli dari game sandbox terkenal dunia yang dikembangkan oleh **Markus "Notch" Persson** dan kemudian diteruskan oleh **Mojang Studios**. Game ini sepenuhnya ditulis dalam bahasa Java, yang memungkinkannya berjalan di berbagai platform (Windows, macOS, Linux) tanpa modifikasi kode. Minecraft Java Edition terkenal karena ekosistem modding-nya yang sangat aktif – pemain dapat membuat mod menggunakan Java untuk menambahkan konten baru ke dalam game. Hingga saat ini, Minecraft tetap menjadi salah satu game terlaris sepanjang masa.

---

## 5. Keunggulan Java

1. **Platform Independent**: Berkat JVM, program Java dapat berjalan di berbagai sistem operasi.
2. **Object-Oriented**: Mendukung konsep OOP secara penuh (enkapsulasi, inheritance, polimorfisme, abstraksi).
3. **Robust & Secure**: Memiliki penanganan exception yang kuat dan sistem keamanan bawaan.
4. **Multithreading**: Mendukung pemrograman multi-thread secara native.
5. **Open Source**: Sejak 2006, Java (OpenJDK) tersedia sebagai proyek open source.
6. **Ekosistem Besar**: Komunitas yang sangat besar dan banyak library/framework tersedia.

---

## Daftar Pustaka

1. Oracle Corporation. (2024). *Java Platform, Standard Edition Documentation*. Diakses pada 4 Mei 2026, dari https://docs.oracle.com/en/java/

2. GeeksforGeeks. (2023). *History of Java*. Diakses pada 4 Mei 2026, dari https://www.geeksforgeeks.org/the-history-of-java/

3. JetBrains. (2024). *IntelliJ IDEA – The Leading Java and Kotlin IDE*. Diakses pada 4 Mei 2026, dari https://www.jetbrains.com/idea/

4. Mojang Studios. (2024). *Minecraft Java Edition*. Diakses pada 4 Mei 2026, dari https://www.minecraft.net/en-us/store/minecraft-java-bedrock-edition-pc

5. Schildt, H. (2018). *Java: The Complete Reference* (11th ed.). McGraw-Hill Education. Diakses dari https://www.oracle.com/java/technologies/javase/codeconventions-introduction.html

6. Wikipedia. (2024). *Java (programming language)*. Diakses pada 4 Mei 2026, dari https://en.wikipedia.org/wiki/Java_(programming_language)
