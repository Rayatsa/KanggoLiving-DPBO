package kanggoliving_poryek;

import java.util.Date;

public class Report {
    private int reportId;
    private String description;
    private Date createdDate;

    public Report(int reportId, String description, Date createdDate) {
        this.reportId = reportId;
        this.description = description;
        this.createdDate = createdDate;
    }

    public boolean generateReport(int inspectionId) {
        System.out.println("Membuat Laporan Untuk Inspeksi ID: " + inspectionId);
        return true;
    }

    public boolean printReport() {
        System.out.println("Report ID: " + reportId);
        System.out.println("Keterangan: " + description);
        System.out.println("Tanggal Pembuatan: " + createdDate);
        return true;
    }

    public boolean exportReport() {
        System.out.println("Exporting report ID: " + reportId);
        return true;
    }

    // Getters and Setters
    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
