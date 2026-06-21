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
public class Client extends User {

    private String address;

    public Client(int userId, String name, String email, String password, String phone, String role, String address) {
        super(userId, name, email, password, phone, "Client");
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TicketProblem submitProblem(String description) {
        System.out.println("Klien " + super.getName() + " mengajukan masalah : " + description);

        TicketProblem newTicket = new TicketProblem(description);

        boolean isSuccess = newTicket.createTicket();

        if (isSuccess) {
            System.out.println("Berhasil memproses pengajuan Klien");
        } else {
            System.out.println("Gagal memproses pengajuan Klien");
        }

        return newTicket;
    }

    public boolean choseSchedule(Schedule schedule) {
        System.out.println("Klien " + super.getName() + " memilih jadwal #" + schedule.getSceduleId());
        return schedule.setScedule(schedule.getDate(), schedule.getTime());
    }

    public boolean approveConsultation(Consultation consultation) {
        System.out.println(
                "Klien " + super.getName() + " menyetujui sesi konsultasi #" + consultation.getConsultationId());
        return consultation.approve();
    }

    public Payment makePayment(Invoice invoice, double amount, String paymentMethod) {
        System.out.println("Klien " + super.getName() + " melakukan pembayaran sebesar Rp"
                + String.format(new java.util.Locale("in", "ID"), "%,.0f", amount) + " untuk Invoice ID: "
                + invoice.getInvoiceId());
        String paymentId = "PAY-" + System.currentTimeMillis();
        Payment newPayment = new Payment(paymentId, amount, paymentMethod, invoice);
        return newPayment;
    }

    public void viewInvoice(Invoice invoice) {
        System.out.println("=== DETAIL INVOICE ===");
        System.out.println("ID Invoice     : " + invoice.getInvoiceId());
        System.out.println(
                "Total Tagihan  : Rp" + String.format(new java.util.Locale("in", "ID"), "%,.0f", invoice.getAmount()));
        System.out.println("Jumlah Dibayar : Rp"
                + String.format(new java.util.Locale("in", "ID"), "%,.0f", invoice.getAmountPaid()));
        System.out.println("Status         : " + invoice.getStatus());
    }
}