/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek.model;

/**
 *
 * @author Meliana
 */
public class Material { // Class untuk menyimpan informasi mengenai material
    private int materialId; // ID unik Material
    private String materialName; // Nama Material
    private int quantity; // Jumlah Material
    private double unitPrice; // Harga Satuan Material
    private String status; // Status Material

    public Material(int materialId, String materialName, int quantity, double unitPrice, String status) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        updateStatus();
    }

    public void updateStatus() { // Method untuk Memperbarui Status Material
        if (quantity <= 0) { // Jika Jumlah Material Kurang Dari 0
            this.status = "Empty";
        } else if (quantity <= 10) { // Jika Jumlah Material Kurang Dari 10
            this.status = "Low";
        } else { // Jika Jumlah Material Lebih Dari 10
            this.status = "In Stock";
        }
    }

    public boolean checkAvailability(int requiredQuantity) { // Mengecek stock apakah memenuhi kebutuhan produksi
        System.out.println("=== CEK KETERSEDIAAN MATERIAL ===");
        System.out.println("Material     : " + materialName);
        System.out.println("Stok Tersedia:" + quantity);
        System.out.println("Dibutuhkan   :" + requiredQuantity);

        if (quantity >= requiredQuantity) { // Jika jumlah stock lebih banyak dari kebutuhan
            System.out.println("Status: Stock mencukupi.");
            return true;
        } else { // Jika jumlah stock kurang dari kebutuhan
            System.out.println("Status: Stok Tidak mencukupi.");
            return false;
        }
    }

    public boolean updateStock(int usedQuantity) { // Memperbarui jumla stock setiap ada perubahan
        System.out.println("=== PEMBARUAN STOK ===");

        if (usedQuantity > quantity) { // Jika jumlah stock lebih banyak dari kebutuhan
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

    public double calculateMaterialCost(int quantity) { // Menghitung total harga material
        double totalCost = unitPrice * quantity; // Menghitung total biaya

        System.out.println("=== KALKULASI BIAYA MATERIAL ==="); // Header untuk menampilkan output
        System.out.println("Material    :" + materialName); // Menampilkan nama material
        System.out.println("Jumlah      :" + quantity); // Menampilkan jumlah material
        System.out.println("Harga Satuan: Rp" + String.format(new java.util.Locale("in", "ID"), "%,.0f", unitPrice)); // Menampilkan
                                                                                                                      // harga
                                                                                                                      // satuan
        System.out.println("Total Biaya : Rp" + String.format(new java.util.Locale("in", "ID"), "%,.0f", totalCost)); // Menampilkan
                                                                                                                      // total
                                                                                                                      // biaya

        return totalCost; // Mengembalikan nilai total biaya
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