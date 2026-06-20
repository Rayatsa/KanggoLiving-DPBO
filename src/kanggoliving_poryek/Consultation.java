package kanggoliving_poryek;

import java.util.Date;

public class Consultation {
    private int consultationId;
    private Date consultationDate;
    private String status;

    public Consultation(int consultationId, Date consultationDate, String status) {
        this.consultationId = consultationId;
        this.consultationDate = consultationDate;
        this.status = status;
    }

    public boolean approve() {
        this.status = "Disetujui";
        System.out.println("ID Konsultasi " + consultationId + " disetujui.");
        return true;
    }

    public boolean reschedule(Date newDate) {
        this.consultationDate = newDate;
        this.status = "Dijadwal Ulang";
        System.out.println("ID Konsultasi " + consultationId + " dijadwal ulang ke " + newDate);
        return true;
    }

    public boolean cancel() {
        this.status = "Dibatalkan";
        System.out.println("ID Konsultasi " + consultationId + " dibatalkan.");
        return true;
    }

    // Getters and Setters
    public int getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(int consultationId) {
        this.consultationId = consultationId;
    }

    public Date getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(Date consultationDate) {
        this.consultationDate = consultationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
