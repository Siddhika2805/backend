package com.interviewhub.model;

/**
 * Academic Entity: Company
 * Represents an organization participating in recruitment & placement drives.
 */
public class Company {

    private int companyId;
    private String companyName;
    private String industry;
    private String location;

    public Company() {
    }

    public Company(int companyId, String companyName, String industry, String location) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.industry = industry;
        this.location = location;
    }

    public Company(String companyName, String industry, String location) {
        this.companyName = companyName;
        this.industry = industry;
        this.location = location;
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("Company [ID: %d | Name: %s | Industry: %s | Location: %s]",
                companyId, companyName, industry, location);
    }
}
