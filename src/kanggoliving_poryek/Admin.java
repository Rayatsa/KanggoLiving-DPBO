/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek;

/**
 *
 * @author nadiya
 */
public class Admin extends User {

    private int adminId;

    public Admin(int adminId, int userId, String name, String email, String password, String phone, String role) {
        super(userId, name, email, password, phone, "Admin");
        this.adminId = adminId;
    }

    public boolean createInvoice(int ticketId) {
        System.out.println("Admin" + this.getName() + " berhasil membuat invoid untuk tike #" + ticketId);
        return true;
    }
    
    public boolean verifyPayment(int paymentId) {
        System.out.println("Pembayaran dengna ID " + paymentId + " telah diverifikasi oleh admin.");
        return true;
    }
    
    public boolean updateSchedule(int scheduleId) {
        System.out.println("Jadwal proyek #" + scheduleId + " telah diperbarui");
        return true;
    }
    
    public int getAdminId() {
        return adminId;
    }
}
