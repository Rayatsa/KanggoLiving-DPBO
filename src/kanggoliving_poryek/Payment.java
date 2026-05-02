/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek;

import java.util.Date;

/**
 *
 * @author nadiya
 */
public class Payment {

    private int paymentId;
    private Date paymentDate;
    private String paymentMethod, status;
    private double amount;

    public Payment(int paymentId, Date paymentDate, String paymentMethod, String status, double amount) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.amount = amount;
    }

    public boolean confirmPayment() {
        if (this.amount > 0 && this.paymentMethod != null && this.paymentMethod.isEmpty()) {
            this.status = "Confirmed";
            System.out.println("Pembayaran dengan ID " + this.paymentId + " berhasil dikonfirmasi.");
            return true;
        } else {
            this.status = "Failed";
            System.out.println("Konfirmasi pembayaran gagal untuk ID " + this.paymentId);
            return false;
        }
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
