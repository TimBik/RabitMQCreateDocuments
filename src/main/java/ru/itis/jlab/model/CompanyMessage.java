package ru.itis.jlab.model;

public class CompanyMessage {
    private String message;
    private String companyName;

    public CompanyMessage(String message, String companyName) {
        this.message = message;
        this.companyName = companyName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
