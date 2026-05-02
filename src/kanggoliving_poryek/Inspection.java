/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek;

/**
 *
 * @author Meliana
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Inspection {
    private int inspectionId;
    private LocalDateTime inspectionDate;
    private String result;
    private String inspectorName;
    private String notes;
    private boolean isCompleted;

    public Inspection(int inspectiomID, String inspectionName) {
        this.inspectionId = inspectionId;
        this.inspectorName = inspectorName;
        this.inspectionDate = LocalDateTime.now();
        this.isCompleted = false;
        this.result = "";
        this.notes = "";
    }
    
    public boolean performInspection() {
            System.out.println("Inspection : " + inspectionId);
            System.out.println("Memulai proses inspeksi ...");
            
            this.result = "Kondisi bangunan aman dan tidak ada kerusakan. ";
            
            this.isCompleted = true;
            return true;
    }
    
    public void recordResult(String result) {
        if (isCompleted) {
            System.out.println("Jalankan performInspection terlebih dahulu. ");
            return;   
        }
        this.result = result;
    }
    
    public String getResult() {
        if (result == null || result.isEmpty()) {
            return "Belum ada hasil inspeksi yang dicatat.";
        }
        return result;
    }
    
    public void addNotes(String notes) {
        if (!isCompleted) {
            System.out.println("Jalankan perform inspection terlebih dahulu. ");
            return;
        }
        this.notes = notes;
        System.out.println("Catatan ditambahkan. ");
    }
    
    public String getFormattedDate() {
        if (inspectionDate == null) {
            return "-";
        }
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss");
        return inspectionDate.format(formatter);
    }
    
    public void printSummary() {
        System.out.println("==========================");
        System.out.println("RINGKASAN INSPEKSI");
        System.out.println("==========================");
        System.out.println("ID Inspeksi     : #" + inspectionId);
        System.out.println("Inspektor       : " + inspectorName);
        System.out.println("Tanggal         : "  + getFormattedDate());
        System.out.println("Status          : " + (isCompleted ? "Selesai" : "Belum dilakukan"));
        System.out.println("Hasil           : " + getResult());
        System.out.println("Catatan         : " + (notes.isEmpty() ? "-" : notes));
        System.out.println("===========================");
    }
    
    public boolean isCompletted() {
        return isCompleted;
    }

    public int getInspectionId() {
        return inspectionId;
    }

    public LocalDateTime getInspectionDate() {
        return inspectionDate;
    }

    public boolean isIsCompleted() {
        return isCompleted;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public String getNotes() {
        return notes;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "InspectionID : " + inspectionId + 
                "Inspector : " + inspectorName +
                "Completed : " + isCompleted +
                "Result : " + getResult();
    }
    
    
    
}
