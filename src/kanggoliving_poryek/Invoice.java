/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import kanggoliving_poryek.interfaces.Payable;

/**
 *
 * @author nadiya
 */
public class Invoice implements Payable {

    private double amount, amountPaid;
    private String invoiceId, status, createDate;

    public Invoice(String invoiceId, double amount) {
        this.invoiceId = invoiceId;
        this.amount = amount;

        this.amountPaid = 0.0;
        this.status = "unPaid";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.createDate = now.format(format);
    }

    public boolean generateInvoice(String ticketId) {
        System.out.println("Membuat faktur " + this.invoiceId + " berdasarkan ID tiket: " + ticketId);
        System.out.println("Tanggal dibuat: " + this.createDate);

        boolean isSucces = true;
        return isSucces;
    }

    public boolean sendVoice(int clientId) {
        System.out.println("Faktur " + this.invoiceId + " sedang dikirimkan  ke email client ID: " + clientId);

        return true;
    }

    public double calculateTotal() {
        System.out.println("Menghitung total biaya untuk Faktur " + this.invoiceId + "...");

        double pajak = this.amount * 0.12; //PPN 2026 Juni :(
        double totalBiayaAkhir = this.amount + pajak;

        return totalBiayaAkhir;
    }

    public void addPayment(double nominalUang) {
        this.amountPaid += nominalUang;

        System.out.println("Dana masuk sebesar Rp" + String.format(new java.util.Locale("in", "ID"), "%,.0f", nominalUang) + " ke Faktur " + this.invoiceId);

        if (this.amountPaid >= this.amount) {
            this.status = "Paid";
            System.out.println("Faktur " + this.invoiceId + " telah LUNAS.");
        } else {
            this.status = "Partial";
            double sisaTagihan = this.amount - this.amountPaid;
            System.out.println("Status Faktur: PARTIAL. Sisa tagihan: Rp" + String.format(new java.util.Locale("in", "ID"), "%,.0f", sisaTagihan));
        }

    }

    public double getAmountPaid() {
        return this.amountPaid;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getStatus() {
        return this.status;
    }
    
    public String getInvoiceId() {
        return this.invoiceId;
    }

    public void printSummary() {
        System.out.println("====================");
        System.out.println("stuff");
        System.out.println("stuff");
        System.out.println("stuff");
        System.out.println("Date: " + this.createDate);
        System.out.println("====================");
    }
}
