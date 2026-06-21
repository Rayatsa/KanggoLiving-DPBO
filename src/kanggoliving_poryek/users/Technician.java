/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek.users;

import kanggoliving_poryek.model.*;

/**
 *
 * @author terzy
 */
public class Technician extends User{
    private int technicianId;
    private String specialization;

    public Technician(int technicianId, String specialization, int userId, String name, String email, String password, 
                      String phone, String role) {
        super(userId, name, email, password, phone, "Technician");
        this.technicianId = technicianId;
        this.specialization = specialization;
    }

    public boolean checkSystem (int systemId ){
        System.out.println("======SURVEY LAPANGAN=====");
        System.out.println("Teknisi: " + this.getName() + "[Specialisasi: " + this.specialization + "]");
        System.out.println("Mengecek Sistem Ruangan (ID:" + systemId + "): ");
        System.out.println("- Memeriksa struktur ruangan");
        System.out.println("- Memeriksa Jalur Aliran lListrik");
        System.out.println("- Memeriksa Jalur Air");
        System.out.println("===Status: Survei Lapangan Selesai===");
        return true;
    }
    
    public boolean performTroubleShooting(int ticketId){
        System.out.println("=== TROUBLESHOOTING ===");
        System.out.println("Teknisi " + this.getName() + " Sedang Mengidentifikasi Masalah Pada Tiket #" + ticketId);
        System.out.println("- Mencari Solusi Untuk Kendala Teknis di Lapangan...");
        System.out.println("Status: Masalah Teknis Berhasil Diselesaikan.");
        return true;
    }
    
    public boolean finalInspection(int ticketId){
        System.out.println("=== FINAL INSPECTION (QC) ===");
        System.out.println("Melakukan Quality Control untuk pengerjaan tiket #" + ticketId);
        System.out.println("- Memastikan hasil instalasi sesuai dengan desain standar KanggoLiving.");
        System.out.println("Status: Inspeksi akhir lolos QC.");
        return true;
    }
    
    public Report createReport(int inspectionId){
        System.out.println("<><><>PEMBUATAN LAPORAN<><><>");
        System.out.println("Menyusun Hasil Laporan dan Dokumen Akhir Untuk Proyek: " + inspectionId);
        System.out.println("\"Status: Laporan Siap Diserahkan ke Klien/Admin.\"");
        
        return new Report(inspectionId, "Laporan hasil inspeksi " + inspectionId, new java.util.Date());    
    }

    public String getSpecialization() {
        return this.specialization;
    }
}