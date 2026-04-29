/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package kanggoliving_poryek;

/**
 *
 * @author terzy
 */
import java.util.Date;

public class Schedule {

    private int sceduleId;
    private Date date;
    private String time, status;

    public Schedule(int sceduleId, Date date, String time, String status) {
        this.sceduleId = sceduleId;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public boolean setScedule(Date date, String time) {
        if (checkAvailable(date)) {
            this.date = date;
            this.time = time;
            this.status = "Sceduled";
            return true;
        }
        return false;
    }
    
    //CLASS INI MASIH BISA DI GANTI, SEMISAL DI GANTI BISA PAKAI DB BIAR LEBIH BAGUS
    //INI MASIH METHOD SEMENTARA
    public boolean checkAvailable(Date requestedDate) {
        Date currentDate = new Date(); // Mengambil tanggal dan waktu detik sekarang

        // Mengecek apakah tanggal yang diminta sudah lewat
        if (requestedDate.before(currentDate)) {
            System.out.println("Gagal: Jadwal Telah di Booking Sebelumnya");
            return false;
        }

        System.out.println("Jadwal tersedia.");
        return true;
    }

    public int getSceduleId() {
        return sceduleId;
    }

    public void setSceduleId(int sceduleId) {
        this.sceduleId = sceduleId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
