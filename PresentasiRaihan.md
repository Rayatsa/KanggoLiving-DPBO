# Teks Presentasi Project KanggoLiving - Bagian Raihan

Dokumen ini berisi draf teks presentasi dan panduan slide untuk menjelaskan bagian class **User**, **Client**, **Technician**, **Schedule**, dan bagian **Interface** pada proyek sistem ERP mikro **KanggoLiving**.

---

## Slide 1: Pembuka & Pengenalan Bagian Presentasi

**Visual Slide:**
* Judul: "Struktur Kelas Aktor, Penjadwalan & Kontrak Interface"
* Sub-judul: "Implementasi OOP pada KanggoLiving"
* Nama Presenter: Raihan Yassar Tsaqib
* Poin Bahasan:
  1. Abstract Class `User`
  2. Subclass `Client` & `Technician` (Inheritance)
  3. Class `Schedule` (Scheduling Logic)
  4. Kontrak Interface (`Approvable`, `Diagnosable`, `Payable`)

**Teks Presentasi:**
> "Selamat pagi/siang rekan-rekan dan Dosen pengampu. Nama saya **Raihan**, dan pada bagian ini saya akan menjelaskan arsitektur OOP yang kami terapkan untuk mengelola aktor (pengguna), sistem penjadwalan, serta kontrak interface (abstraksi) di dalam sistem KanggoLiving.
> 
> Fokus bahasan saya akan mencakup kelas abstrak `User`, implementasinya pada kelas turunan `Client` dan `Technician`, pengelolaan jadwal konsultasi/teknis lewat kelas `Schedule`, serta bagaimana kami menerapkan prinsip Polymorphism melalui penggunaan `Interface`."

---

## Slide 2: Abstract Class - `User`

**Visual Slide:**
* Judul: "Abstract Class: User"
* Kode/UML Ringkas:
  * Atribut: `userId` (int), `name`, `email`, `password`, `phone`, `role` (String)
  * Metode: `login()`, `logout()`, `updateProfile()`, `changePassword()`
* Konsep OOP: **Abstraction & Encapsulation**
* Catatan: Dibuat `abstract` agar tidak bisa di-instansiasi langsung, melainkan harus diturunkan.

**Teks Presentasi:**
> "Pertama, mari kita bahas kelas **`User`**. Kelas ini dirancang sebagai **Abstract Class** yang bertindak sebagai superclass (parent) bagi seluruh aktor di dalam sistem KanggoLiving.
> 
> Di dalam kelas ini, kami menerapkan konsep **Encapsulation** dengan menyembunyikan data sensitif menggunakan modifier `private` (seperti `email`, `password`, `phone`), yang hanya dapat diakses melalui method getter dan setter.
> 
> Kelas ini juga mengemas fungsi dasar autentikasi dan manajemen profil yang pasti dimiliki oleh semua tipe user, yaitu method `login()`, `logout()`, `updateProfile()`, serta `changePassword()`. Karena sifatnya abstrak, kita tidak bisa membuat objek `User` secara langsung. Kita harus menurunkannya menjadi peran yang lebih spesifik."

---

## Slide 3: Subclass - `Client`

**Visual Slide:**
* Judul: "Subclass: Client (Inheritance & Workflow)"
* Kode/UML Ringkas:
  * Pewarisan: `extends User`
  * Atribut Tambahan: `address` (String)
  * Metode Utama:
    * `submitProblem(description)` $\rightarrow$ return `TicketProblem`
    * `choseSchedule(schedule)`
    * `approveConsultation(consultation)`
    * `makePayment(invoice, amount, method)` $\rightarrow$ return `Payment`
    * `viewInvoice(invoice)`

**Teks Presentasi:**
> "Selanjutnya adalah kelas **`Client`**, yang merupakan turunan langsung dari kelas `User` dengan menggunakan kata kunci `extends`. Di sini kami menerapkan prinsip **Inheritance** (Pewarisan). Kelas `Client` secara otomatis mewarisi semua atribut dan method dari `User`, namun ditambahkan atribut spesifik yaitu `address` untuk mencatat alamat hunian klien.
> 
> Kelas ini memiliki peran penting dalam alur bisnis pra-proyek hingga transaksi. Melalui kelas `Client`, pengguna dapat:
> 1. Mengajukan keluhan ruangan lewat `submitProblem()` yang akan menghasilkan objek `TicketProblem`.
> 2. Memilih jadwal konsultasi yang diajukan admin menggunakan `choseSchedule()`.
> 3. Menyetujui hasil konsultasi proyek dengan `approveConsultation()`.
> 4. Serta melakukan transaksi pembayaran termin desain atau produksi lewat method `makePayment()` yang menginstansiasi objek `Payment` baru."

---

## Slide 4: Subclass - `Technician`

**Visual Slide:**
* Judul: "Subclass: Technician (Operational Logic)"
* Kode/UML Ringkas:
  * Pewarisan: `extends User`
  * Atribut Tambahan: `technicianId` (int), `specialization` (String)
  * Metode Utama:
    * `checkSystem(systemId)` (Simulasi survey lapangan)
    * `performTroubleShooting(ticketId)`
    * `finalInspection(ticketId)` (Quality Control)
    * `createReport(inspectionId)` $\rightarrow$ return `Report`

**Teks Presentasi:**
> "Aktor berikutnya adalah **`Technician`** (Teknisi), yang juga merupakan subclass dari `User`. Teknisi memiliki atribut khusus yaitu `technicianId` dan `specialization` untuk mengelompokkan spesialisasi keahlian teknisi (seperti instalasi listrik, desain interior, surveyor, atau staf QC).
> 
> Operasional di lapangan didefinisikan ke dalam beberapa method di kelas ini:
> * `checkSystem()` digunakan untuk memeriksa kelayakan awal sistem ruangan klien.
> * `performTroubleShooting()` mensimulasikan perbaikan masalah di lapangan.
> * `finalInspection()` memodelkan fungsi Quality Control untuk memastikan hasil instalasi sesuai dengan standar KanggoLiving.
> * Dan terakhir `createReport()` untuk menyusun dokumen laporan akhir setelah inspeksi selesai."

---

## Slide 5: Class - `Schedule`

**Visual Slide:**
* Judul: "Class: Schedule (Jadwal Kegiatan)"
* Kode/UML Ringkas:
  * Atribut: `sceduleId` (int), `date` (Date), `time` (String), `status` (String)
  * Metode Utama:
    * `setScedule(date, time)` $\rightarrow$ return `boolean`
    * `checkAvailable(requestedDate)` $\rightarrow$ return `boolean`
* Catatan Teknis: Validasi tanggal agar tidak memesan waktu yang sudah lampau.

**Teks Presentasi:**
> "Sekarang kita beralih ke kelas **`Schedule`** (Penjadwalan). Kelas ini bertindak sebagai entitas mandiri yang menjembatani koordinasi jadwal kegiatan antara Klien, Admin, dan Teknisi.
> 
> Kelas ini menyimpan data ID jadwal, tanggal, waktu, serta status penjadwalan. Proses pemesanan diverifikasi melalui method `setScedule()`, yang di dalamnya memanggil fungsi validasi `checkAvailable()`.
> 
> Di dalam `checkAvailable()`, sistem melakukan pengecekan logis: jika tanggal yang diajukan klien berada sebelum waktu saat ini (`requestedDate.before(currentDate)`), pemesanan otomatis ditolak untuk menghindari pemesanan jadwal di masa lalu. Kelas ini dirancang fleksibel sehingga ke depannya sangat mudah untuk diintegrasikan dengan database guna pengecekan slot kosong secara dinamis."

---

## Slide 6: Bagian Interface (Polymorphism & Abstraction)

**Visual Slide:**
* Judul: "Bagian Interface: Kontrak Sistem & Polymorphism"
* Diagram / Struktur 3 Interface utama:
  1. `Approvable`
     * Method: `approve()`, `getStatus()`
     * Diimplementasikan oleh: `Consultation`, `DesignProject`
  2. `Diagnosable`
     * Method: `diagnose()`, `updateCondition(condition)`
     * Diimplementasikan oleh: `SystemUnit`
  3. `Payable`
     * Method: `getAmount()`, `getStatus()`
     * Diimplementasikan oleh: `Invoice`, `Payment`

**Teks Presentasi:**
> "Bagian terakhir dan yang paling krusial untuk fleksibilitas sistem kami adalah penggunaan **Interface**. Interface dalam OOP bertindak sebagai kontrak formal yang menjamin bahwa kelas yang mengimplementasikannya pasti menyediakan fungsionalitas tertentu. Kami menggunakan 3 interface utama:
> 
> 1. **`Approvable`**: Kontrak untuk objek yang memerlukan persetujuan klien, memiliki fungsi `approve()` dan `getStatus()`. Interface ini diterapkan pada kelas sesi `Consultation` dan rancangan `DesignProject`.
> 2. **`Diagnosable`**: Kontrak untuk objek fisik/sistem ruangan yang perlu dideteksi kondisinya, memiliki fungsi `diagnose()` dan `updateCondition()`. Diimplementasikan oleh kelas `SystemUnit`.
> 3. **`Payable`**: Kontrak untuk transaksi keuangan yang memiliki tagihan/nilai bayar, dengan fungsi `getAmount()` dan `getStatus()`. Diterapkan pada kelas `Invoice` dan objek `Payment`.
> 
> Keuntungan menggunakan Interface ini adalah kami bisa menerapkan **Polymorphism**. Contohnya, modul kasir di masa depan cukup berinteraksi dengan tipe data `Payable` tanpa perlu peduli apakah itu sebuah `Invoice` (tagihan) atau `Payment` (bukti bayar), karena keduanya dijamin memiliki method `getAmount()` dan `getStatus()`."

---

## Slide 7: Penutup & Tanya Jawab

**Visual Slide:**
* Judul: "Terima Kasih & Sesi Tanya Jawab"
* Ringkasan Konsep OOP yang Dijelaskan Raihan:
  * **Abstraction**: Abstract class `User` & Interfaces.
  * **Inheritance**: `Client` & `Technician` mewarisi `User`.
  * **Polymorphism**: Interaksi objek melalui tipe interface (`Approvable`, `Diagnosable`, `Payable`).
  * **Encapsulation**: Kontrol akses properti kelas via getters & setters.

**Teks Presentasi:**
> "Kesimpulannya, perpaduan dari Abstract Class, turunan kelas (Inheritance), dan kontrak Interface membuat arsitektur sistem KanggoLiving menjadi sangat terstruktur, aman, serta mudah diperluas di masa mendatang.
> 
> Demikian penjelasan dari saya untuk bagian Aktor, Penjadwalan, dan Interface. Saya kembalikan ke moderator, atau jika ada pertanyaan dari audiens, dipersilakan. Terima kasih banyak."
