/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek.model;

/**
 *
 * @author Meliana
 */

import java.util.Date;

public class Inspection {
    private int inspectionId;
    private Date inspectionDate;
    private String result;

    public Inspection(int inspectionId, Date inspectionDate, String result) {
        this.inspectionId = inspectionId;
        this.inspectionDate = inspectionDate;
        this.result = null;
    }
    
    public boolean performInspection() {
        System.out.println("=== INSPEKSI LAPANGAN ===");
        System.out.println("Inspeksi ID : " + inspectionId + " dimulai pada: " + inspectionDate);
        System.out.println("- Memeriksa hasil instalasi dan kesesuaian dengan standar KanggoLiving... ");
        System.out.println("- Memverifikasi kualitas material yang digunakan...");
        System.out.println("- Melakukan pengecekan akhir terhadap kondisi ruangan...");
        System.out.println("Status : Inspeksi selesai dijalankan. ");
        return true;
    }
    
    public void recordResult(String result) {
        this.result = result;
        System.out.println("Hasil Inspeksi : " + inspectionId + " berhasil dicatat: " + this.result );
    }

    public String getResult() {
        if (this.result == null || this.result.isEmpty()) {
            System.out.println("Belum ada hasil inspeksi yang tercatat untuk ID : " + inspectionId);
            return "Belum ada hasil";
        }
        return this.result;
    }

    public int getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(int inspectionId) {
        this.inspectionId = inspectionId;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }
    
    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public void setResult(String result) {
        this.result = result;
    }  
}
