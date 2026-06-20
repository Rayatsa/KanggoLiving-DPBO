/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek.model;

import kanggoliving_poryek.interfaces.Diagnosable;

/**
 *
 * @author Meliana
 */
public class SystemUnit implements Diagnosable {
    private int systemId;
    private String systemName;
    private String condition;
    private String serialNumber;

    public SystemUnit(int systemId, String systemName, String condition, String serialNumber) {
        this.systemId = systemId;
        this.systemName = systemName;
        this.condition = condition;
        this.serialNumber = serialNumber;
    }
    
    public String diagnose(){
        System.out.println("=== DIAGNOSA SISTEM ===");
        System.out.println("ID Sistem   :   " + systemId);
        System.out.println("Nama Sistem     :" + systemName);
        System.out.println("Serial Number   :" + serialNumber);
        System.out.println("Kondisi Awal    :" + condition);
        System.out.println("Proses          :Menganalisis kondisi sistem secara menyeluruh...");
        
        String diagnoseResult;
        
        switch (this.condition.toLowerCase()) {
            case "rusak" :
                diagnoseResult = "Sistem mengalami kerusakan fisik . Diperlukan perbaikan atau penggantian komponen.";
                break;
            case "error" :
                diagnoseResult = "Sistem mengalami gangguan fungsional. Diperlukan troubleshooting teknis.";
                break;
            case "baik":
                diagnoseResult = "Sistem dalam kondisi baik. Tidak ditermukan kerusakan.";
                break;
            case "perlu pengecekan":
                diagnoseResult =" Sistem memerlukan pengecekan lebih lanjut oleh teknisi ahli.";
                break;
            default:
                diagnoseResult = "Kondisi sistem tidak dikenali. Diperlukan inspeksi manual oleh teknisi.";
                break;
        }
        System.out.println("Hasil Diagnosa: " + diagnoseResult);
        return  diagnoseResult;
    }
    
    public void updateCondition(String condition) {
        String kondisiLama = this.condition;
        this.condition = condition;
        
        System.out.println("=== PEMBARUAN KONDISI SISTEM ===");
        System.out.println("ID Sistem       :" + systemId);
        System.out.println("Nama Sistem     :" + systemName);
        System.out.println("Serial Number   :" + serialNumber);
        System.out.println("Kondisi Lama    :" + kondisiLama);
        System.out.println("Kondisi Terbaru :" + condition);
        System.out.println("Status          :Kondisi sistem berhasil diperbarui." );
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}