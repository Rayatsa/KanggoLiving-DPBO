package kanggoliving_poryek;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   SIMULASI SISTEM MANAJEMEN KANGGOLIVING        ");
        System.out.println("=================================================\n");

        // 1. Inisiasi Aktor & Objek Utama
        System.out.println("--- 1. INISIASI AKTOR ---");
        Client client = new Client(101, "Sidqi Maan", "sidqi@client.com", "sidqi123", "0812345", "Client", "Bandung");
        Admin adminFinance = new Admin(1, "Meliana", "meli@kanggo.com", "pass123", "08112233", "Finance");
        Technician technician = new Technician(201, "Instalasi & Listrik", 301, "Raihan Yassar", "raihan@tech.com", "raihan123", "089999", "Technician");

        System.out.println("Klien      : " + client.getName() + " (" + client.getAddress() + ")");
        System.out.println("Admin      : " + adminFinance.getName() + " (Role: " + adminFinance.getRole() + ")");
        System.out.println("Teknisi    : " + technician.getName() + " (Spesialisasi: " + technician.checkSystem(1) + ")"); // test method checkSystem
        System.out.println();

        // 2. Alur Pra-Proyek (Konsultasi & Penjadwalan)
        System.out.println("--- 2. ALUR PRA-PROYEK (KONSULTASI & JADWAL) ---");
        // Klien mengajukan keluhan kelayakan ruangan
        TicketProblem tiketMasalah = client.submitProblem("Instalasi listrik kamar utama sering korsleting dan perlu desain ulang.");
        
        // Admin membuatkan jadwal konsultasi
        Date tanggalKonsultasi = new Date(System.currentTimeMillis() + 86400000); // besok
        Schedule jadwal = new Schedule(501, tanggalKonsultasi, "10:00 WIB", "Pending");
        
        // Klien memilih jadwal tersebut
        client.choseSchedule(jadwal);
        
        // Membuat sesi konsultasi
        Consultation konsultasi = new Consultation(601, tanggalKonsultasi, "Pending");
        
        // Klien menyetujui hasil konsultasi
        client.approveConsultation(konsultasi);
        System.out.println();

        // 3. Alur Manajemen Desain & Budgeting
        System.out.println("--- 3. MANAJEMEN DESAIN & BUDGETING ---");
        // Desainer membuat proyek desain berdasarkan konsultasi
        DesignProject proyekDesain = new DesignProject(701, konsultasi.getConsultationId(), "Modern Japandi", 25000000.0);
        
        // Validasi budget dari Klien
        double budgetKlien = 30000000.0;
        System.out.println("Estimasi Budget Desain : Rp" + String.format(new java.util.Locale("in", "ID"), "%,.0f", proyekDesain.getEstimatedBudget()));
        System.out.println("Budget yang dimiliki Klien : Rp" + String.format(new java.util.Locale("in", "ID"), "%,.0f", budgetKlien));
        proyekDesain.validateBudget(budgetKlien);
        
        // Klien mengajukan revisi kecil
        proyekDesain.updateDesign("Ubah warna cat dinding dari krem menjadi abu-abu terang.");
        
        // Cek kesiapan produksi
        if (proyekDesain.isReadyForProduction()) {
            System.out.println("Proyek Desain SIAP untuk diproduksi!");
        }
        System.out.println();

        // 4. Alur Pembayaran DP (Down Payment)
        System.out.println("--- 4. ALUR PEMBAYARAN TERMIN DP ---");
        // Admin Finance menerbitkan Invoice DP senilai Rp 10.000.000
        adminFinance.createInvoice("INV-JAPANDI-01", 10000000.0);
        Invoice tagihanDP = new Invoice("INV-JAPANDI-01", 10000000.0);
        
        // Klien melihat rincian Invoice
        client.viewInvoice(tagihanDP);
        
        // Klien membayar DP sebesar Rp 10.000.000
        Payment bayarDP = client.makePayment(tagihanDP, 10000000.0, "Transfer BCA");
        
        // Admin memverifikasi pembayaran
        adminFinance.verifyPayment(bayarDP);
        
        // Cek status Invoice
        client.viewInvoice(tagihanDP);
        System.out.println();

        // 5. Alur Operasional, Produksi & Survei Lapangan (Teknisi)
        System.out.println("--- 5. OPERASIONAL LAPANGAN (TEKNISI) ---");
        // Teknisi melakukan survey lapangan pada unit sistem
        SystemUnit unitKamar = new SystemUnit(801, "Instalasi Listrik Kamar Utama", "Perlu Pengecekan", "SN-LSTRK-099");
        unitKamar.diagnose();
        
        // Teknisi melakukan troubleshooting
        technician.performTroubleShooting(501);
        
        // Teknisi memperbarui kondisi unit sistem ke "Baik"
        unitKamar.updateCondition("Baik");
        
        // Inspeksi Akhir (Quality Control)
        technician.finalInspection(501);
        
        // Teknisi membuat Laporan Akhir Proyek
        Report laporanAkhir = technician.createReport(801);
        laporanAkhir.printReport();
        System.out.println();

        System.out.println("=================================================");
        System.out.println("      SIMULASI SELESAI DENGAN SUKSES!            ");
        System.out.println("=================================================");
    }
}
