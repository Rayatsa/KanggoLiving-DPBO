package kanggoliving_poryek;

import kanggoliving_poryek.interfaces.Approvable;

public class DesignProject implements Approvable {
    private String status = "Pending";
    private int designId;
    private int consultationId;
    private String conceptStyle;
    private double estimatedBudget;
    private int revisionCount;

    public DesignProject(int designId, int consultationId, String conceptStyle, double estimatedBudget) {
        this.designId = designId;
        this.consultationId = consultationId;
        this.conceptStyle = conceptStyle;
        this.estimatedBudget = estimatedBudget;
        this.revisionCount = 0;
    }

    public boolean validateBudget(double clientBudget) {
        if (clientBudget >= estimatedBudget) {
            System.out.println("Budget validated successfully. Client budget is sufficient.");
            return true;
        } else {
            System.out.println("Budget validation failed. Client budget is lower than estimated budget.");
            return false;
        }
    }

    public void updateDesign(String revisionNotes) {
        this.revisionCount++;
        System.out.println("Design updated with revision notes: " + revisionNotes);
        System.out.println("Current revision count: " + revisionCount);
    }

    public boolean isReadyForProduction() {
        System.out.println("Checking if design is ready for production. Revision count: " + revisionCount);
        return true; 
    }

    // Getters and Setters
    public int getDesignId() {
        return designId;
    }

    public void setDesignId(int designId) {
        this.designId = designId;
    }

    public int getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(int consultationId) {
        this.consultationId = consultationId;
    }

    public String getConceptStyle() {
        return conceptStyle;
    }

    public void setConceptStyle(String conceptStyle) {
        this.conceptStyle = conceptStyle;
    }

    public double getEstimatedBudget() {
        return estimatedBudget;
    }

    public void setEstimatedBudget(double estimatedBudget) {
        this.estimatedBudget = estimatedBudget;
    }

    public int getRevisionCount() {
        return revisionCount;
    }

    public void setRevisionCount(int revisionCount) {
        this.revisionCount = revisionCount;
    }

    @Override
    public boolean approve() {
        this.status = "Approved";
        System.out.println("Design Project ID " + designId + " approved.");
        return true;
    }

    @Override
    public String getStatus() {
        return this.status;
    }
}
