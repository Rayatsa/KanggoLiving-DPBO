/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek.model;

/**
 *
 * @author Meliana
 */
public class Material {
    private int materialId;
    private String materialName;
    private int quantity;
    private double unitPrice;
    private String status;

    public Material(int materialId, String materialName, int quantity, double unitPrice, String status) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        updateStatus();
    }
    
    public void updateStatus(){
        if (quantity <= 0) {
            this.status = "Empty";
        }else if (quantity <= 10) {
            this.status = "Low";
        }else{
            this.status = "In Stock";
        }
    }
    
    public boolean checkAvailability(int requiredQuantity){
        System.out.println("=== CEK KETERSEDIAAN MATERIAL ===");
        System.out.println("Material     : " + materialName);
        System.out.println("Stok Tersedia:" + quantity);
        System.out.println("Dibutuhkan   :" + requiredQuantity);
        
        if (quantity >= requiredQuantity) {
            System.out.println("Status: Stock mencukupi.");
            return true;
        }else{
            System.out.println("Status: Stok Tidak mencukupi.");
            return false;
        }
    }
    
    public boolean updateStock(int usedQuantity){
        System.out.println("=== PEMBARUAN STOK ===");
        
        if (usedQuantity > quantity) {
            System.out.println("Gagal: Jumlah yang digunakan melebihi stok yang tersedia.");
            return false;
        }
        
        this.quantity -= usedQuantity;
        updateStatus();
        
        System.out.println("Material    :" + materialName);
        System.out.println("Digunakan   :" + usedQuantity);
        System.out.println("Sisa Stok   :" + quantity);
        System.out.println("Status Baru :" + status);
        return true;
    }
    
    public double calculateMaterialCost(int quantity) {
        double totalCost = unitPrice * quantity;
        
        System.out.println("=== KALKULASI BIAYA MATERIAL ===");
        System.out.println("Material    :" + materialName);
        System.out.println("Jumlah      :" + quantity);
        System.out.println("Harga Satuan: Rp" + String.format(new java.util.Locale("in", "ID"), "%,.0f", unitPrice));
        System.out.println("Total Biaya : Rp" + String.format(new java.util.Locale("in", "ID"), "%,.0f", totalCost));
        
        return totalCost;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateStatus();
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getStatus() {
        return status;
    }   
}