package com.interviewhub.model;

import com.interviewhub.interfaces.Eligible;

import java.time.LocalDate;

/**
 * Academic Entity: PlacementDrive (implements Eligible)
 * Represents a campus recruitment drive with eligibility criteria.
 */
public class PlacementDrive implements Eligible {

    private int driveId;
    private int companyId;
    private String companyName;
    private String driveName;
    private double eligibilityCgpa;
    private String eligibleDepartment;
    private LocalDate applicationDeadline;
    private String status; // OPEN, CLOSED, COMPLETED

    public PlacementDrive() {
    }

    public PlacementDrive(int driveId, int companyId, String companyName, String driveName,
                          double eligibilityCgpa, String eligibleDepartment,
                          LocalDate applicationDeadline, String status) {
        this.driveId = driveId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.driveName = driveName;
        this.eligibilityCgpa = eligibilityCgpa;
        this.eligibleDepartment = eligibleDepartment;
        this.applicationDeadline = applicationDeadline;
        this.status = status;
    }

    public int getDriveId() {
        return driveId;
    }

    public void setDriveId(int driveId) {
        this.driveId = driveId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDriveName() {
        return driveName;
    }

    public void setDriveName(String driveName) {
        this.driveName = driveName;
    }

    public double getEligibilityCgpa() {
        return eligibilityCgpa;
    }

    public void setEligibilityCgpa(double eligibilityCgpa) {
        this.eligibilityCgpa = eligibilityCgpa;
    }

    public String getEligibleDepartment() {
        return eligibleDepartment;
    }

    public void setEligibleDepartment(String eligibleDepartment) {
        this.eligibleDepartment = eligibleDepartment;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Implements Eligible interface.
     * Evaluates whether a student meets CGPA and department criteria.
     */
    @Override
    public boolean isEligible(Student student) {
        if (student == null) {
            return false;
        }
        if (student.getCgpa() < this.eligibilityCgpa) {
            return false;
        }
        if ("All Engineering".equalsIgnoreCase(this.eligibleDepartment) ||
                "All".equalsIgnoreCase(this.eligibleDepartment)) {
            return true;
        }
        return this.eligibleDepartment.equalsIgnoreCase(student.getDepartment());
    }

    @Override
    public String toString() {
        return String.format("PlacementDrive [ID: %d | %s | Company: %s | Min CGPA: %.2f | Dept: %s | Deadline: %s | Status: %s]",
                driveId, driveName, companyName, eligibilityCgpa, eligibleDepartment, applicationDeadline, status);
    }
}
