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
public class Payment implements Payable {

    private String paymentId, paymentMethod, status, paymentDate;
    private double amount;
    private Invoice targetInvoice;

    public Payment(String paymentId, double amount, String paymentMethod, Invoice targetInvoice) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.targetInvoice = targetInvoice;
        this.status = "Pending";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.paymentDate = now.format(format);
    }

    public boolean confirmPayment() {
        this.status = "Succes";
        this.targetInvoice.addPayment(this.amount);
        return true;
    }

    public String getPaymentDate() {
        return this.paymentDate;
    }

    public String getPaymentId() {
        return this.paymentId;
    }
    
    public double getAmount() {
        return this.amount;
    }

    public Invoice getTargetInvoice() {
        return this.targetInvoice;
    }

    public String getStatus() {
        return this.status;
    }
}