/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author nadiya
 */
public class TicketProblem {

    private String ticketId;
    private String problemDescription, status;
    private String createDate;

    public TicketProblem(String problemDescription) {
        this.problemDescription = problemDescription;
        this.status = "Draft";
    }

    public boolean createTicket() {
        if (this.problemDescription == null || this.problemDescription.trim().isEmpty()) {
            System.out.println("Gagal membuat tiket : Deskripsi keluhan tidak boleh kosong!!");
            return false;
        }

        this.ticketId = "TCT-" + System.currentTimeMillis();
        this.status = "Open";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.createDate = now.format(format);
        
        System.out.println("Tiket berhasil dibuat! ID : " + this.ticketId);
        return true;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}
