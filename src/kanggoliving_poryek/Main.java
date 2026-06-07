/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek;

/**
 *
 * @author nadiya
 */
public class Main {

    public static void main(String[] args) {
        Admin adminFinance = new Admin(1, "Meliana", "meli@kanggo.com", "0811", "pass123", "Finance");

        // Buat Invoice dengan total tagihan Rp 10.000.000
        Invoice tagihanProyek = new Invoice("INV-2026", 10000000.0);
        System.out.println("ID Tagihan    : " + tagihanProyek.getInvoiceId());
        System.out.println("Total Tagihan : Rp" + tagihanProyek.getAmount());
        System.out.println("Status Awal   : " + tagihanProyek.getStatus()); // Ekspektasi: Unpaid

        System.out.println("\n=== 2. SIMULASI PEMBAYARAN DP (PARTIAL) ===");
        // Kita bypass class Client dan langsung membuat objek Payment (misal klien transfer 4 Juta)
        Payment transferDP = new Payment("PAY-001", 4000000.0, "Transfer BCA", tagihanProyek);
        System.out.println("Tiket Pembayaran dibuat otomatis pada: " + transferDP.getPaymentDate());

        // Admin memverifikasi tiket pembayaran tersebut
        adminFinance.verifyPayment(transferDP);

        // Cek kondisi Invoice setelah DP diverifikasi
        System.out.println("-> Total Uang Masuk : Rp" + tagihanProyek.getAmountPaid()); // Ekspektasi: 4000000.0
        System.out.println("-> Status Tagihan   : " + tagihanProyek.getStatus()); // Ekspektasi: Partial

        System.out.println("\n=== 3. SIMULASI PEMBAYARAN TIDAK VALID (NEGATIVE TEST) ===");
        // Skenario: Bukti transfer palsu atau nominal 0
        Payment transferBodong = new Payment("PAY-002", 0.0, "Transfer Mandiri", tagihanProyek);

        // Admin memverifikasi
        adminFinance.verifyPayment(transferBodong);

        // Cek kondisi, pastikan uang tidak bertambah
        System.out.println("-> Total Uang Masuk : Rp" + tagihanProyek.getAmountPaid()); // Ekspektasi: Tetap 4000000.0

        System.out.println("\n=== 4. SIMULASI PELUNASAN SISA TAGIHAN ===");
        // Sisa tagihan adalah 6 Juta. Kita buatkan tiket pembayaran kedua.
        Payment transferLunas = new Payment("PAY-003", 6000000.0, "Transfer BCA", tagihanProyek);

        // Admin memverifikasi transfer kedua
        adminFinance.verifyPayment(transferLunas);

        // Cek kondisi Invoice setelah pelunasan
        System.out.println("-> Total Uang Masuk : Rp" + tagihanProyek.getAmountPaid()); // Ekspektasi: 10000000.0
        System.out.println("-> Status Tagihan   : " + tagihanProyek.getStatus()); // Ekspektasi: Paid

        System.out.println("\n=== PENGUJIAN SELESAI ===");
    }
}
