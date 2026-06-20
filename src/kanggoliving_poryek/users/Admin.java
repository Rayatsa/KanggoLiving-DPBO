/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek.users;

import kanggoliving_poryek.model.*;

/**
 *
 * @author nadiya
 */
public class Admin extends User {

    private String role;

    public Admin(int userId, String name, String email, String password, String phone, String role) {
        super(userId, name, email, password, phone, "Admin");
        this.role = role;
    }

    public boolean createInvoice(String invoiceId, double amount) {
        if(!this.role.equalsIgnoreCase("Finance") && !this.role.equalsIgnoreCase("Manager")) {
            System.out.println("Akses Ditolak!!: " + super.getName() + " bukan bagian Keuangan atau Finance");
            return false;
        }
        
        System.out.println("Memproses pembuatan faktur....");
        
        Invoice fakturBaru = new Invoice(invoiceId, amount);
        
        boolean isGenerated = fakturBaru.generateInvoice(invoiceId);
        
        if(isGenerated) {
            System.out.println("Sukses! Faktur ID " + invoiceId + " berhasil diterbitkan oleh " + super.getName());
            return true;
        } else {
            System.out.println("Gagal menerbitkan faktur.");
            return false;
        }
    }
    
    public boolean verifyPayment(Payment buktiTransfer) {
        Invoice tagihanYangMauDiBayar = buktiTransfer.getTargetInvoice();
        String idFaktur = tagihanYangMauDiBayar.getInvoiceId();
        
        System.out.println("Admin Keuangan memverifikasi Bukti Transfer ID: " + buktiTransfer.getPaymentId());
        System.out.println("-> Uang ditujukan untuk Invoice ID: " + idFaktur);
        
        if(buktiTransfer.getAmount() > 0) {
            buktiTransfer.confirmPayment();
            return true;
        } else {
            System.out.println("Nomimal TIDAK VALID!");
            return false;
        }
    }
    
    public boolean updateSchedule(int scheduleId) {
        System.out.println("Jadwal proyek #" + scheduleId + " telah diperbarui");
        return true;
    }
    
    public String getRole() {
        return this.role;
    }
}